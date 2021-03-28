package com.example.crisisopp.logIn.viewmodel

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crisisopp.extensions.emailDomain
import com.example.crisisopp.home.view.LocalMunicipalityMainActivity
import com.example.crisisopp.logIn.models.LoginResult
import com.example.crisisopp.logIn.repository.LoginRepository
import com.example.crisisopp.logIn.view.LoginActivity
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(){
    val TAG = "321"
    val userType: MutableLiveData<String> by lazy {
        MutableLiveData<String>()}
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginWithCoroutines(email: String, password: String){
        viewModelScope.launch {
            try {
                val result = loginRepository.login(email, password)
                userType.setValue(email.emailDomain)
                loginRepository.fetchToken()
                loginRepository.updateUserInfo(result.userId, email.emailDomain)
                Log.d(TAG, email.emailDomain)
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

//    fun updateUiWithUser(activity: LoginActivity) {
//        loginRepository.
//        val intent = Intent(activity, LocalMunicipalityMainActivity::class.java)
//        intent.putExtra("usertype", userType)
//        startActivity(intent)
//    }

//    fun updateLiveData(string: String){
//        userType.value(string)
//    }

}