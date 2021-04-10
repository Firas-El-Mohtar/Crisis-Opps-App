package com.example.crisisopp.LocalMunicipality

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import java.util.*

class PcrFormContentDialog() : DialogFragment(), DatePickerDialog.OnDateSetListener,
    TimePickerDialog.OnTimeSetListener {

    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var formTitle: TextView
    private lateinit var fullName: TextView
    private lateinit var mothersName: TextView
    private lateinit var birthDate: TextView
    private lateinit var bloodType: TextView
    private lateinit var placeOfResidence: TextView
    private lateinit var dateOfInfection: TextView
    private lateinit var recordNumber: TextView
    private lateinit var phoneNumber: TextView
    private lateinit var nameOfSource: TextView
    private lateinit var additionalNotes: TextView
    private lateinit var form: PcrForm
    private lateinit var approveButton: Button
    private lateinit var declineButton: Button

    var day = 0
    var month = 0
    var year = 0
    var hour = 0
    var minute = 0
    var saveDay = 0
    var saveMonth = 0
    var saveYear = 0
    var saveHour = 0
    var saveMinute = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pcr_form_dialog, container, false)
        approveButton = view.findViewById(R.id.approve_button_pcr)
        declineButton = view.findViewById(R.id.decline_button_pcr)
        if (homeViewModel.canApproveForm()) {
            approveButton.visibility = VISIBLE
            declineButton.visibility = VISIBLE
        } else {
            approveButton.visibility = GONE
            declineButton.visibility = GONE
        }

        formTitle = view.findViewById(R.id.form_title_pcr)
        fullName = view.findViewById(R.id.full_name_tv_pcr)
        mothersName = view.findViewById(R.id.mothers_name_tv_pcr)
        birthDate = view.findViewById(R.id.birth_date_tv_pcr)
        bloodType = view.findViewById(R.id.blood_type_tv_pcr)
        placeOfResidence = view.findViewById(R.id.place_of_residency_tv_pcr)
        dateOfInfection = view.findViewById(R.id.date_of_infection_pcr)
        recordNumber = view.findViewById(R.id.record_number_tv_pcr)
        phoneNumber = view.findViewById(R.id.phone_number_tv_pcr)
        nameOfSource = view.findViewById(R.id.name_of_source_pcr)
        additionalNotes = view.findViewById(R.id.additional_notes_pcr)
        homeViewModel.getPcrForm()?.let {
            form = it
            formTitle.text = it.fullName + ": PCR Form"
            fullName.text = "Full Name: " + it.fullName
            mothersName.text = "Mothers Name: " + it.mothersName
            birthDate.text = "Date Of Birth: " + it.birthDate
            bloodType.text = "Blood Type: " + it.bloodType
            placeOfResidence.text = "Place Of Residence: " + it.placeOfResidence
            dateOfInfection.text = "Date Of Infection: " + it.dateOfInfection
            recordNumber.text = "Record Number: " + it.recordNumber
            recordNumber.text = "Record Number: " + it.recordNumber
            additionalNotes.text = "Additional Notes: " + it.additionalNotes
            phoneNumber.text = "Phone Number: " + it.phoneNumber
            nameOfSource.text = "Name Of Source: " + it.nameOfSource
        }


        approveButton.setOnClickListener() {
            homeViewModel.approveForm()

        }
        declineButton.setOnClickListener {
            homeViewModel.declineForm()
        }
        return view
    }

    private fun getDateTimeCalendar() {
        val cal: Calendar = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
        hour = cal.get(Calendar.HOUR)
        minute = cal.get(Calendar.MINUTE)

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

    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        TODO("Not yet implemented")
    }
}
