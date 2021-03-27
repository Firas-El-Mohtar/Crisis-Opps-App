package com.example.crisisopp.logIn.models

/**
 * Authentication result : success (user details) or error message.
 */
data class LoginResult(
        val success: User? = null,
        val error: Exception? = null
)