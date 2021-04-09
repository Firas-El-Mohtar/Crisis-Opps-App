package com.example.crisisopp.home.models

data class PcrAppointment (
    var date: String,
    var time: String,
    var userId: String,
    var appointmentType: String = "Pcr"
)