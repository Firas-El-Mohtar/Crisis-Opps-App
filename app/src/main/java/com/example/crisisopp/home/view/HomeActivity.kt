package com.example.crisisopp.home.view

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.crisisopp.LocalMunicipality.HomeCareFormDialog
import com.example.crisisopp.LocalMunicipality.CreatePcrForm
import com.example.crisisopp.LocalMunicipality.ExampleAdapter
import com.example.crisisopp.LocalMunicipality.FormContentDialog
import com.example.crisisopp.R
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.crisisopp.home.viewmodel.HomeViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeActivity : AppCompatActivity(){

    val TAG = "JNCICUBIUBQRV"
    private val homeViewModel by viewModels<HomeViewModel> { HomeViewModelFactory(userType!!, userToken!!, municipalityName!!) }


    val firestore = Firebase.firestore
    private lateinit var userType: String
    private lateinit var userToken: String
    private lateinit var userId: String
    private lateinit var municipalityName: String
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
    private lateinit var recyclerView: RecyclerView
    private lateinit var exapmleAdapter: ExampleAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_municipality_main)
        intent.getStringExtra("UserType")?.let {
            userType = it
        }
        intent.getStringExtra("UserToken")?.let {
            userToken = it
        }
        intent.getStringExtra("MunicipalityName")?.let {
            municipalityName = it
        }


        homeViewModel.selectedForm.observe(this@HomeActivity, Observer {
                val dialog = FormContentDialog()
                dialog.show(supportFragmentManager, "View Form")
        })

        exapmleAdapter = ExampleAdapter(homeViewModel)
        recyclerView = findViewById(R.id.recycler_view_test)
        recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = exapmleAdapter.apply {
                notifyDataSetChanged()
            }
        }

        mainFab = findViewById(R.id.main_fab)
        if(homeViewModel.canCreateForm()){
            mainFab.visibility = VISIBLE
            mainFab.setOnClickListener{
                onAddButtonClicked()
            }
        } else mainFab.visibility = GONE

        homeCareFab = findViewById(R.id.fab_home_care)
        pcrFab = findViewById(R.id.fab_pcr)
        pcrText = findViewById(R.id.text_view_pcr)
        homeCareText = findViewById(R.id.text_view_home_care)

        homeCareFab.setOnClickListener{
            val dialog = HomeCareFormDialog()
            dialog.show(supportFragmentManager, "Create Form")
        }
        pcrFab.setOnClickListener {
            val pcrDialog = CreatePcrForm()
            pcrDialog.show(supportFragmentManager, "Create PCR Form" )
        }
    }

    private fun onAddButtonClicked(){
        setVisibility(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        setAnimation(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        clicked = !clicked
    }

    override fun onResume() {
        super.onResume()
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

    override fun onStart() {
        super.onStart()
        exapmleAdapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        exapmleAdapter.stopListening()
    }



}