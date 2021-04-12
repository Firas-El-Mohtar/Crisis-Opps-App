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
import com.example.crisisopp.home.models.HomecareForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import java.util.*


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

        val view = inflater.inflate(R.layout.form_content_dialog, container, false)
        tFormTitle = view.findViewById(R.id.form_title)
        tFullName = view.findViewById(R.id.full_name_tv)
        tMothersName = view.findViewById(R.id.mothers_name_tv)
        tMunicipalityName = view.findViewById(R.id.municipality_name)
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
        val progressBar = view.findViewById<ProgressBar>(R.id.progress_bar_home_care_content)
        firstImage = view.findViewById(R.id.doctors_prescription_iv)
        secondImage = view.findViewById(R.id.hawiyye_scan_iv)

        homeViewModel.getHomeCareForm()?.let {
            tFormTitle.text = it.fullName + " Home Care Form"
            tFullName.text = "Full Name: " + it.fullName
            tMothersName.text = "Mothers Name: " + it.mothersName
            tBirthDate.text = "Date Of Birth: " + it.birthDate
            tBloodType.text = "Blood Type: " + it.bloodType
            tPlaceOfResidence.text = "Place Of Resendence: " + it.placeOfResidence
            tPhoneNumber.text = "Phone Number: " + it.phoneNumber
            tDateOfPrescription.text = "Date Of Prescription: " + it.dateOfPrescription
            tRecordNumber.text = "Record Number: " + it.recordNumber.toString()
            tLastPcrDate.text = "Last PCR Date: " + it.lastPcrDate
            tDoctorName.text = "Doctors Name: " + it.doctorsName
            progressBar.visibility = VISIBLE
            GlideApp.with(this).load(homeViewModel.getFirstStorageReference(it))
                .into(firstImage)
            GlideApp.with(this).load(homeViewModel.getSecondStorageReference(it))
                .into(secondImage)
            progressBar.visibility = GONE
        }


        if (!homeViewModel.canCreateForm()) {

            approveButton.visibility = VISIBLE
            declineButton.visibility = VISIBLE
        }
        approveButton.setOnClickListener {

            if (homeViewModel.isFarahUser() || homeViewModel.isAynWZein()) {
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
            }
            progressBar.visibility = GONE
        }
        declineButton.setOnClickListener {

            progressBar.visibility = VISIBLE
            homeViewModel.declineForm()
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
        val timePickerDialog = TimePickerDialog(requireContext(), this, hour, minute, true)
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        tLastPcrDate.text =
            "Year: " + myYear + "\\" + "Month: " + myMonth + "\\" + "Day: " + myDay + "\\" + "Hour: " + myHour + "\\" + "Minute: " + myMinute
    }
}

