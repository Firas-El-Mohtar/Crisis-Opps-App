package com.example.crisisopp.logIn.models

import java.io.Serializable

/**eved from L
 * Data class that captures user information for logged in users retrioginRepository
 */
data class LoggedInUser(
        val userId: String,
        val email: String
): Serializable