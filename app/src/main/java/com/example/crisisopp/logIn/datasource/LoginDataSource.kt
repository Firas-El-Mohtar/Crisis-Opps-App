package com.example.crisisopp.logIn.datasource

import com.example.crisisopp.logIn.models.LoggedInUser
import com.example.crisisopp.logIn.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.IOException

class LoginDataSource {
    private var auth: FirebaseAuth = Firebase.auth
    private val usersCollectionRef = Firebase.firestore.collection("users")



    /**
     * Returns the absolute value of the given number.
     * @param number The number to return the absolute value for.
     * @return The absolute value.
     */
    suspend fun login(email: String, password: String): LoggedInUser {
        try{
            val result = auth.signInWithEmailAndPassword(email, password).await()
            return LoggedInUser(result.user.uid, result.user.email)
        }catch (e: Exception) {
            throw e
        }
    }

    fun logout() {
        auth.signOut()
    }

    suspend fun fetchToken(): String {
        try {
            val instanceIdResult = FirebaseInstanceId.getInstance().instanceId.await()
            return instanceIdResult.token
        }catch (e: java.lang.Exception){
            throw e
        }
    }

    suspend fun updateUserInfo(user: User){
        usersCollectionRef.add(user).await()

    }
//
//    fun changeUserType(string: String){
//        usersCollectionRef.
//    }
}