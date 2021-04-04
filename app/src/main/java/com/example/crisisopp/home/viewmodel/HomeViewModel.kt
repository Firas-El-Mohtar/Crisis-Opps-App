package com.example.crisisopp.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crisisopp.home.models.HomeCareForm
import com.example.crisisopp.home.models.IForm
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.repository.HomeRepository

import com.google.firebase.firestore.Query
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository): ViewModel() {
    //todo var query (computed var)
    var query: Query? = null

    private val _selectedForm = MutableLiveData<Int>()
    val selectedForm: LiveData<Int>
        get() = _selectedForm


    private lateinit var parentForm: IForm

    fun setDrawerInfo(userType: String, municipalityName: String){

    }

    fun setSelectedForm(form: IForm){
         this.parentForm = form
        _selectedForm.value = 1
    }
    fun getHomeCareForm(): HomeCareForm? {
      return parentForm as? HomeCareForm
    }
    fun getPcrForm(): PcrForm? {
        return parentForm as? PcrForm
    }

    fun formId(): String{
        return (0..1000).random().toString()
    }
    fun canCreateForm(): Boolean{
        return homeRepository.userType == "local"
    }
    fun getUserId(): String{
        return homeRepository.getUserId()
    }
    fun getUserToken(): String{
        return homeRepository.userToken
    }
    fun uploadHomeCareForm(homeCareForm : HomeCareForm){
        homeRepository.uploadHomeCareForm(homeCareForm)
    }

    fun uploadPcrForm(pcrForm: PcrForm){
        homeRepository.uploadPcrForm(pcrForm)
    }
    fun approveForm() {
        viewModelScope.launch {
            homeRepository.updateFormApproval(parentForm, true)
        }
    }
    fun declineForm(){
        viewModelScope.launch {
            homeRepository.updateFormApproval(parentForm, false)
        }
    }

    fun onFormUploadSendNotification(token: String){
        homeRepository.onFormUploadSendNotification(token)
    }

    fun autoSendNotification(userId: String){
        viewModelScope.launch {
            homeRepository.autoSendNotification(userId)
        }
        //The notificationTitle and notificationContent will come from an edit text in a dialogue fragment
    }

    fun getMunicipalityName(): String{
        return homeRepository.municipalityName
    }

    suspend fun getFormSenderToken(userId: String): String?{
        //pass the userId from the FormContentDialog that contains the query with the userId from firebase
        return homeRepository.getFormSenderToken(userId)
    }

    fun querySelector(): Query?{
        query = homeRepository.querySelector()
        return query
    }
    fun pcrQuerySelector(): Query?{
        return homeRepository.pcrQuerySelector()
    }

}