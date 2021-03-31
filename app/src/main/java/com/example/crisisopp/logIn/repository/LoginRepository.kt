package com.example.crisisopp.logIn.repository

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.crisisopp.logIn.datasource.LoginDataSource
import com.example.crisisopp.logIn.models.LoggedInUser
import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.notifications.FirebaseService
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.tasks.await
import java.lang.Exception
import javax.sql.DataSource

class LoginRepository(val dataSource: LoginDataSource, var sharedPref: SharedPreferences) {
    val TAG = "LOGIN"
    private var token: String?
             get() {
                return sharedPref?.getString("token", null)
            }
            set(value) {
                sharedPref?.edit()?.putString("token", value)?.apply()
            }
    // in-memory cache of the loggedInUser object
    var user: User? = null
        private set

    val isLoggedIn: Boolean
        get() = user != null

    init {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        user = null
    }

    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(email: String, password: String): LoggedInUser {
        // handle login
        return dataSource.login(email, password)
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    suspend fun fetchToken(): String {
        token?.let {
            return it
        } ?: run{
            var instanceIdToken = dataSource.fetchToken()
            setInstanceIdToken(instanceIdToken)
            return instanceIdToken
        }
    }

    fun setInstanceIdToken(token: String) {
        this.token = token
    }

    suspend fun updateUserInfo(userId: String, userType: String) {
        val user = User(token, userId, userType)

        var firasuser = dataSource.getUserInfo(userId)
        dataSource.updateUserInfo(firasuser!!)

        setLoggedInUser(user)
    }
}