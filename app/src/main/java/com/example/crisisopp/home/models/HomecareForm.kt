package com.example.crisisopp.home.models

import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
data class HomecareForm (
    var fullName: String = "",
    var mothersName: String = "",
    var birthDate: String = "",
    var bloodType: String = "",
    var placeOfResidence: String = "",
    var dateOfPrescription: String? = "",
    var recordNumber: Int = 0,
    var lastPcrDate: String? = "",
    var phoneNumber: String = "",
    var doctorsName: String = "",
    var firstDocumentReference: String? = "",
    var secondDocumentReference: String? = "",
    var originatorId: String = "",
    var ainWzeinApproval: Int = 0,
    var farahApproval: Int = 0,
    var mainApproval: Int = 0,
    var municipalityName: String = "",
    override var formType: String = "",
    override var formID: String = "",
    override var appointmentDate: String = "",
    override var appointmentTime: String = "",
    override var appointmentLocation: String = ""
): IForm, Serializable
{
    companion object{
        const val FIELD_FORM_NAME = "fullName"
        const val FIELD_FORM_PHONE_NUMBER= "phoneNumber"
        const val FIELD_AIN_WZEIN_APPROVAL = "ainWZeinApproval"
        const val FIELD_MAIN_APPROVAL = "mainApproval"
        const val FIELD_FARAH_APPROVAL = "farahApproval"
    }
}
