package com.example.crisisopp.home.datasource

import android.content.ContentValues.TAG
import android.util.Log
import com.example.crisisopp.home.models.HomecareForm
import com.example.crisisopp.home.models.IForm
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.notifications.NotificationData
import com.example.crisisopp.notifications.PushNotification
import com.example.crisisopp.notifications.RetrofitInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
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


    fun refreshToken(){
        var auth = Firebase.auth
        var doc: DocumentSnapshot?
        var docObject: User?
        val usersCollectionRef = Firebase.firestore
        auth.currentUser?.uid?.let {
            GlobalScope.launch {
                val query =
                    usersCollectionRef.collection("users")
                        .whereEqualTo("userId", it).get().await()

                doc = query.documents.first()
                docObject = doc?.toObject<User>()

                var token = fetchCurrentUserToken()
                if (docObject?.token != token) {
                    val tokenHashMap = hashMapOf("token" to token)
                    doc?.reference?.set(tokenHashMap, SetOptions.merge())
                }
            }
        }
    }

    fun getCurrentUserId(): String {
        return currentUser.uid
    }

    fun saveForm(homecareForm: HomecareForm) {
        db.collection("forms").add(homecareForm)
    }

    fun savePcrForm(pcrForm: PcrForm) {
        db.collection("pcrforms").add(pcrForm)
    }

    fun getFirstStorageReference(homecareForm: HomecareForm): StorageReference {
        return FirebaseStorage.getInstance()
            .getReferenceFromUrl(getFirstImageReference(homecareForm))
    }

    fun getFirstImageReference(homecareForm: HomecareForm): String {
        return "gs://crisis-opps-app.appspot.com/images/${homecareForm.firstDocumentReference}"
    }

    fun getSecondStorageReference(homecareForm: HomecareForm): StorageReference {
        return FirebaseStorage.getInstance()
            .getReferenceFromUrl(getSecondImageReference(homecareForm))
    }

    fun getSecondImageReference(homecareForm: HomecareForm): String {
        return "gs://crisis-opps-app.appspot.com/images/${homecareForm.secondDocumentReference}"
    }

    fun querySelector(usertype: String, municipalityName: String): Query? {
        var query: Query? = null
        when (usertype.toLowerCase()) {
            "local" -> query =
                db.collection("forms").whereEqualTo("municipalityName", municipalityName.toLowerCase())
                    .orderBy("dateOfUpload", Query.Direction.DESCENDING).limit(50)
            "farah" -> query = db.collection("forms").whereEqualTo("mainApproval", 1)
                .whereEqualTo("ainWzeinApproval", 1)
                .orderBy("dateOfUpload", Query.Direction.DESCENDING).limit(50)
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
                    .orderBy("dateOfUpload", Query.Direction.DESCENDING)
            "ainwzein" -> query =
                db.collection("pcrforms").orderBy("dateOfUpload", Query.Direction.DESCENDING)
            "main" -> query =
                db.collection("pcrforms").orderBy("dateOfUpload", Query.Direction.DESCENDING)

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

    fun autoSendNotification(token: String, b: Boolean) {

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

    fun uploadImageToStorage(uuid: String): StorageReference? {
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

    suspend fun uploadPcrAppointment(formId: String, appointment: String) {
        val querySnapshot =
            db.collection("pcrforms").whereEqualTo("formID", formId).get().await()
        val document = querySnapshot.documents.firstOrNull()
        val newAppointment = hashMapOf("appointment" to appointment)
        document?.reference?.set(newAppointment, SetOptions.merge())
    }

    suspend fun uploadHomecareAppointment(formId: String, appointment: String) {
        val querySnapshot =
            db.collection("forms").whereEqualTo("formID", formId).get().await()
        val document = querySnapshot.documents.firstOrNull()
        val newAppointment = hashMapOf("appointment" to appointment)
        document?.reference?.set(newAppointment, SetOptions.merge())
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

    suspend fun getAppointmentDocument(
        appointId: String,
        appointmentType: String
    ): DocumentSnapshot? {
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

    suspend fun fetchCurrentUserToken(): String {
        try {
            val instanceIdResult = FirebaseInstanceId.getInstance().instanceId.await()
            return instanceIdResult.token
        } catch (e: java.lang.Exception) {
            throw e
        }
    }

    fun querySelectorWithFilter(
        userType: String,
        municipalityName: String,
        filter: String
    ): Query? {
        var query: Query? = null
        when (userType.toLowerCase()) {
            "local" -> {
                when (filter) {
                    "Approved" -> {
                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                    "Requested" -> {
                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .whereEqualTo("farahApproval", 0)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                    "Rejected" -> {
                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .whereEqualTo("farahApproval", -1)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                }
            }
            "farah" -> {
                when (filter) {
                    "Approved" -> {
                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                    "Requested" -> {
                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .whereEqualTo("farahApproval", 0)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                    "Rejected" -> {

                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .whereEqualTo("farahApproval", -1)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                }
            }
            else -> {
                when (filter) {

                    "Approved" -> {

                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                    "Requested" -> {
                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .whereEqualTo("farahApproval", 0)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                    "Rejected" -> {

                        query = db.collection("forms")
                            .whereEqualTo("municipalityName", municipalityName)
                            .whereEqualTo("farahApproval", -1)
                            .orderBy("farahApproval", Query.Direction.DESCENDING).limit(50)
                    }
                }
            }
        }
        return query
    }


    //todo new query to filter by type
    // add date of creation to forms objects    DONE
    // sort queries based on date  DONE
    // new state for forms: Passed/Done
    // content dialogs fix  DONE
    // login buttons learn more + need help

}



