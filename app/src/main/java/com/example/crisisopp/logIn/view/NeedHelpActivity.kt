package com.example.crisisopp.login.view

import android.content.Intent
import android.os.Bundle
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.R
import com.example.crisisopp.logIn.view.LoginActivity

class NeedHelpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_need_help)
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_need_help)

        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        }
    

    }
