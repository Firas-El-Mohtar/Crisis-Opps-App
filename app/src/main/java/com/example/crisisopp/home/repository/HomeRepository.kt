package com.example.crisisopp.home.repository

import com.example.crisisopp.home.datasource.HomeDataSource

import com.example.crisisopp.home.models.Form
import com.example.crisisopp.user.UserTYPE
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeRepository(val homeDataSource: HomeDataSource, val userType: String) {

    fun floatingActionButtonStatus(mainFAB: FloatingActionButton){
        homeDataSource.master(userType, mainFAB)
    }

    fun getUserId(): String{
        return homeDataSource.getUserId()
    }
    fun uploadForm(form: Form){
        homeDataSource.saveForm(form)
    }
    fun getFormId():String{
        return homeDataSource.getFormId()
    }
}

