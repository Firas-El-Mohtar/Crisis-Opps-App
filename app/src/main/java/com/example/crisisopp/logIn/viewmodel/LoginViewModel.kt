package com.example.crisisopp.logIn.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crisisopp.extensions.emailDomain
import com.example.crisisopp.extensions.municipalityName
import com.example.crisisopp.logIn.models.LoginResult
import com.example.crisisopp.logIn.repository.LoginRepository
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import kotlinx.coroutines.launch

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel(){
    val TAG = "321"
    var userType: String? = null
    var userToken: String? = null
    var municipalityName: String? = null
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun loginWithCoroutines(email: String, password: String){
        viewModelScope.launch {
            try {
                val result = loginRepository.login(email, password)
                val limit = 0
                municipalityName = email.municipalityName

                userType = email.emailDomain
                userToken = loginRepository.fetchToken()
                loginRepository.updateUserInfo(result.userId, email.emailDomain)
                Log.d(TAG, email.emailDomain)
                loginRepository.user?.let {
                    _loginResult.postValue(LoginResult(it))
                } ?: _loginResult.postValue(LoginResult(error = Exception("Something went wrong")))

            } catch (e: Exception) {
                userType = null
                println(e)
                when (e) {
                    is FirebaseAuthInvalidCredentialsException -> {}//display our own message
                    is FirebaseAuthInvalidUserException -> {}
                    else -> {}
                }
            }
        }
    }
    suspend fun returnToken(): String{
        return loginRepository.fetchToken()
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