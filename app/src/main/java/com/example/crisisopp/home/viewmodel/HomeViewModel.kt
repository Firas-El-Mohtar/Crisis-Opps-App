package com.example.crisisopp.home.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.crisisopp.home.models.HomecareForm
import com.example.crisisopp.home.models.IForm
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.models.*
import com.example.crisisopp.home.repository.HomeRepository
import com.example.crisisopp.logIn.models.User
import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    //todo var query (computed var)
    var auth = Firebase.auth
    var query: Query? = null
    var user: User? = null
    var token: String? = null
    private val _selectedForm = MutableLiveData<Int>()
    private val _selectedAppointment = MutableLiveData<Int>()
    private val _selectedPcrForm = MutableLiveData<Int>()
    private val _selectedHomeCareForm = MutableLiveData<Int>()
    val selectedHomeCareForm: LiveData<Int>
        get() = _selectedHomeCareForm
    val selectedForm: LiveData<Int>
        get() = _selectedForm
    val selectedAppointment: LiveData<Int>
        get() = _selectedAppointment
    val selectedPcrForm: LiveData<Int>
        get() = _selectedPcrForm

    private lateinit var parentForm: IForm
    private lateinit var parentAppointment: IAppointment

    //User Functions

    fun getUserId(): String {
        return homeRepository.getUserId()
    }

    fun getMunicipalityName(): String {
        return homeRepository.municipalityName
    }

    fun canCreateForm(): Boolean {
        return homeRepository.userType == "local"
    }

    fun isFarahUser(): Boolean {
        return homeRepository.userType == "farah"
    }

    fun canApproveForm(): Boolean {
        return homeRepository.userType == "ainwzein"
    }

    //Universal form/appointment functions

    fun setSelectedForm(form: IForm) {
        this.parentForm = form
        _selectedForm.value = 1
    }

    fun getHomeCareForm(): HomecareForm? {
        return parentForm as? HomecareForm
    }

    fun getPcrForm(): PcrForm? {
        return parentForm as? PcrForm
    }

    fun formId(): String {
        return (0..1000).random().toString()
    }

    fun deleteAppointment(appointment: IAppointment) {
        viewModelScope.launch {
            homeRepository.deleteAppointment(appointment)
        }
    }

    fun approveForm() {
        viewModelScope.launch {
            homeRepository.updateFormApproval(parentForm, true)
        }
    }

    fun declineForm() {
        viewModelScope.launch {
            homeRepository.updateFormApproval(parentForm, false)
        }
    }

    fun getFormSenderToken(userId: String): String? {
        //pass the userId from the FormContentDialog that contains the query with the userId from firebase
        viewModelScope.launch {
            token = homeRepository.getFormSenderToken(userId)
        }
        return token
    }

    //Homecare Forms/Appointments

    fun uploadHomeCareForm(homecareForm: HomecareForm) {
        homeRepository.uploadHomeCareForm(homecareForm)
    }

    fun uploadHomecareAppointment(appointment: HomecareAppointment) {
        homeRepository.uploadHomecareAppointment(appointment)
    }

    fun homecareQuerySelector(): Query? {
        query = homeRepository.querySelector()
        return query
    }

    fun setSelectedHomeCareForm(form: IForm) {
        this.parentForm = form
        _selectedHomeCareForm.value = 1
    }

    fun getStorageReference(homecareForm: HomecareForm): StorageReference {
        return homeRepository.getStorageReference(homecareForm)
    }

    //Pcr Forms/Appointments

    fun uploadPcrForm(pcrForm: PcrForm) {
        homeRepository.uploadPcrForm(pcrForm)
    }

    fun uploadPcrAppointment(appointment: PcrAppointment) {
        homeRepository.uploadPcrAppointment(appointment)
    }

    fun pcrQuerySelector(): Query? {
        return homeRepository.pcrQuerySelector()
    }

    fun setSelectedPcrForm(form: IForm) {
        this.parentForm = form
        _selectedPcrForm.value = 1
    }

    //Notification Functions

    fun onFormUploadSendNotification(token: String) {
        homeRepository.onFormUploadSendNotification(token)
    }

    fun autoSendNotification(userId: String, b: Boolean) {
        viewModelScope.launch {
            homeRepository.autoSendNotification(userId, b)
        }
        //The notificationTitle and notificationContent will come from an edit text in a dialogue fragment
    }

    fun setSelectedAppointmentType(appointment: IAppointment) {
        this.parentAppointment = appointment
        _selectedAppointment.value = 1
    }

    fun pcrAppointmentQuerySelector(): Query? {
        return homeRepository.pcrAppointmentQuerySelector()
    }

    fun homecareAppointmentQuerySelector(): Query? {
        return homeRepository.homecareAppointmentQuerySelector()
    }

    fun uploadImageToStorage(uuid: String): StorageReference? {
        return homeRepository.uploadImageToStorage(uuid)
    }

    fun getUserParams(userId: String): String? {
        viewModelScope.launch {
            token = homeRepository.getUserParams(userId)
        }
        return token
    }

    fun logout() {
        auth.signOut()
    }

    fun getHomecareAppointment(): HomecareAppointment? {
        return parentAppointment as? HomecareAppointment
    }

    fun getPcrAppointment(): PcrAppointment? {
        return parentAppointment as? PcrAppointment
    }
}