package com.example.crisisopp.LocalMunicipality

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.R
import com.example.crisisopp.RecyclerView.RecyclerViewFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import java.io.File


class LocalMunicipalityMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_municipality_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.RecyclerViewHolder, RecyclerViewFragment())
            .commitNow()
        var newFormButton : FloatingActionButton = findViewById(R.id.floating_action_button)
        newFormButton.setOnClickListener(

        )

    }
//        val mStorageRef = FirebaseStorage.getInstance().getReference();
//
//        val file: Uri = Uri.fromFile(File("Desktop/download.jfif"))
//        val riversRef: StorageReference = mStorageRef.child("images/rivers.jpg")
//
//        val filepath: Uri = "gs://crisis-opps-app.appspot.com/forms"
//        var imageRef: StorageReference = FirebaseStorage.getInstance().reference.child("Desktop/download.jfif")
//        imageRef.putFile(filepath)
//            //todo
//        }
//        riversRef.putFile(file)
//            .addOnSuccessListener { taskSnapshot : UploadTask.TaskSnapshot -> // Get a URL to the uploaded content
//                val downloadUrl: Uri = taskSnapshot.getDownloadUrl()
//            }
//            .addOnFailureListener {
//                // Handle unsuccessful uploads
//                // ...
//            }
//    }


}
