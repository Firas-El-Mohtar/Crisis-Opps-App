package com.example.crisisopp.LocalMunicipality

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.models.HomeCareForm
import com.example.crisisopp.home.viewmodel.HomeViewModel

class FormContentDialog: DialogFragment() {
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
    private lateinit var form: HomeCareForm

    private lateinit var approveButton: Button
    private lateinit var declineButton: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.form_content_dialog, container, false)
        homeViewModel.getHomeCareForm()?.let {
            form = it
        }
        tFormTitle = view.findViewById(R.id.form_title)
        tFullName = view.findViewById(R.id.full_name_tv)
        tMothersName = view.findViewById(R.id.mothers_name_tv)
        tBirthDate = view.findViewById(R.id.birth_date_tv)
        tBloodType = view.findViewById(R.id.blood_type_tv)
        tPlaceOfResidence = view.findViewById(R.id.place_of_residency_tv)
        tPhoneNumber = view.findViewById(R.id.phone_number_tv)
        tDateOfPrescription = view.findViewById(R.id.date_of_prescription_tv)
        tRecordNumber = view.findViewById(R.id.record_number_tv)
        tLastPcrDate = view.findViewById(R.id.last_pcr_date_tv)
        tDoctorName = view.findViewById(R.id.doctor_name_tv)
        approveButton = view.findViewById(R.id.approve_button)
        declineButton = view.findViewById(R.id.decline_button)


        tFormTitle.text= form.fullName + " Home Care Form"
        tFullName.text = "Full Name: " + form.fullName
        tMothersName.text = "Mothers Name: "+ form.mothersName
        tBirthDate.text = "Date Of Birth: "+ form.birthDate
        tBloodType.text = "Blood Type: " + form.bloodType
        tPlaceOfResidence.text = "Place Of Resendence: " + form.placeOfResidence
        tPhoneNumber.text ="Phone Number: " + form.phoneNumber
        tDateOfPrescription.text = "Date Of Prescription: " + form.dateOfPrescription
        tRecordNumber.text = "Record Number: "+  form.recordNumber.toString()
        tLastPcrDate.text = "Last PCR Date: " + form.lastPcrDate
        tDoctorName.text = "Doctors Name: " + form.doctorsName

        if(!homeViewModel.canCreateForm()){
            approveButton.visibility = VISIBLE
            declineButton.visibility = VISIBLE
        }

        approveButton.setOnClickListener {
            homeViewModel.approveForm()
        }
        declineButton.setOnClickListener {
            homeViewModel.declineForm()
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }
}