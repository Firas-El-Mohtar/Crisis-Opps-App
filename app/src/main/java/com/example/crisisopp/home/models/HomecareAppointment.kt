package com.example.crisisopp.home.models

data class HomecareAppointment (
    var date: String,
    var time: String,
    var location: String?,
    var userId: String,
    var appointmentType: String = "Homecare"
)