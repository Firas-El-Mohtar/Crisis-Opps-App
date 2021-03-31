package com.example.crisisopp.home.models

data class Form (
    var formID: String,
    var fullName: String,
    var mothersName: String,
    var birthDate: String,
    var bloodType: String,
    var placeOfResidence: String,
    var dateOfPrescription: String?,
    var recordNumber: Int,
    var lastPcrDate: String?,
    var phoneNumber: String,
    var doctorsName: String,
    var documentReference: String?,
    var originatorId: String,
    var ainWzeinApproval: Int,
    var farahApproval: Int,
    var mainApproval: Int,
    var originatorToken: String,
    var municipalityName: String
)