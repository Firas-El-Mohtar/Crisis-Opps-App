package com.example.crisisopp.LogIn

import android.content.Intent
import android.os.Bundle
import android.provider.AlarmClock.EXTRA_MESSAGE
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.LocalMunicipality.LocalMunicipalityMainActivity
import com.example.crisisopp.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var progressBar: ProgressBar? = null
    // Initialize Firebase Auth

    val TAG = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signInButton = findViewById<Button>(R.id.signup)
        signInButton.setOnClickListener(){
            auth.signInWithEmailAndPassword(username.text.toString(), password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        changeButtontext()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
//            val message = "Local Municipality"
//            val intent = Intent(this, LocalMunicipalityMainActivity::class.java).apply {
//                putExtra(EXTRA_MESSAGE, message)
//            }
//            startActivity(intent)
        }
    }
    fun changeButtontext(){
        val user = auth.currentUser
        val name = user.email
        val reqButton = findViewById<Button>(R.id.display_button)
        reqButton.setText(name)
    }

    fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }



    public override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}