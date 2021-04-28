package com.example.crisisopp.dialogs

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.crisisopp.R
import com.example.crisisopp.home.view.HomeActivity
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import java.io.File

/**
 * This dialog is presented to the user upon clicking the filter button in the appbar
 */

class PhotoPickerDialog : DialogFragment() {
    private lateinit var imageView: ImageView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.choose_photo, container, false)
        val cameraIcon = view.findViewById<ImageView>(R.id.camera_image_view)
        val gallery_view = view.findViewById<ImageView>(R.id.gallery_image_view)
        val cancelButton = view.findViewById<Button>(R.id.cancel_button)
//        imageView = view.findViewById<ImageView>(R.id.hawiyye_scan_iv)

        cameraIcon.setOnClickListener{
            dispatchTakePictureIntent()
        }
        cancelButton.setOnClickListener {
            dialog?.dismiss()
        }
        return view
    }
    val REQUEST_IMAGE_CAPTURE = 1

    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, "Something went Wrong with the openning the camera", Toast.LENGTH_SHORT).show()
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            val imageBitmap = data?.extras?.get("data") as Bitmap
////            imageView.setImageBitmap(imageBitmap)
            Toast.makeText(context, "Photo has been captured!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.let {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.WRAP_CONTENT
            it.window?.setLayout(width, height)
        }
    }

}

