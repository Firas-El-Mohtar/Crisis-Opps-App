package com.example.crisisopp.home.viewmodel

import androidx.lifecycle.ViewModel
import com.example.crisisopp.home.models.Form
import com.example.crisisopp.home.repository.HomeRepository
import com.google.android.material.floatingactionbutton.FloatingActionButton

class HomeViewModel(private val homeRepository: HomeRepository): ViewModel() {
    //todo var query (computed var)

    fun fABStatus(mainFAB: FloatingActionButton){
        homeRepository.floatingActionButtonStatus(mainFAB)
    }
    fun getUserId(): String{
        return homeRepository.getUserId()
    }
    fun uploadForm(form: Form){
        homeRepository.uploadForm(form)
    }
}