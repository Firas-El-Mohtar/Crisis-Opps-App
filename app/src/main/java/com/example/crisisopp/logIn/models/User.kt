package com.example.crisisopp.logIn.models

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class User(var token: String? = "", var userId: String? = "", var userType: String? = "", var municipalityName: String? = ""): Serializable {
    companion object {
        const val FIELD_TOKEN = "token"
        const val FIELD_USER_ID = "userID"
        const val FIELD_USER_TYPE = "userType"

    }
}

