package com.example.crisisopp.home.datasource

import android.view.View
import android.widget.Toast
import android.util.Log
import com.example.crisisopp.FarahFoundation.TAG
import com.example.crisisopp.home.models.Form
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.notifications.NotificationData
import com.example.crisisopp.notifications.PushNotification
import com.example.crisisopp.notifications.RetrofitInstance
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class HomeDataSource {

    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser

    fun getCurrentUserId(): String {
        return currentUser.uid
    }

    fun saveForm(form: Form) {
        db.collection("forms").add(form)
    }
    fun savePcrForm(pcrForm: PcrForm){
        db.collection("pcrforms").add(pcrForm)
    }

    fun querySelector(usertype: String, municipalityName: String): Query? {
        var query: Query? = null
        if(usertype == "local"){
            query = db.collection("forms").whereEqualTo("municipalityName", municipalityName ).orderBy("formID", Query.Direction.DESCENDING).limit(50)
        }else {
            query = db.collection("forms").orderBy("recordNumber", Query.Direction.DESCENDING).limit(50)
        }
        return query
    }

    fun onFormUploadSendNotification(
        token: String,
    ) {
        PushNotification(
            NotificationData("تم إرسال طلبك بنجاح", ""),
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

    fun approveForm(formId: String, userType: String) {

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
}

