package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource
import com.example.crisisopp.home.models.HomeCareForm
import com.example.crisisopp.home.models.IForm

import com.example.crisisopp.home.models.PcrForm
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class HomeRepository(val homeDataSource: HomeDataSource, val userType: String, val userToken: String, val municipalityName: String) {



    fun getStorageReference(homeCareForm: HomeCareForm): StorageReference{
        return homeDataSource.getStorageReference(homeCareForm)
    }
    fun uploadImageToStorage(uuid: String): StorageReference?{
        return homeDataSource.uploadImageToStorage(uuid)
    }
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
    fun pcrQuerySelector(): Query?{
        return homeDataSource.pcrQuerySelector(userType, municipalityName)
    }

}

