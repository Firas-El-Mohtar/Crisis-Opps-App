package com.example.crisisopp.home.viewmodel

import androidx.lifecycle.ViewModel
import com.example.crisisopp.home.models.Form
import com.example.crisisopp.home.repository.HomeRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query

class HomeViewModel(private val homeRepository: HomeRepository): ViewModel() {
    //todo var query (computed var)
    lateinit var query: Query

    fun fABStatus(mainFAB: FloatingActionButton){
        homeRepository.floatingActionButtonStatus(mainFAB)
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
    fun sendNotification(token: String, notificationTitle: String, notificationContent: String){
        homeRepository.sendNotification(token, notificationTitle, notificationContent)
    }
    fun getMunicipalityName(): String{
        return homeRepository.municipalityName
    }

    fun querySelector(): Query?{
        return homeRepository.querySelector()
    }
}