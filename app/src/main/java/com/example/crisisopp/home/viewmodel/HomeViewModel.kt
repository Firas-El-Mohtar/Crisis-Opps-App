package com.example.crisisopp.home.viewmodel

import android.view.View
import androidx.lifecycle.ViewModel
import com.example.crisisopp.home.models.Form
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.repository.HomeRepository

import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query

class HomeViewModel(private val homeRepository: HomeRepository): ViewModel() {
    //todo var query (computed var)
    var query: Query? = null


    fun canCreateForm(): Boolean{
        return homeRepository.userType == "local"
    }
    fun getUserId(): String{
        return homeRepository.getUserId()
    }
    fun getUserToken(): String{
        return homeRepository.userToken
    }
    fun uploadForm(form: Form){
        homeRepository.uploadForm(form)
    }
    fun uploadPcrForm(pcrForm: PcrForm){
        homeRepository.uploadPcrForm(pcrForm)
    }


    fun onFormUploadSendNotification(token: String){
        homeRepository.onFormUploadSendNotification(token)
    }

    suspend fun autoSendNotification(userId: String){
        return homeRepository.autoSendNotification(userId)
        //The notificationTitle and notificationContent will come from an edit text in a dialogue fragment
    }

    fun getMunicipalityName(): String{
        return homeRepository.municipalityName
    }

    suspend fun getFormSenderToken(userId: String): String?{
        //pass the userId from the FormContentDialog that contains the query with the userId from firebase
        return homeRepository.getFormSenderToken(userId)
    }

    fun querySelector(): Query?{
        query = homeRepository.querySelector()
        return query
    }

    fun onFormClicked(){

    }
}