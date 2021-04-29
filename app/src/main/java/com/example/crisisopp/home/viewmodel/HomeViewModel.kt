package com.example.crisisopp.home.viewmodel

import android.content.Context
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
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class HomeViewModel(private val homeRepository: HomeRepository) : ViewModel() {
    var auth = Firebase.auth
    var query: Query? = null
    var user: User? = null
    var token: String? = null
    private lateinit var filter: String
    private val _selectedForm = MutableLiveData<Int>()
    private val _selectedAppointment = MutableLiveData<Int>()
    private val _selectedPcrForm = MutableLiveData<Int>()
    private val _selectedHomeCareForm = MutableLiveData<Int>()
    private val _selectedFilter = MutableLiveData<Int>()
    val selectedHomeCareForm: LiveData<Int>
        get() = _selectedHomeCareForm
    val selectedForm: LiveData<Int>
        get() = _selectedForm
    val selectedAppointment: LiveData<Int>
        get() = _selectedAppointment
    val selectedPcrForm: LiveData<Int>
        get() = _selectedPcrForm
    val selectedFilter: LiveData<Int>
        get() = _selectedFilter

    private lateinit var mycontext: Context
    fun setContext(context: Context){
        mycontext = context
    }
    fun getContext():Context{
        return mycontext
    }
    private lateinit var parentForm: IForm

    //User Functions

    /**
     * Fetches userId from firebase auth
     * @return userId
     */
    fun getUserId(): String {
        return homeRepository.getUserId()
    }
    fun getUserType():String{
        return homeRepository.userType
    }

    /**
     * Fetches municipality name from homeRepository
     * @return municipalityName
     */
    fun getMunicipalityName(): String {
        return homeRepository.municipalityName
    }

    /**
     * Determines whether the user is permitted to add forms
     * @return boolean
     */
    fun canCreateForm(): Boolean {
        return homeRepository.userType == "local"
    }

    /**
     * Determines whether the user is the super User Farah Foundation
     * @return boolean
     */
    fun isFarahUser(): Boolean {
        return homeRepository.userType == "farah"
    }

    /**
     * Determines whether the user is Ain W Zein
     * @return boolean
     */
    fun isAynWZein(): Boolean {
        return homeRepository.userType == "ainwzein"
    }

    /**
     * Ends the logged in user's instance and returns the user to the login activity
     */
    fun logout() {
        auth.signOut()
    }

    /**
     * Determines whether the user can add new forms
     * @return boolean
     */
    fun canApproveForm(): Boolean {
        return homeRepository.userType == "ainwzein"
    }

    /**
     * Returns the user token as a deferred nullable string
     * (returned value should be placed in a coroutine GlobalScope and paired with .await)
     * @param userId Unique userId from firbase authentication
     * @return Deferred<String?>
     */
    fun getUserParams(userId: String): Deferred<String?> {
        var tokenn: Deferred<String?> = viewModelScope.async {
            homeRepository.getUserParams(userId)
        }
        return tokenn
    }

    //Universal form/appointment functions

    fun setSelectedForm(form: IForm) {
        this.parentForm = form
        _selectedForm.value = 1
    }

    /**
     * Returns the parentForm as a homecare form
     * @return parentForm as HomecareForm
     */
    fun getHomeCareForm(): HomecareForm? {
        return parentForm as? HomecareForm
    }

    /**
     * Returns the parentForm as a pcr form
     * @return parentForm as PcrForm
     */
    fun getPcrForm(): PcrForm? {
        return parentForm as? PcrForm
    }

    /**
     * Calculates a random value between 0 and 10000 to be used as a unique form id
     * @return formId: String
     */
    fun formId(): String {
        return (0..10000).random().toString()
    }

    /**
     * Approves the selected form by changing respective user's approval in firestore database
     * sets user approval to 1
     */
    fun approveForm() {
        viewModelScope.launch {
            homeRepository.updateFormApproval(parentForm, true)
        }
    }

    /**
     * Declines the selected form by changing respective user's approval in firestore database
     * sets user approval to -1
     */
    fun declineForm() {
        viewModelScope.launch {
            homeRepository.updateFormApproval(parentForm, false)
        }
    }


    //Homecare Forms/Appointments

    /**
     * Uploads the selected homecare form to the firestore database
     * @param homecareForm
     */
    fun uploadHomeCareForm(homecareForm: HomecareForm) {
        homeRepository.uploadHomeCareForm(homecareForm)
    }

    /**
     * Modifies the "appointment" parameter of the selected homemcare form
     * adds the appointment details as DD/MM/YY Hour/Minute ex: 5/12/2021 9:30
     * @param formId
     * @param appointment
     */
    fun uploadHomecareAppointment(formId: String, appointment: String) {
        viewModelScope.launch {
            homeRepository.uploadHomecareAppointment(formId, appointment)
        }
    }

    /**
     * Selects the correct query to then send to the homecare froms adapter in order to display recyclerview items based primarily on userType
     * @return Query?
     */
    fun homecareQuerySelector(): Query? {
        query = homeRepository.querySelector()
        return query
    }

    fun setSelectedHomeCareForm(form: IForm) {
        this.parentForm = form
        _selectedHomeCareForm.value = 1
    }

    //Pcr Forms/Appointments

    /**
     * Uploads the selected pcr form to the firestore database
     * @param pcrForm
     */
    fun uploadPcrForm(pcrForm: PcrForm) {
        homeRepository.uploadPcrForm(pcrForm)
    }

    /**
     * Modifies the "appointment" parameter of the selected pcr form
     * adds the appointment details as DD/MM/YY Hour/Minute ex: 5/12/2021 9:30
     * @param formId
     * @param appointment
     */
    fun uploadPcrAppointment(formId: String, appointment: String) {
        viewModelScope.launch {
            homeRepository.uploadPcrAppointment(formId, appointment)
        }
    }

    /**
     * Selects the correct query to then send to the pcr forms adapter in order to display recyclerview items based primarily on userType
     * @return Query?
     */
    fun pcrQuerySelector(): Query? {
        return homeRepository.pcrQuerySelector()
    }

    /**
     * Modifies _selectedPcrForm livedata
     * @param form
     */
    fun setSelectedPcrForm(form: IForm) {
        this.parentForm = form
        _selectedPcrForm.value = 1
    }

    //Notification Functions

    /**
     * Send a notification to the user uploading the form to notify them thet their form has been successfully uploaded
     * @param token Current user token
     */
    fun onFormUploadSendNotification(token: String) {
        homeRepository.onFormUploadSendNotification(token)
    }

    /**
     * Sends a notification to the form sender that their form has been approved by Farah Foundation
     * @param userId Unique Id from firebase authentication
     * @param boolean Representing approval/decline (true for approved, false for declined)
     */
    fun autoSendNotification(userId: String, b: Boolean) {
        viewModelScope.async {
            homeRepository.autoSendNotification(userId, b)
        }
        //The notificationTitle and notificationContent will come from an edit text in a dialogue fragment
    }

    // Image Functions


    /**
     * Uploads the image selected by the form sender to the firebase storage
     * @param uuid Unique ImageId
     * @return StorageReference?
     */

    fun uploadImageToStorage(uuid: String): StorageReference? {
        return homeRepository.uploadImageToStorage(uuid)
    }

    /**
     * Fetches the unique storage reference of the first image uploaded with the homecare form
     * @param homecareForm
     * @return StorageReference
     */
    fun getFirstStorageReference(homecareForm: HomecareForm): StorageReference {
        return homeRepository.getFirstStorageReference(homecareForm)
    }
    /**
     * Fetches the unique storage reference of the second image uploaded with the homecare form
     * @param homecareForm
     * @return StorageReference
     */
    fun getSecondStorageReference(homecareForm: HomecareForm): StorageReference {
        return homeRepository.getSecondStorageReference(homecareForm)
    }

    //filter Functions

    /**
     * Sets the filter for the recycler view based on the user's choice
     * @param filter "Approved" / "Rejected" / "Requested" / "Done"
     */
    fun setSelectedFilter(filter: String) {
        this.filter = filter
        when(filter){
            "Approved" ->  _selectedFilter.value = 1
            "Rejected" ->  _selectedFilter.value = 2
            "Requested" ->  _selectedFilter.value = 3
            "Done" ->  _selectedFilter.value = 4
        }
    }

    /**
     * This fucntion works similarly to onNewToken where it updates the stored user token in the firebase db if it has changed
     * however this function gets called whenever the user enters the home activity to ensure that the notification service
     * will always send to the most recent token. It also returns the token for storing it in sharedPreferences.
     *
     * @return token: String?
     */

    fun refreshToken(){
        homeRepository.refreshToken()
    }
}