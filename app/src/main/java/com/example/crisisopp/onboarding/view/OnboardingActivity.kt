package com.example.crisisopp.onboarding.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.R
import com.example.crisisopp.logIn.view.LoginActivity
import com.example.crisisopp.onboarding.prefsmanager.OnBoardingPrefManager

/**
 * This activity is only shown to the user once upon opening the app for the first time
 */

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity_layout)
        val prefManager = OnBoardingPrefManager(this)

        //checking if the user has previously seen the onboarding screens
        if (!prefManager.isFirstTimeLaunch) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}