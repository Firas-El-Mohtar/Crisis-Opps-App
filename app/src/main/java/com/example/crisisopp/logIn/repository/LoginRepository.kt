package com.example.crisisopp.logIn.repository

import android.content.SharedPreferences
import com.example.crisisopp.logIn.datasource.LoginDataSource
import com.example.crisisopp.logIn.models.LoggedInUser
import com.example.crisisopp.logIn.models.User

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

    suspend fun login(email: String, password: String): LoggedInUser {
        // handle login

        return dataSource.login(email, password)
    }

    private fun setLoggedInUser(loggedInUser: User) {
        this.user = loggedInUser
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
    }

    /**
     * Fetches the user token from shared preferences if token is found there, if not, fetches the token from firebase
     */
    suspend fun fetchToken(): String {
        token?.let {
            return it
        } ?: run {
            var instanceIdToken = dataSource.fetchToken()
            setInstanceIdToken(instanceIdToken)
            return instanceIdToken
        }
    }

    fun setInstanceIdToken(token: String) {
        this.token = token
    }

    suspend fun updateUserInfo(userId: String, userType: String, municipalityName: String) {
        val user = User(token, userId, userType, municipalityName)
        dataSource.updateUserInfo(user!!)
        setLoggedInUser(user)
    }
}