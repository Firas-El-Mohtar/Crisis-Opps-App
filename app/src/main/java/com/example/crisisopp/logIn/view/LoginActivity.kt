package com.example.crisisopp.logIn.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.crisisopp.R
import com.example.crisisopp.home.view.HomeActivity
import com.example.crisisopp.logIn.viewmodel.LoginViewModel
import com.example.crisisopp.logIn.viewmodel.LoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> { LoginViewModelFactory(prefs) }
    private lateinit var auth: FirebaseAuth
    lateinit var prefs: SharedPreferences
    private var progressBar: ProgressBar? = null
    private var userType: String? = null

    // Initialize Firebase Auth
    val TAG = "1234"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = Firebase.auth
        prefs = getPreferences(Context.MODE_PRIVATE)
        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signInButton = findViewById<Button>(R.id.signup)
        signInButton.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            viewModel.loginWithCoroutines(email.text.toString(), password.text.toString())
            progressBar?.visibility = View.GONE
        }
        if (auth.currentUser != null) {
            updateUiWithUser()
        } else {
            return
        }
        setResult(Activity.RESULT_OK)
        finish()
    }


    private fun updateUiWithUser() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("UserType", viewModel.userType)
        intent.putExtra("MunicipalityName", viewModel.municipalityName)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        viewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer
            if (loginResult.success != null) {
                updateUiWithUser()
            }
            setResult(Activity.RESULT_OK)
            finish()
        })
    }

}

