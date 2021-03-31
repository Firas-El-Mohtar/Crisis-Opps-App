package com.example.crisisopp.LocalMunicipality

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.models.Form
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.crisisopp.home.viewmodel.HomeViewModelFactory
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.File
import java.io.IOException
import java.util.*


class CreateForm : DialogFragment() {
    private var btnSubmit: Button? = null
    private var btnAttach: Button? = null
    // view for image view
    private var imageView: ImageView? = null
    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null
    // instance for firebase storage and StorageReference
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null
    var db = Firebase.firestore

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


    private val homeViewModel: HomeViewModel by activityViewModels()
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_create_form, container, false)

        //initialize views
        imageView = view.findViewById(R.id.imageView)
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
        etDoctorName = view.findViewById(R.id.doctor_name)

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference
        // on pressing btnSelect SelectImage() is called
        btnAttach!!.setOnClickListener{
            ImagePicker.with(this).start()
        }
        // on pressing btnSubmit uploadimage() is called another functions may be added later
        btnSubmit!!.setOnClickListener{
            uploadImage()
            val currentUserId = homeViewModel.getUserId()
            val formid = homeViewModel.getFormId()
            var form = Form(formid, etFullName.editText?.text.toString(), etMothersName.editText?.text.toString(), etBirthDate.editText?.text.toString(),
                etBloodType.editText?.text.toString(), etPlaceOfResidence.editText?.text.toString(), etDateOfPrescription.editText?.text.toString(),
                Integer.parseInt(etRecordNumber.editText?.text.toString()), etLastPcrDate.editText?.text.toString(),
                etPhoneNumber.editText?.text.toString(), etDoctorName.editText?.text.toString(), currentUserId)
            homeViewModel.uploadForm(form)
        }
        return view
    }


    // UploadImage method
    private fun uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            val progressDialog = ProgressDialog(this.context)
            progressDialog.setTitle("Uploading...")
            progressDialog.show()

            // Defining the child of storageReference
            val ref: StorageReference? = storageReference
                ?.child(
                    "images/"
                            + UUID.randomUUID().toString()
                )

            // adding listeners on upload
            // or failure of imag
            ref?.putFile(filePath!!)
                ?.addOnSuccessListener { // Image uploaded successfully
                    // Dismiss dialog
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            activity,
                            "Image Uploaded!!",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                ?.addOnFailureListener { e -> // Error, Image not uploaded
                    progressDialog.dismiss()
                    Toast
                        .makeText(
                            activity,
                            "Failed " + e.message,
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
                ?.addOnProgressListener { taskSnapshot ->
                    // Progress Listener for loading
                    // percentage on the dialog box
                    val progress: Double = (100.0
                            * taskSnapshot.bytesTransferred
                            / taskSnapshot.totalByteCount)
                    progressDialog.setMessage(
                        "Uploaded "
                                + progress.toInt() + "%"
                    )
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
        if (resultCode == RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            filePath = data?.data
            imageView?.setImageURI(filePath)
            imageView?.visibility = View.VISIBLE
            //You can get File object from intent
            val file: File = ImagePicker.getFile(data)!!

            //You can also get File Path from intent
            val filePath:String = ImagePicker.getFilePath(data)!!
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(context, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }


}