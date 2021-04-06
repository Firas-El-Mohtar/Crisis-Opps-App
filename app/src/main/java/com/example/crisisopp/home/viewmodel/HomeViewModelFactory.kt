package com.example.crisisopp.home.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crisisopp.home.datasource.HomeDataSource
import com.example.crisisopp.home.repository.HomeRepository

class HomeViewModelFactory(val userType: String, val municipalityName: String) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                homeRepository = HomeRepository(
                    homeDataSource = HomeDataSource(),
                    userType = userType,
                    municipalityName = municipalityName
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}