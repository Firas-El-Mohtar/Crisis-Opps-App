package com.example.crisisopp.login.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.R
import com.example.crisisopp.logIn.view.LoginActivity
import java.util.*

class LearnMoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_more)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_learn_more)


        var language = Locale.getDefault().displayLanguage
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}