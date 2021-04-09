package com.example.crisisopp.home.datasource



import android.util.Log
import com.example.crisisopp.FarahFoundation.TAG
import android.view.View
import android.widget.Toast
import com.example.crisisopp.home.models.*

import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.notifications.NotificationData
import com.example.crisisopp.notifications.PushNotification
import com.example.crisisopp.notifications.RetrofitInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class HomeDataSource {

    val db = Firebase.firestore
    var currentUser = Firebase.auth.currentUser

    val hashMap: HashMap<String, String> = hashMapOf("PCR" to "pcrforms", "Homecare" to "forms")
    val appointmentHashMap: HashMap<String, String> =
        hashMapOf("Pcr" to "pcrappointments", "Homecare" to "homecareappointments")

    suspend fun updateFormApproval(userType: String, form: IForm, isApproved: Boolean) {
        hashMap.get(form.formType)?.let {
            val value = if (isApproved) 1 else -1
            val querySnapshot = db.collection(it).whereEqualTo("formID", form.formID).get().await()
            val document = querySnapshot.documents.firstOrNull()
            when (userType.toLowerCase()) {
                "farah" -> {
                    val farahApproval = hashMapOf("farahApproval" to value)
                    document?.reference?.set(farahApproval, SetOptions.merge())
                }
                "main" -> {
                    val mainApproval = hashMapOf("mainApproval" to value)
                    document?.reference?.set(mainApproval, SetOptions.merge())
                }
                "ainwzein" -> {
                    val aynWZaynApproval = hashMapOf("ainWzeinApproval" to value)
                    document?.reference?.set(aynWZaynApproval, SetOptions.merge())
                }
                else -> return
            }
        }
    }


    fun getCurrentUserId(): String {
        return currentUser.uid
    }

    fun saveForm(homeCareForm: HomeCareForm) {
        db.collection("forms").add(homeCareForm)
    }

    fun savePcrForm(pcrForm: PcrForm) {
        db.collection("pcrforms").add(pcrForm)
    }

    fun querySelector(usertype: String, municipalityName: String): Query? {
        var query: Query? = null
        when (usertype.toLowerCase()) {
            "local" -> query =
                db.collection("forms").whereEqualTo("municipalityName", municipalityName)
                    .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
            "farah" -> query = db.collection("forms").whereEqualTo("mainApproval", 1)
                .whereEqualTo("ainWzeinApproval", 1)
                .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
            else -> query =
                db.collection("forms").orderBy("farahApproval", Query.Direction.DESCENDING)
                    .limit(50)
        }
        return query
    }


    fun pcrQuerySelector(usertype: String, municipalityName: String): Query? {
        var query: Query? = null
        when (usertype.toLowerCase()) {
            "local" -> query =
                db.collection("pcrforms").whereEqualTo("municipalityName", municipalityName)
                    .orderBy("ainWzeinApproval", Query.Direction.DESCENDING)
            "ainwzein" -> query =
                db.collection("pcrforms").orderBy("ainWzeinApproval", Query.Direction.DESCENDING)

        }
        return query
    }

    fun onFormUploadSendNotification(token: String) {
        PushNotification(
            NotificationData("خلية الأزمة", "تم إرسال طلبك بنجاح"),
            token
        ).also {
            sendNotificationRetrofit(it)
        }
    }


    suspend fun getUserToken(userId: String): String? {
        var user = getUserInfo(userId)
        return user?.token
    }

    suspend fun getUserInfo(userId: String): User? {
        getUserDocument(userId)?.let {
            return it.toObject<User>()
        } ?: return null
    }

    suspend fun getUserDocument(userId: String): DocumentSnapshot? {

        val query =
            db.collection("users").whereEqualTo("userId", userId).get().await()
        return if (query.isEmpty) {
            null
        } else {
            query.documents.first()
        }
    }

    fun autoSendNotification(userType: String, token: String, b: Boolean) {

        when (b) {
            false -> {
                PushNotification(
                    NotificationData("تم رفوض طلبك", "الرجاء مراجعة الطلب"),
                    token
                ).also {
                    sendNotificationRetrofit(it)
                }
            }

            true -> {
                PushNotification(
                    NotificationData("تمت الموافقة على طلبك", "اضغط لرؤية تاريخ الموعد"),
                    token
                ).also {
                    sendNotificationRetrofit(it)
                }
            }
        }
    }
    fun uploadImageToStorage(uuid: String): StorageReference?{
        val storage = FirebaseStorage.getInstance()
        val storageReference = storage!!.reference
        return storageReference?.child("images/" + uuid)
    }


    fun sendNotificationRetrofit(notification: PushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }
        }

    suspend fun getUserParams(userId: String): User? {
        return getUserInfo(userId)
    }

    suspend fun getOriginatorId(userId: String): String? {
        val user = getUserInfo(userId)
        return user?.userId
    }

    fun uploadPcrAppointment(appointment: PcrAppointment) {
        db.collection("pcrappointments").add(appointment)
    }

    fun uploadHomecareAppointment(appointment: HomecareAppointment) {
        db.collection("homecareappointments").add(appointment)
    }

    suspend fun deleteAppointment(appointment: IAppointment) {
        appointmentHashMap.get(appointment.appointmentType)?.let {
            val querySnapshot =
                db.collection(it).whereEqualTo("appointmentId", appointment.appointmentId).get()
                    .await()
            val document = querySnapshot.documents.firstOrNull()
            document?.reference?.delete()
        }
    }

    fun homecareAppointmentQuerySelector(userType: String, municipalityName: String): Query? {
        var query: Query? = null

        when (userType) {
            "local" -> {
                query =
                    db.collection("homecareappointments")
                        .whereEqualTo("municipalityName", municipalityName)
                        .orderBy("time", Query.Direction.DESCENDING)
            }
            "ainwzein" -> {
                query =
                    db.collection("homecareappointments")
                        .orderBy("time", Query.Direction.DESCENDING)
            }
            "farah" -> {
                query =
                    db.collection("homecareappointments")
                        .orderBy("time", Query.Direction.DESCENDING)
            }
        }
        return query

    }
    fun getStorageReference(homeCareForm: HomeCareForm): StorageReference {
        return FirebaseStorage.getInstance().getReferenceFromUrl(getImageReference(homeCareForm))
    }
    fun getImageReference(homeCareForm: HomeCareForm): String{
        return "gs://crisis-opps-app.appspot.com/images/${homeCareForm.documentReference}"
    }


    fun pcrAppointmentQuerySelector(userType: String, municipalityName: String): Query? {
        var query: Query? = null
        when (userType) {
            "local" -> {
                query =
                    db.collection("pcrappointments")
                        .whereEqualTo("municipalityName", municipalityName)
                        .orderBy("time", Query.Direction.DESCENDING)
            }
            "ainwzein" -> {
                query =
                    db.collection("pcrappointments")
                        .orderBy("time", Query.Direction.DESCENDING)
            }
        }
        return query
    }

    suspend fun getAppointmentInfo(appointmentId: String, appointmentType: String): IAppointment? {
        getAppointmentDocument(appointmentId, appointmentType)?.let {
            return it.toObject<IAppointment>()
        } ?: return null
    }
    suspend fun getAppointmentDocument(appointId: String, appointmentType: String): DocumentSnapshot? {
//        var query: QuerySnapshot? = null
        var b: Boolean? = null
        var doc: DocumentSnapshot? = null
        hashMap.get(appointmentType)?.let {
            val query =
                db.collection(it).whereEqualTo("appointmentId", appointId).get().await()
            b = query.isEmpty
            doc = query.documents.first()
        }
        return if (b!!) {
            null
        } else {
            doc
        }
    }

    fun logOut(){
        currentUser = null
    }
}



