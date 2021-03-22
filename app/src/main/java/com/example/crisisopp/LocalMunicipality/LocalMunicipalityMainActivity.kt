package com.example.crisisopp.LocalMunicipality

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import com.example.crisisopp.R
import com.example.crisisopp.RecyclerView.RecyclerViewFragment

class LocalMunicipalityMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_municipality_main)


        supportFragmentManager.beginTransaction()
            .replace(R.id.RecyclerViewHolder, RecyclerViewFragment())
            .commitNow()
    }
}
