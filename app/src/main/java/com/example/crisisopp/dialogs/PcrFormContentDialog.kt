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
import com.example.crisisopp.home.models.PcrForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import java.util.*

/**
 * This dialog is presented to the user upon clicking a pcr form item in the recycler view entailed in HomeActivity
 */

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
    private var myMinute: Int = 0


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
        fullName = view.findViewById(R.id.full_name_pcr_placeholder)
        mothersName = view.findViewById(R.id.mothers_name__pcr_placeholder)
        birthDate = view.findViewById(R.id.birth_date_pcr_placeholder)
        bloodType = view.findViewById(R.id.blood_type_pcr_placeholder)
        placeOfResidence = view.findViewById(R.id.place_of_residency_pcr_placeholder)
        dateOfInfection = view.findViewById(R.id.date_of_infection_pcr_placeholder)
        recordNumber = view.findViewById(R.id.record_number_pcr_placeholder)
        phoneNumber = view.findViewById(R.id.phone_number_pcr_placeholder)
        nameOfSource = view.findViewById(R.id.name_of_source_pcr_placeholder)
        additionalNotes = view.findViewById(R.id.additional_notes_pcr_placeholder)
        tAppointmentDate = view.findViewById(R.id.appointment_date_pcr)
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_pcr_content)
        homeViewModel.getPcrForm()?.let {
            form = it
            formTitle.text = "PCR Form"
            fullName.text = it.fullName
            mothersName.text = it.mothersName
            birthDate.text = it.birthDate
            bloodType.text = it.bloodType
            placeOfResidence.text = it.placeOfResidence
            dateOfInfection.text = it.dateOfInfection
            recordNumber.text = it.recordNumber.toString()
            additionalNotes.text = it.additionalNotes
            phoneNumber.text = it.phoneNumber
            nameOfSource.text =it.nameOfSource
            tAppointmentDate.text = "Appointment: " + it.appointment
        }

        approveButton.setOnClickListener() {
            if (homeViewModel.isAynWZein()) {
                progressBar.visibility = VISIBLE
                homeViewModel.approveForm()
                val calendar: Calendar = Calendar.getInstance()
                day = calendar.get(Calendar.DAY_OF_MONTH)
                month = calendar.get(Calendar.MONTH)
                year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(requireContext(), this, year, month, day)
                datePickerDialog.show()
                homeViewModel.autoSendNotification(homeViewModel.getPcrForm()?.originatorId!!, true)
            } else {

                progressBar.visibility = VISIBLE
                homeViewModel.approveForm()
                homeViewModel.autoSendNotification(homeViewModel.getPcrForm()?.originatorId!!, true)
                progressBar.visibility = GONE
            }
        }
        declineButton.setOnClickListener {
            progressBar.visibility = VISIBLE
            homeViewModel.declineForm()
            homeViewModel.autoSendNotification(form.originatorId, false)
            progressBar.visibility = GONE
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
        myDay = day
        myYear = year
        myMonth = month
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        myHour = hourOfDay
        myMinute = minute
        var appointment : String?
        appointment = if (minute <= 9){
            "" + myDay + "\\" + myMonth + "\\" + myYear + " " + myHour + ":0" + minute
        } else "" + myDay + "\\" + myMonth + "\\" + myYear + " " + myHour + ":" + minute
        tAppointmentDate.text = "Appointment: " + appointment
        //"Year: " + myYear + "\\" + "Month: " + myMonth + "\\" + "Day: " + myDay + "\\" + "Hour: " + myHour + "\\" + "Minute: " + myMinute
        homeViewModel.uploadPcrAppointment(homeViewModel.getPcrForm()?.formID!!, appointment)
    }
}
