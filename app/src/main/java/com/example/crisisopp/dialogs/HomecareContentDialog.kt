package com.example.crisisopp.dialogs

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.glide.GlideApp
import com.example.crisisopp.home.viewmodel.HomeViewModel
import java.util.*

/**
 * This dialog is presented to the user upon clicking a homecare form item in the recycler view entailed in HomeActivity
 */

class HomecareContentDialog : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var tFormTitle: TextView
    private lateinit var tFullName: TextView
    private lateinit var tMothersName: TextView
    private lateinit var tBirthDate: TextView
    private lateinit var tBloodType: TextView
    private lateinit var tPlaceOfResidence: TextView
    private lateinit var tPhoneNumber: TextView
    private lateinit var tDateOfPrescription: TextView
    private lateinit var tRecordNumber: TextView
    private lateinit var tLastPcrDate: TextView
    private lateinit var tDoctorName: TextView
    private lateinit var firstImage: ImageView
    private lateinit var secondImage: ImageView
    private lateinit var tMunicipalityName: TextView
    private lateinit var approveButton: Button
    private lateinit var declineButton: Button
    private lateinit var tAppointmentDate: TextView
    private var day = 0
    private var month: Int = 0
    private var year: Int = 0
    private var hour: Int = 0
    private var minute: Int = 0
    private var myDay = 0
    private var myMonth: Int = 0
    private var myYear: Int = 0
    private var myHour: Int = 0
    private var myMinute: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.form_content_dialog, container, false)
        tFormTitle = view.findViewById(R.id.form_title_pcr)
        tFullName = view.findViewById(R.id.full_name_tv_hc_placeholder)
        tMothersName = view.findViewById(R.id.mothers_name_tv_hc_placeholder)
        tMunicipalityName = view.findViewById(R.id.municipality_name_hc_placeholder)
        tBirthDate = view.findViewById(R.id.birth_date_tv_hc_placeholder)
        tBloodType = view.findViewById(R.id.blood_type_tv_hc_placeholder)
        tPlaceOfResidence = view.findViewById(R.id.place_of_residency_tv_hc_placeholder)
        tPhoneNumber = view.findViewById(R.id.phone_number_tv_hc_placeholder)
        tDateOfPrescription = view.findViewById(R.id.date_of_prescription_tv_hc_placeholder)
        tRecordNumber = view.findViewById(R.id.record_number_tv_hc_placeholder)
        tLastPcrDate = view.findViewById(R.id.last_pcr_date_tv_hc_placeholder)
        tDoctorName = view.findViewById(R.id.doctor_name_tv_hc_placeholder)
        approveButton = view.findViewById(R.id.approve_button)
        declineButton = view.findViewById(R.id.decline_button)
        tAppointmentDate = view.findViewById(R.id.appointment_date_homecare)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home_care_content)
        firstImage = view.findViewById(R.id.doctors_prescription_iv)
        secondImage = view.findViewById(R.id.hawiyye_scan_iv)

        homeViewModel.getHomeCareForm()?.let {
            tFullName.text = it.fullName
            tMothersName.text = it.mothersName
            tMunicipalityName.text = it.municipalityName
            tBirthDate.text = it.birthDate
            tBloodType.text = it.bloodType
            tPlaceOfResidence.text = it.placeOfResidence
            tPhoneNumber.text = it.phoneNumber
            tDateOfPrescription.text = it.dateOfPrescription
            tRecordNumber.text = it.recordNumber.toString()
            tLastPcrDate.text = it.lastPcrDate
            tDoctorName.text = it.doctorsName
            tMunicipalityName.text = it.municipalityName
            if(it.appointment != "") {
                tAppointmentDate.text = it.appointment
            }

            progressBar.visibility = VISIBLE
            GlideApp.with(this).load(homeViewModel.getFirstStorageReference(it))
                .into(firstImage)
            GlideApp.with(this).load(homeViewModel.getSecondStorageReference(it))
                .into(secondImage)
            progressBar.visibility = GONE

        }

        if(homeViewModel.getUserType() == "main"){
            val form = homeViewModel.getHomeCareForm()
            if(form?.mainApproval == 1 || form?.mainApproval == -1){
                approveButton.isEnabled = false
                declineButton.isEnabled = false
            }
        }
        if(homeViewModel.getUserType() == "ainwzein"){
            val form = homeViewModel.getHomeCareForm()
            if(form?.ainWzeinApproval == 1 || form?.ainWzeinApproval == -1){
                approveButton.isEnabled = false
                declineButton.isEnabled = false
            }
        }
        if(homeViewModel.getUserType() == "farah"){
            val form = homeViewModel.getHomeCareForm()
            if(form?.farahApproval == 1 || form?.farahApproval == -1){
                approveButton.isEnabled = false
                declineButton.isEnabled = false
            }
        }


        if (!homeViewModel.canCreateForm()) {
            approveButton.visibility = VISIBLE
            declineButton.visibility = VISIBLE
        }
        approveButton.setOnClickListener {
            if (homeViewModel.isFarahUser()) {
                progressBar.visibility = VISIBLE
                homeViewModel.approveForm()
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(requireContext(), this, year, month, day)
                datePickerDialog.show()

            } else {

                progressBar.visibility = VISIBLE
                homeViewModel.approveForm()
                if(homeViewModel.isFarahUser()) {
                    homeViewModel.autoSendNotification(
                        homeViewModel.getHomeCareForm()?.originatorId!!,
                        true
                    )
                }
            }
            progressBar.visibility = GONE
        }
        declineButton.setOnClickListener {

            progressBar.visibility = VISIBLE
            homeViewModel.declineForm()
            if(homeViewModel.isFarahUser()) {
                homeViewModel.autoSendNotification(
                    homeViewModel.getHomeCareForm()?.originatorId!!,
                    false
                )
            }
            progressBar.visibility = GONE
        }
        return view
    }

    override fun onStart() {

        super.onStart()
        dialog?.let {

            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        if (minute <= 9) {
            myMinute = ":0" + minute
        }
        val timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        var appointment: String? = null
        appointment = if (minute <= 9){
            "" + myDay + "\\" + myMonth + "\\" + myYear + " " + myHour + ":0" + minute
        } else "" + myDay + "\\" + myMonth + "\\" + myYear + " " + myHour + ":" + minute

        tAppointmentDate.text = appointment
        homeViewModel.autoSendNotification(homeViewModel.getHomeCareForm()?.originatorId!!, true)
        homeViewModel.uploadHomecareAppointment(homeViewModel.getHomeCareForm()?.formID!!, appointment)
    }
}

