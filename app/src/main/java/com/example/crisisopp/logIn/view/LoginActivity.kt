package com.example.crisisopp.logIn.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.crisisopp.FarahFoundation.FarahFoundationMainActivity
import com.example.crisisopp.LocalMunicipality.LocalMunicipalityMainActivity
import com.example.crisisopp.MainMunicipality.MainMunicipalityMainActivity
import com.example.crisisopp.R
import com.example.crisisopp.logIn.repository.LoginRepository
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
    // Initialize Firebase Auth

    val TAG = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        prefs = getPreferences(Context.MODE_PRIVATE)
        val email = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val signInButton = findViewById<Button>(R.id.signup)

        signInButton.setOnClickListener() {
            viewModel.loginWithCoroutines(email.text.toString(), password.text.toString())
        }
    }
//        signInButton.setOnClickListener(){
//            auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString())
//                .addOnCompleteListener(this) { task ->
//                    if (task.isSuccessful) {
//                        // Sign in success, update UI with the signed-in user's information
//                        Log.d(TAG, "signInWithEmail:success")
//                        val firebaseUser = auth.currentUser
//                        changeButtontext()
//                        LayoutSelecter(firebaseUser.email)
//                    } else {
//                        // If sign in fails, display a message to the user.
//                        Log.w(TAG, "signInWithEmail:failure", task.exception)
//                        Toast.makeText(
//                            baseContext, "Authentication failed.",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
//            val message = "Local Municipality"
//            val intent = Intent(this, LocalMunicipalityMainActivity::class.java).apply {
//                putExtra(EXTRA_MESSAGE, message)
//            }
//            startActivity(intent
//    }

    fun changeButtontext(){
        val user = auth.currentUser
        val name = user.email
        val reqButton = findViewById<Button>(R.id.display_button)
        reqButton.setText(name)
    }
//
    fun setProgressBar(bar: ProgressBar) {
        progressBar = bar
    }

    fun showProgressBar() {
        progressBar?.visibility = View.VISIBLE
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.INVISIBLE
    }

    fun LayoutSelecter(userEmail: String){
        val mainRef = "@main"
        val localRef = "@local"
        val farahRef = "@farah"
        if(userEmail.contains(mainRef)){
            val message = "Main Municipality"
            val intent = Intent(this, MainMunicipalityMainActivity::class.java).apply {
                startActivity(intent)
            }
        }else if(userEmail.contains(localRef)){
            val message = "Local Municipality"
            val intent = Intent(this, LocalMunicipalityMainActivity::class.java)
                startActivity(intent)

        }else if(userEmail.contains(farahRef)){
            val message = "Farah Foundation"
            val intent = Intent(this, FarahFoundationMainActivity::class.java)
                startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.loginResult.observe(this@LoginActivity, Observer {
            val loginResult = it ?: return@Observer

//            loading.visibility = View.GONE
//            if (loginResult.error != null) {
//                showLoginFailed(loginResult.error)
//            }
//            if (loginResult.success != null) {
//                updateUiWithUser(loginResult.success)
//            }
            setResult(Activity.RESULT_OK)
            //Complete and destroy login activity once successful
            finish()
        })

    }
    private fun updateUiWithUser() {

        val intent = Intent(this, FarahFoundationMainActivity::class.java)
//        intent.putExtra("displayname", displayName)
        startActivity(intent)
    }

    public override fun onStop() {
        super.onStop()
        hideProgressBar()
    }
}