package com.example.crisisopp.onboarding

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.R
import com.example.crisisopp.logIn.view.LoginActivity
import com.example.crisisopp.onboarding.prefsmanager.OnBoardingPrefManager
import org.json.JSONObject

class OnboardingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.onboarding_activity)
        val prefManager = OnBoardingPrefManager(this)


        if(!prefManager.isFirstTimeLaunch){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}