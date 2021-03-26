package com.example.crisisopp.LocalMunicipality

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.crisisopp.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException
import android.content.Context
import android.widget.Toast
import androidx.annotation.NonNull
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.storage.OnProgressListener
import com.google.firebase.storage.UploadTask
import java.lang.Exception
import java.util.*

class CreateForm : DialogFragment() {
    private var btnSubmit: Button? = null
    private var btnAttach: Button? = null

    // view for image view
    private var imageView: ImageView? = null

    // Uri indicates, where the image will be picked from
    private var filePath: Uri? = null

    // request code
    private val PICK_IMAGE_REQUEST = 22

    // instance for firebase storage and StorageReference
    var storage: FirebaseStorage? = null
    var storageReference: StorageReference? = null




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

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance()
        storageReference = storage!!.reference


        // on pressing btnSelect SelectImage() is called
        btnAttach!!.setOnClickListener{attachImage()}

        // on pressing btnSubmit uploadimage() is called another functions may be added later
        btnSubmit!!.setOnClickListener{uploadImage()}
        return view
    }

    private fun attachImage() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(
                intent,
                "Select Image from here..."
            ),
            PICK_IMAGE_REQUEST
        )
    }
    override fun onActivityResult(
            requestCode: Int,
            resultCode: Int,
            data: Intent?
    ) {
        super.onActivityResult(
                requestCode,
                resultCode,
                data
        )

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data != null && data.data != null) {

            // Get the Uri of data
            filePath = data.data
            try {

                // Setting image on image view using Bitmap
                val bitmap: Bitmap = MediaStore.Images.Media
                        .getBitmap(
                                activity?.contentResolver,
                                filePath
                        )
                imageView!!.setImageBitmap(bitmap)
            } catch (e: IOException) {
                // Log the exception
                e.printStackTrace()
            }
        }
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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        setWidthPercent(85)
    }


}