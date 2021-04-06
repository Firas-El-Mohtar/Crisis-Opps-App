package com.example.crisisopp.home.datasource

import android.util.Log
import com.example.crisisopp.FarahFoundation.TAG
import com.example.crisisopp.home.models.HomeCareForm
import com.example.crisisopp.home.models.IForm
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.notifications.NotificationData
import com.example.crisisopp.notifications.PushNotification
import com.example.crisisopp.notifications.RetrofitInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
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
    val currentUser = Firebase.auth.currentUser
    val hashMap:HashMap<String, String> = hashMapOf("PCR" to "pcrforms", "Homecare" to "forms")

    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null

    fun getStorageReference(homeCareForm: HomeCareForm): StorageReference{
        return FirebaseStorage.getInstance().getReferenceFromUrl(getImageReference(homeCareForm))
    }
    fun getImageReference(homeCareForm: HomeCareForm): String{
        return "gs://crisis-opps-app.appspot.com/images/${homeCareForm.documentReference}"
    }
    fun uploadImageToStorage(uuid: String): StorageReference?{
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        return storageReference?.child("images/" + uuid)
    }

    suspend fun updateFormApproval(userType: String, form: IForm, isApproved: Boolean){
        hashMap.get(form.formType)?.let {
            val value = if(isApproved) 1 else -1
            val querySnapshot = db.collection(it).whereEqualTo("formID", form.formID).get().await()
            val document = querySnapshot.documents.firstOrNull()
            when(userType.toLowerCase()){
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
    fun savePcrForm(pcrForm: PcrForm){
        db.collection("pcrforms").add(pcrForm)
    }

    fun querySelector(usertype: String, municipalityName: String): Query? {
        var query: Query? = null
        if(usertype == "local"){
            query = db.collection("forms").whereEqualTo("municipalityName", municipalityName).orderBy(
                "formID",
                Query.Direction.DESCENDING
            ).limit(50)
        }else {
            query = db.collection("forms").orderBy("recordNumber", Query.Direction.DESCENDING).limit(
                50
            )
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
        val query = db.collection("users").whereEqualTo("userId", userId).get().await()
        return if (query.isEmpty) {
            null
        } else {
            query.documents.first()
        }
    }

    fun autoSendNotification(userType: String, token: String) {

        when (userType) {
            "ainwzein" -> {
                PushNotification(
                    NotificationData("تمت الموافقة على طلبك", "اضغط لرؤية تاريخ الموعد"),
                    token
                ).also {
                    sendNotificationRetrofit(it)
                }
            }

            "farah" -> {

            }
        }
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
}

