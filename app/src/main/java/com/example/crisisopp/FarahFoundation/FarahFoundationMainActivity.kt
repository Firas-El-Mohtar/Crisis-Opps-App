package com.example.crisisopp.FarahFoundation

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.example.crisisopp.R
import com.example.crisisopp.RecyclerView.RecyclerViewFragment
import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.notifications.FirebaseService
import com.example.crisisopp.notifications.NotificationData
import com.example.crisisopp.notifications.PushNotification
import com.example.crisisopp.notifications.RetrofitInstance
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

val TAG = "" //todo
const val TOPIC = "/topics/myTopic2"
private val usersCollectionRef = Firebase.firestore.collection("users")

class FarahFoundationMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farah_foundation_main)

        val etToken = findViewById<EditText>(R.id.etToken)
        val btnSend = findViewById<Button>(R.id.send_notif_button)
        val etTitle = findViewById<EditText>(R.id.etTitle)
        val etMessage = findViewById<EditText>(R.id.etMessage)
//        FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
//            FirebaseService.token = it.token

            val masterToken = it.token


            //            etToken.setText(it.token)
//        }
//                FirebaseInstallations.getInstance().getToken(false).addOnSuccessListener {
//            FirebaseService.token = it.token
//            etToken.setText(it.token)
//        }
//        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)
//
//        btnSend.setOnClickListener {
//            val title = "Message 1"
//            val message = "message"
//            val recipientToken = etToken.text.toString()
//            if(title.isNotEmpty() && message.isNotEmpty() && recipientToken.isNotEmpty()) {
//                PushNotification(
//                    NotificationData(title, message),
//                    recipientToken
//                ).also {
//                    sendNotification(it)
//                }
//            }
//        }
//    }
            fun createUserToken(user: FirebaseUser) = CoroutineScope(Dispatchers.IO).launch {

            }

            fun sendNotification(notification: PushNotification) =
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
    }
}

