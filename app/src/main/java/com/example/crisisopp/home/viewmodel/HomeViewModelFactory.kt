package com.example.crisisopp.home.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crisisopp.home.datasource.HomeDataSource
import com.example.crisisopp.home.repository.HomeRepository
import com.example.crisisopp.logIn.datasource.LoginDataSource
import com.example.crisisopp.logIn.repository.LoginRepository
import com.example.crisisopp.logIn.viewmodel.LoginViewModel

class HomeViewModelFactory(val userType: String) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(
                homeRepository = HomeRepository(
                    homeDataSource = HomeDataSource(),
                    userType = userType
                )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}