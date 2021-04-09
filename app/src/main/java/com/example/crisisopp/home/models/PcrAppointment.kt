package com.example.crisisopp.home.models

import java.io.Serializable

data class PcrAppointment (
    var date: String = "",
    var time: String = "",
    override var appointmentId: String = "",
    override var appointmentType: String ="",
    override var userId: String = ""
):IAppointment, Serializable