package com.example.crisisopp.FarahFoundation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.crisisopp.R
import com.example.crisisopp.RecyclerView.RecyclerViewFragment

class FarahFoundationMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_farah_foundation_main)

        supportFragmentManager.beginTransaction()
            .replace(R.id.RecyclerViewHolder, RecyclerViewFragment())
            .commitNow()
    }

}