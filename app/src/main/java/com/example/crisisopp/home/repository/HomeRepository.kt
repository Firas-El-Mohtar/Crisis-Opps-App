package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource
import com.example.crisisopp.home.models.HomeCareForm
import com.example.crisisopp.home.models.IForm

import com.example.crisisopp.home.models.PcrForm
import com.google.firebase.firestore.Query


class HomeRepository(val homeDataSource: HomeDataSource, val userType: String, val userToken: String, val municipalityName: String) {



    fun getUserId(): String{
        return homeDataSource.getCurrentUserId()
    }

    fun uploadHomeCareForm(homeCareForm: HomeCareForm){
        homeDataSource.saveForm(homeCareForm)
    }
    fun uploadPcrForm(pcrForm: PcrForm){
        homeDataSource.savePcrForm(pcrForm)
    }
    suspend fun updateFormApproval(form: IForm, isApproved: Boolean){
        homeDataSource.updateFormApproval(userType, form, isApproved)
    }

    fun onFormUploadSendNotification(token: String){
        homeDataSource.onFormUploadSendNotification(token)
    }
    suspend fun autoSendNotification(userId: String) {

        getFormSenderToken(userId)?.let {
            homeDataSource.autoSendNotification(userType, it)
        }
    }

    fun querySelector(): Query?{
        return homeDataSource.querySelector(userType, municipalityName)

    }
    suspend fun getFormSenderToken(userId: String): String?{
        return homeDataSource.getUserToken(userId)
    }

}

