package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource

import com.example.crisisopp.home.models.Form
import com.example.crisisopp.user.UserTYPE
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.Query


class HomeRepository(val homeDataSource: HomeDataSource, val userType: String, val userToken: String, val municipalityName: String) {

    fun floatingActionButtonStatus(mainFAB: FloatingActionButton){
        homeDataSource.master(userType, mainFAB)
    }

    fun getUserId(): String{
        return homeDataSource.getUserId()
    }

    fun uploadForm(form: Form){
        homeDataSource.saveForm(form)
    }

    fun sendNotification(token: String, notificationTitle: String, notificationContent: String){
        homeDataSource.sendNotification(token, userType, notificationTitle, notificationContent)
    }
    fun querySelector(): Query?{
        return homeDataSource.querySelector(userType, municipalityName)

    }
}

