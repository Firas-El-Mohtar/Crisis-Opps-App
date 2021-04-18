package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource
import com.example.crisisopp.home.models.*

import com.google.firebase.firestore.Query
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.tasks.await

class HomeRepository(
    val homeDataSource: HomeDataSource,
    val userType: String,
    val municipalityName: String
) {


    //User Functions
    fun getUserId(): String {
        return homeDataSource.getCurrentUserId()
    }

    suspend fun getUserParams(userId: String): String? {
        return homeDataSource.getUserToken(userId)
    }

    //Form Functions
    fun uploadHomeCareForm(homecareForm: HomecareForm) {
        homeDataSource.saveForm(homecareForm)
    }

    fun uploadPcrForm(pcrForm: PcrForm) {
        homeDataSource.savePcrForm(pcrForm)
    }

    suspend fun updateFormApproval(form: IForm, isApproved: Boolean) {
        homeDataSource.updateFormApproval(userType, form, isApproved)
    }

    fun onFormUploadSendNotification(token: String) {
        homeDataSource.onFormUploadSendNotification(token)
    }

    suspend fun autoSendNotification(userId: String, b: Boolean) {
        homeDataSource.autoSendNotification(getFormSenderToken(userId)!!, b)

    }

    fun getStorageReference(homecareForm: HomecareForm): StorageReference {
        return homeDataSource.getStorageReference(homecareForm)
    }

    fun querySelector(): Query? {
        return homeDataSource.querySelector(userType, municipalityName)
    }

    fun pcrQuerySelector(): Query? {
        return homeDataSource.pcrQuerySelector(userType, municipalityName)
    }

    suspend fun getFormSenderToken(userId: String): String? {
        return homeDataSource.getUserToken(userId)
    }

    //Appointment Functions
    suspend fun uploadPcrAppointment(formId: String, appointment: String) {
        homeDataSource.uploadPcrAppointment(formId, appointment)
    }

    suspend fun uploadHomecareAppointment(formId: String, appointment: String) {
        homeDataSource.uploadHomecareAppointment(formId, appointment)
    }

    fun pcrAppointmentQuerySelector(): Query? {
        return homeDataSource.pcrAppointmentQuerySelector(userType, municipalityName)
    }

    fun homecareAppointmentQuerySelector(): Query? {
        return homeDataSource.homecareAppointmentQuerySelector(userType, municipalityName)
    }

    //Image handling
    fun uploadImageToStorage(uuid: String): StorageReference? {
        return homeDataSource.uploadImageToStorage(uuid)
    }

    suspend fun fetchCurrentUserToken(): String {
        return homeDataSource.fetchCurrentUserToken()
    }

    fun getFirstStorageReference(homecareForm: HomecareForm): StorageReference {
        return homeDataSource.getFirstStorageReference(homecareForm)
    }

    fun getSecondStorageReference(homecareForm: HomecareForm): StorageReference {
        return homeDataSource.getSecondStorageReference(homecareForm)
    }

    //filters
    fun homecareQueryWithFilter(filter: String): Query? {
        return homeDataSource.querySelectorWithFilter(userType, municipalityName, filter)
    }
}