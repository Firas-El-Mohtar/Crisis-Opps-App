package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource

import com.example.crisisopp.home.models.Form
import com.example.crisisopp.home.models.PcrForm
import com.google.firebase.firestore.Query


class HomeRepository(val homeDataSource: HomeDataSource, val userType: String, val userToken: String, val municipalityName: String) {

    fun getUserId(): String{
        return homeDataSource.getCurrentUserId()
    }

    fun uploadForm(form: Form){
        homeDataSource.saveForm(form)
    }
    fun uploadPcrForm(pcrForm: PcrForm){
        homeDataSource.savePcrForm(pcrForm)
    }


    fun onFormUploadSendNotification(token: String){
        homeDataSource.onFormUploadSendNotification(token)
    }
    suspend fun autoSendNotification(userId: String) {

        return homeDataSource.autoSendNotification(userType, getFormSenderToken(userId)!!)
    }

    fun querySelector(): Query?{
        return homeDataSource.querySelector(userType, municipalityName)

    }
    suspend fun getFormSenderToken(userId: String): String?{
        return homeDataSource.getUserToken(userId)
    }

    fun approveFrom(userType: String, formId: String){
        homeDataSource.approveForm(formId, userType)
    }
}

