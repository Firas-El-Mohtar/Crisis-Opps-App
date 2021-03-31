package com.example.crisisopp.home.view

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.crisisopp.LocalMunicipality.CreateForm
import com.example.crisisopp.R
import com.example.crisisopp.RecyclerView.RecyclerViewFragment
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.crisisopp.home.viewmodel.HomeViewModelFactory
import com.example.crisisopp.logIn.viewmodel.LoginViewModel
import com.example.crisisopp.logIn.viewmodel.LoginViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity() {
    private val homeViewModel by viewModels<HomeViewModel> { HomeViewModelFactory(userType) }

    private lateinit var userType: String
    // todo get usetype(mutable) from intent
//  val y = userType
    private lateinit var mainFab: FloatingActionButton
    private lateinit var homeCareFab: FloatingActionButton
    private lateinit var pcrFab: FloatingActionButton
    private lateinit var pcrText: TextView
    private lateinit var homeCareText: TextView
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var clicked= false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_municipality_main)
        intent.getStringExtra("UserType")?.let {
            userType = it
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.RecyclerViewHolder, RecyclerViewFragment())
            .commitNow()

        mainFab = findViewById(R.id.main_fab)
        homeCareFab = findViewById(R.id.fab_home_care)
        pcrFab = findViewById(R.id.fab_pcr)
        pcrText = findViewById(R.id.text_view_pcr)
        homeCareText = findViewById(R.id.text_view_home_care)

        mainFab.setOnClickListener{
            onAddButtonClicked()

        }
        homeCareFab.setOnClickListener{
            val dialog = CreateForm()
            dialog.show(supportFragmentManager, "Create Form")
        }
        pcrFab.setOnClickListener {

        }

    }


    private fun onAddButtonClicked(){
        setVisibility(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        setAnimation(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        clicked = !clicked
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.fABStatus(mainFab)
    }

    private fun setAnimation(clicked: Boolean, pcrFAB: FloatingActionButton, pcrText: TextView, homeCareFab: FloatingActionButton, homeCareText: TextView) {
        if(!clicked){
            pcrFAB.startAnimation(fromBottom)
            homeCareFab.startAnimation(fromBottom)
            mainFab.startAnimation(rotateOpen)
            homeCareText.startAnimation(fromBottom)
            pcrText.startAnimation(fromBottom)
        }
        else{
            pcrFAB.startAnimation(toBottom)
            homeCareFab.startAnimation(toBottom)
            mainFab.startAnimation(rotateClose)
            pcrText.startAnimation(toBottom)
            homeCareText.startAnimation(toBottom)

        }
    }

    private fun setVisibility(clicked: Boolean, pcrFAB: FloatingActionButton, pcrText: TextView, homeCareFab: FloatingActionButton, homeCareText: TextView) {
        if(!clicked){
            pcrFAB.visibility = VISIBLE
            homeCareFab.visibility = VISIBLE
            homeCareText.visibility = VISIBLE
            pcrText.visibility = VISIBLE
        }
        else{
            pcrFAB.visibility = GONE
            homeCareFab.visibility = GONE
            homeCareText.visibility = GONE
            pcrText.visibility = GONE

        }
    }



}