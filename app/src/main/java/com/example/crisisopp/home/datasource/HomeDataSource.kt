package com.example.crisisopp.home.datasource

import android.view.View
import android.widget.Toast
import com.example.crisisopp.home.models.Form
import com.example.crisisopp.notifications.NotificationData
import com.example.crisisopp.notifications.PushNotification
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeDataSource {

    val db = Firebase.firestore
    val currentUser = Firebase.auth.currentUser

    fun master(usertype: String, mainFAB: FloatingActionButton) {
        if (usertype == "local") {
            displayActionButtonStatus(mainFAB)

        } else hideActionButton(mainFAB)

    }

    fun displayActionButtonStatus(mainFAB: FloatingActionButton) {
        mainFAB.visibility = View.VISIBLE
    }

    fun hideActionButton(mainFAB: FloatingActionButton) {
        mainFAB.visibility = View.GONE
    }

    fun getUserId(): String {
        return currentUser.uid
    }

    fun saveForm(form: Form) {
        db.collection("forms").add(form)
    }

    fun querySelector(usertype: String, municipalityName: String): Query?{
        var query: Query? = null
        if(usertype == "local"){
            query = db.collection("forms").whereEqualTo("municipalityName", municipalityName ).orderBy("formId", Query.Direction.DESCENDING).limit(50)
            var query1 = query
        }else {
            query = db.collection("forms").orderBy("recordNumber", Query.Direction.DESCENDING).limit(50)
        }
        return query
    }

    fun sendNotification(
        token: String,
        usertype: String,
        notificationTitle: String,
        notificationContent: String
    ) {
        if (usertype == "farah") {
            val recipientToken = token
            if (notificationTitle.isNotEmpty() && notificationContent.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(notificationTitle, notificationContent),
                    recipientToken
                )
            }
        } else if (usertype == "hospital") {
            val recipientToken = token
            if (notificationTitle.isNotEmpty() && notificationContent.isNotEmpty() && recipientToken.isNotEmpty()) {
                PushNotification(
                    NotificationData(notificationTitle, notificationContent),
                    recipientToken
                )
            }
        }
    }
}


