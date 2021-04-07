package com.example.crisisopp.home.models

data class PcrForm (
    var fullName: String = "",
    var mothersName: String = "",
    var birthDate: String = "",
    var bloodType: String = "",
    var placeOfResidence: String = "",
    var dateOfInfection: String? = "",
    var recordNumber: Int = 0,
    var phoneNumber: String = "",
    var nameOfSource: String = "",
    var originatorId: String = "",
    var ainWzeinApproval: Int = 0,
    var additionalNotes: String = "",
    override var formType: String = "PCR",
    override var formID: String = ""
) :IForm