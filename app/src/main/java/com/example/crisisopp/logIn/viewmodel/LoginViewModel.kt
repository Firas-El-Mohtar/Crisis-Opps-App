package com.example.crisisopp.logIn.viewmodel

import android.content.Intent
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crisisopp.FarahFoundation.FarahFoundationMainActivity
import com.example.crisisopp.LocalMunicipality.LocalMunicipalityMainActivity
import com.example.crisisopp.MainMunicipality.MainMunicipalityMainActivity
import com.example.crisisopp.R
import com.example.crisisopp.extensions.emailDomain
import com.example.crisisopp.logIn.models.LoginResult
import com.example.crisisopp.logIn.models.User
import com.example.crisisopp.logIn.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(){
    val TAG = ""
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginWithCoroutines(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = loginRepository.login(email, password)
                loginRepository.fetchToken()
                loginRepository.updateUserInfo(result.userId, email.emailDomain)
                loginRepository.user?.let {
                    _loginResult.postValue(LoginResult(it))
                } ?: _loginResult.postValue(LoginResult(error = Exception("Something went wrong")))

            } catch (e: Exception) {
                println(e)
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {}//display our own message
                    is FirebaseAuthInvalidUserException -> {}
                    else -> {}
                }
            }
        }
    }
//
//    fun checkAccountType(email: String) {
//        var ref = email.drop(5)
//        if (ref.contains("@main")){
//    }
}