package com.example.crisisopp.logIn.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.R
import java.util.*

class LearnMoreActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_learn_more)
        var toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_learn_more)

        var language = Locale.getDefault().displayLanguage
        if (language == "English"){
            toolbar.menu.findItem(R.id.back_icon).icon = getDrawable(R.drawable.ic_back_icon_english)
        }

        toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.back_icon -> {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

    }
}