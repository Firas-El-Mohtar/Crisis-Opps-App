package com.example.crisisopp.logIn.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.crisisopp.logIn.datasource.LoginDataSource
import com.example.crisisopp.logIn.repository.LoginRepository


/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(val sharedPref: SharedPreferences) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginRepository = LoginRepository(
                        dataSource = LoginDataSource(),
                        sharedPref = sharedPref
                    )
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}