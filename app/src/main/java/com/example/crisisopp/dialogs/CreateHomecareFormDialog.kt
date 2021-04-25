package com.example.crisisopp.dialogs

import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.models.HomecareForm
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

/**
 *This dialog is presented to the user upon clicking the add homecare form floating action button in HomeActivity
 */

class CreateHomecareFormDialog : DialogFragment() {
    private var btnSubmit: Button? = null
    private var btnAttach: Button? = null
    private var documentReference: String? = null
    // view for image view
    private var firstImageView: ImageView? = null
    private var secondImageView: ImageView? = null
    // Uri indicates, where the image will be picked from
    private var filePathOne: Uri? = null
    private var filePathTwo: Uri? = null
    // instance for firebase storage and StorageReference
//    var storage: FirebaseStorage? = null
//    var storageReference: StorageReference? = null
//    var db = Firebase.firestore
    var firstImageID: String? = null
    var secondImageID: String? = null
    //Edit Text References
    private lateinit var etFullName: TextInputLayout
    private lateinit var etMothersName: TextInputLayout
    private lateinit var etBirthDate: TextInputLayout
    private lateinit var etBloodType: TextInputLayout
    private lateinit var etPlaceOfResidence: TextInputLayout
    private lateinit var etPhoneNumber: TextInputLayout
    private lateinit var etDateOfPrescription: TextInputLayout
    private lateinit var etRecordNumber: TextInputLayout
    private lateinit var etLastPcrDate: TextInputLayout
    private lateinit var etDoctorName: TextInputLayout
    private lateinit var progressBar: ProgressBar
    private var counter = 1
    private val homeViewModel: HomeViewModel by activityViewModels()

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_create_homecare_form, container, false)
        //initialize views
        firstImageView = view.findViewById(R.id.imageView)
        secondImageView = view.findViewById(R.id.imageView_2)
        btnAttach = view.findViewById(R.id.attach_image)
        btnSubmit = view.findViewById(R.id.submit)
        //Edit Text References (initializing)
        etFullName = view.findViewById(R.id.full_name)
        etMothersName = view.findViewById(R.id.mothers_name)
        etBirthDate = view.findViewById(R.id.birth_date)
        etBloodType = view.findViewById(R.id.blood_type)
        etPlaceOfResidence = view.findViewById(R.id.place_of_residency)
        etPhoneNumber = view.findViewById(R.id.phone_number)
        etDateOfPrescription = view.findViewById(R.id.date_of_prescription)
        etRecordNumber = view.findViewById(R.id.record_number)
        etLastPcrDate = view.findViewById(R.id.pcr_date)
        etDoctorName = view.findViewById(R.id.doctors_name)
        progressBar = view.findViewById(R.id.progress_bar_create_home_care_form)
        // get the Firebase  storage reference
//        storage = FirebaseStorage.getInstance()
//        storageReference = storage!!.reference
        // on pressing btnSelect SelectImage() is called
        btnAttach!!.setOnClickListener {
            ImagePicker.with(this).compress(1024).start()
            when (counter) {
                1 -> {
                    firstImageID = UUID.randomUUID().toString()
                    counter++
                }
                2 -> {
                    secondImageID = UUID.randomUUID().toString()
                    counter--
                }
            }
        }
// on pressing btnSubmit uploadimage() is called another functions may be added later
        btnSubmit!!.setOnClickListener {
            uploadImage()
            val currentUserId = homeViewModel.getUserId()
            val formId = (0..1000).random().toString()
            val currentUserToken = homeViewModel.getUserParams(currentUserId)
            //constructor to build a Form object to then pass to firebase for saving
            var form = HomecareForm(
                formID = formId,
                fullName = etFullName.editText?.text.toString(),
                mothersName = etMothersName.editText?.text.toString(),
                birthDate = etBirthDate.editText?.text.toString(),
                bloodType = etBloodType.editText?.text.toString(),
                placeOfResidence = etPlaceOfResidence.editText?.text.toString(),
                dateOfPrescription = etDateOfPrescription.editText?.text.toString(),
                recordNumber = Integer.parseInt(etRecordNumber.editText?.text.toString()),
                lastPcrDate = etLastPcrDate.editText?.text.toString(),
                phoneNumber = etPhoneNumber.editText?.text.toString(),
                doctorsName = etDoctorName.editText?.text.toString(),
                originatorId = currentUserId,
                firstDocumentReference = firstImageID,
                secondDocumentReference = secondImageID,
                farahApproval = 0,
                mainApproval = 0,
                ainWzeinApproval = 0,
                municipalityName = homeViewModel.getMunicipalityName(),
                formType = "Homecare",
            )
            homeViewModel.uploadHomeCareForm(form)
            GlobalScope.launch {
                homeViewModel.onFormUploadSendNotification(currentUserToken.await()!!)
            }
            dialog?.dismiss()
        }

        return view
    }
    private fun uploadImage() {
        if (filePathOne != null) {
            progressBar.visibility = VISIBLE
// Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this.context)
//            progressDialog.setTitle("Uploading...")
//            progressDialog.show()
            firstImageID?.let {
                val ref = homeViewModel.uploadImageToStorage(it)
                ref?.putFile(filePathOne!!)
                    ?.addOnSuccessListener {// Image uploaded successfully
                        // Dismiss dialog
                        if (filePathTwo != null) {
                            val progressDialog = ProgressDialog(this.context)
                            progressDialog.setTitle("Uploading...")
                            progressDialog.show()
                            secondImageID?.let {
                                val ref = homeViewModel.uploadImageToStorage(it)
                                ref?.putFile(filePathTwo!!)
                                    ?.addOnSuccessListener {// Image uploaded successfully
                                        // Dismiss dialog
                                        progressBar.visibility = GONE
                                        progressDialog.dismiss()
                                        Toast.makeText(
                                            activity,
                                            "Image Uploaded!!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        dialog?.dismiss()
                                    }
                                    ?.addOnFailureListener { e -> // Error, Image not uploaded
                                        progressDialog.dismiss()
                                        Toast.makeText(
                                            activity,
                                            "Failed " + e.message,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                        dialog?.dismiss()
                                    }
                                    ?.addOnProgressListener { taskSnapshot ->
// Progress Listener for loading
//                                         percentage on the dialog box
                                        val progress: Double =
                                            (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                                    }
                            }
                        }
                        progressDialog.dismiss()
                        Toast.makeText(activity, "Image Uploaded!!", Toast.LENGTH_SHORT).show()
                    }
                    ?.addOnFailureListener { e -> // Error, Image not uploaded
                        progressDialog.dismiss()
                        Toast.makeText(activity, "Failed " + e.message, Toast.LENGTH_SHORT).show()
                        dialog?.dismiss()
                    }
                    ?.addOnProgressListener { taskSnapshot ->
// Progress Listener for loading
                        // percentage on the dialog box
                        val progress: Double =
                            (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)
                        progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                    }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            it.window?.setLayout(width, height)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && counter == 1) {
            //Image Uri will not be null for RESULT_OK
            filePathOne = data?.data
            firstImageView?.setImageURI(filePathOne)
            firstImageView?.visibility = View.VISIBLE
//You can get File object from intent
//            val file: File = ImagePicker.getFile(data)!!
//            //You can also get File Path from intent
//            val filePath: String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == RESULT_OK && counter == 2) {
            filePathTwo = data?.data
            secondImageView?.setImageURI(filePathTwo)
            secondImageView?.visibility = View.VISIBLE
//You can get File object from intent
//            val file: File = ImagePicker.getFile(data)!!
//            //You can also get File Path from intent
//            val filePath: String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}