package com.example.crisisopp.logIn.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.crisisopp.home.view.HomeActivity
import com.example.crisisopp.R
import com.example.crisisopp.logIn.viewmodel.LoginViewModel
import com.example.crisisopp.logIn.viewmodel.LoginViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking


class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel> { LoginViewModelFactory(prefs) }
    private lateinit var auth: FirebaseAuth
    private lateinit var rememberCheckBox: CheckBox
    lateinit var prefs: SharedPreferences
    private var progressBar: ProgressBar? = null
    private var userType: String? = null
    // Initialize Firebase Auth
    val TAG = "1234"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        prefs = getPreferences(Context.MODE_PRIVATE)
        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signInButton = findViewById<Button>(R.id.signup)
        progressBar = findViewById(R.id.loading)
        var userPrefs = getSharedPreferences("userPrefs", MODE_PRIVATE)
        var userPrefsEditor = userPrefs.edit()

        var preferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        val checkValue = preferences.getString("remember", "")
        if (checkValue.equals("true")) {
            var username = userPrefs.getString("username", "")!!
            var pass = userPrefs.getString("password", "")!!
            runBlocking {
                viewModel.loginWithCoroutines(username, pass)
                delay(5000)
            }
        } else if (checkValue.equals("false")) {
            return
        }

        setContentView(R.layout.activity_main)

        rememberCheckBox = findViewById(R.id.rememberMeCheckBox)
        rememberCheckBox.setOnCheckedChangeListener { buttonView, isChecked ->
            Toast.makeText(this, isChecked.toString(), Toast.LENGTH_SHORT).show()


            if (buttonView.isChecked) {
                var prefs = getSharedPreferences("checkbox", MODE_PRIVATE)
                var prefsEditor = prefs.edit()
                prefsEditor.putString("remember", "true")
                prefsEditor.apply()

            } else if (!buttonView.isChecked) {
                var prefs = getSharedPreferences("checkbox", MODE_PRIVATE)
                var prefsEditor = prefs.edit()
                prefsEditor.putString("remember", "false")
                prefsEditor.apply()
            }
        }




        signInButton.setOnClickListener() {
            showProgressBar()
            userPrefsEditor.putString("username", email.text.toString().toLowerCase())
            userPrefsEditor.putString("password", password.text.toString().toLowerCase())
            userPrefsEditor.apply()
            viewModel.loginWithCoroutines(email.text.toString().toLowerCase(), password.text.toString().toLowerCase())
            hideProgressBar()
        }
    }


    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    override fun onResume() {
        super.onResume()
        viewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

            progressBar?.visibility = View.GONE
            if (loginResult.error != null) {
                (loginResult.error)
            }
            if (loginResult.success != null) {
                updateUiWithUser()
            }
            setResult(Activity.RESULT_OK)
            finish()

        })
    }

    private fun updateUiWithUser() {
//        viewModel.userType?.let {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("UserType", viewModel.userType)
            intent.putExtra("UserToken", viewModel.userToken)
            intent.putExtra("MunicipalityName", viewModel.municipalityName)
            startActivity(intent)

    }
    }

//    public override fun onStop() {
//        super.onStop()
//        hideProgressBar()
//    }
