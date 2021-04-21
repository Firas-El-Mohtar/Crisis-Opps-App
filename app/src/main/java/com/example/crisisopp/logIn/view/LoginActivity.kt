package com.example.crisisopp.logIn.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import com.example.crisisopp.R
import com.example.crisisopp.home.view.HomeActivity
import com.example.crisisopp.logIn.viewmodel.LoginViewModel
import com.example.crisisopp.logIn.viewmodel.LoginViewModelFactory
import com.example.crisisopp.login.view.LearnMoreActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.util.*


/**
 * Login Activity
 *
 * This activity will not be displayed if the user has previously logged in before
 *
 */

class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> { LoginViewModelFactory(prefs) }
    private lateinit var auth: FirebaseAuth
    lateinit var prefs: SharedPreferences
    private var progressBar: ProgressBar? = null
    private var userType: String? = null
    private lateinit var learnMore: TextView

    // Initialize Firebase Auth
    val TAG = "1234"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        auth = Firebase.auth
        prefs = getPreferences(Context.MODE_PRIVATE)
        progressBar = findViewById(R.id.progress_bar_login)
        learnMore = findViewById(R.id.learn_more)
        learnMore.setOnClickListener {
            val intent = Intent(this, LearnMoreActivity::class.java)
            startActivity(intent)
        }
        
        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signInButton = findViewById<Button>(R.id.signup)
        progressBar = findViewById(R.id.progress_bar_login)
        password.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)


        signInButton.setOnClickListener {
            progressBar?.visibility = View.VISIBLE
            viewModel.loginWithCoroutines(email.text.toString(), password.text.toString())
            progressBar?.visibility = View.GONE
        }
        // checking if the user has an active session
        //skips login if true
        if (auth.currentUser != null) {
            updateUiWithUser()
        } else {
            return
        }
        setResult(Activity.RESULT_OK)
        finish()
    }


    // Function to move to the home activity
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

