package com.example.crisisopp.logIn.models

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(var token: String? = "", var userId: String? = "", var userType: String? = "") {

    companion object {

        const val FIELD_TOKEN = "token"
        const val FIELD_USER_ID = "userID"
        const val FIELD_USER_TYPE = "userType"

    }
}

