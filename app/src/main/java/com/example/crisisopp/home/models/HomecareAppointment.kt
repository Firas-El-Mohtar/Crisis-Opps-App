package com.example.crisisopp.home.models

import java.io.Serializable

data class HomecareAppointment (
    var date: String = "",
    var time: String = "",
    var location: String? = "",
    override var appointmentType: String = "",
    override var appointmentId: String = "",
    override var userId: String = "",

    ):IAppointment, Serializable