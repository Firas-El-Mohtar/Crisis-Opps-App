package com.example.crisisopp.logIn.viewmodel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crisisopp.R
import com.example.crisisopp.extensions.emailDomain
import com.example.crisisopp.extensions.municipalityName
import com.example.crisisopp.logIn.repository.LoginRepository
import com.example.crisisopp.logIn.models.LoginResult
import com.example.crisisopp.logIn.view.LoginActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    val TAG = "321"
    var userType: String? = null
    var userToken: String? = null
    var municipalityName: String? = null
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private var _loginStatus = MutableLiveData<Int>()
    var loginStaus: LiveData<Int> = _loginStatus

    /**
     * Logs the user into the app and determines usertype and token
     * @param email Account Email from EditText
     * @param password Account Password from EditText
     */

    fun loginWithCoroutines(email: String, password: String) {
        viewModelScope.launch {
            try {
                val result = loginRepository.login(email, password)
                municipalityName = email.municipalityName
                userType = email.emailDomain
                userToken = loginRepository.fetchToken()
                loginRepository.updateUserInfo(
                    result.userId,
                    email.emailDomain,
                    email.municipalityName
                )
                Log.d(TAG, email.emailDomain)
                loginRepository.user?.let {
                    _loginStatus.postValue(1)
                    _loginResult.postValue(LoginResult(it))
                } ?: _loginResult.postValue(LoginResult(error = Exception("Something went wrong")))


            } catch (e: Exception) {
                userType = null
                println(e)
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {
                        _loginStatus.postValue(-1)
                    }//display our own message
                    is FirebaseAuthInvalidUserException -> {
                        _loginStatus.postValue(-1)
                    }
                    else -> {
                        _loginStatus.postValue(-1)
                    }
                }
            }
        }
    }
    fun resetLoginStatus(){
        _loginStatus.postValue(0)
    }
}