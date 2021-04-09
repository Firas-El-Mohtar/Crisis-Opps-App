package com.example.crisisopp.LocalMunicipality

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.google.android.material.textfield.TextInputLayout

class CreatePcrForm : DialogFragment() {

    private lateinit var btnSubmit: Button
    private lateinit var etFullName: TextInputLayout
    private lateinit var etMothersName: TextInputLayout
    private lateinit var etBirthDate: TextInputLayout
    private lateinit var etPlaceOfResidence: TextInputLayout
    private lateinit var etPhoneNumber: TextInputLayout
    private lateinit var etRecordNumber: TextInputLayout
    private lateinit var etNameOfSource: TextInputLayout
    private lateinit var etDateOfInfection: TextInputLayout
    private lateinit var etBloodType: TextInputLayout
    private lateinit var etAdditionalNotes: TextInputLayout

    private val homeViewModel: HomeViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_pcr_form, container, false)

        btnSubmit = view.findViewById(R.id.submit)
        //Edit Text References (initializing)
        etFullName = view.findViewById(R.id.full_name)
        etMothersName = view.findViewById(R.id.mothers_name)
        etBirthDate = view.findViewById(R.id.birth_date)
        etPlaceOfResidence = view.findViewById(R.id.place_of_residency)
        etPhoneNumber = view.findViewById(R.id.phone_number)
        etRecordNumber = view.findViewById(R.id.record_number)
        etNameOfSource = view.findViewById(R.id.name_of_source)
        etDateOfInfection = view.findViewById(R.id.date_of_infection)
        etBloodType = view.findViewById(R.id.blood_type)
        etAdditionalNotes = view.findViewById(R.id.additional_notes)


        btnSubmit!!.setOnClickListener {

            val currentUserId = homeViewModel.getUserId()

            val currentUserToken = homeViewModel.getUserId()
            var pcrForm = PcrForm(
                municipalityName = homeViewModel.getMunicipalityName(),
                formID = homeViewModel.formId(),
                fullName = etFullName.editText?.text.toString(),
                mothersName = etMothersName.editText?.text.toString(),
                birthDate = etBirthDate.editText?.text.toString(),
                bloodType = etBloodType.editText?.text.toString(),
                placeOfResidence = etPlaceOfResidence.editText?.text.toString(),
                dateOfInfection = etDateOfInfection.editText?.text.toString(),
                recordNumber = Integer.parseInt(etRecordNumber.editText?.text.toString()),
                nameOfSource = etNameOfSource.editText?.text.toString(),
                phoneNumber = etPhoneNumber.editText?.text.toString(),
                originatorId = currentUserId,
                ainWzeinApproval = 0,
                additionalNotes = etAdditionalNotes.editText?.text.toString(),
                formType = "PCR"
            )

            homeViewModel.uploadPcrForm(pcrForm)
            homeViewModel.onFormUploadSendNotification(currentUserToken)
            dialog?.dismiss()

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
