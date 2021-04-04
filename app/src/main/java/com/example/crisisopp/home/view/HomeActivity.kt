package com.example.crisisopp.home.view

import android.os.Bundle
import android.view.Gravity
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.crisisopp.LocalMunicipality.HomeCareFormDialog
import com.example.crisisopp.LocalMunicipality.CreatePcrForm
import com.example.crisisopp.LocalMunicipality.FormContentDialog
import com.example.crisisopp.LocalMunicipality.PcrFormDialog
import com.example.crisisopp.R
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.crisisopp.home.viewmodel.HomeViewModelFactory
import com.example.crisisopp.viewpager.ViewPagerAdapter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
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
    private lateinit var topAppBar: MaterialToolbar
    private val rotateOpen: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_open_anim) }
    private val rotateClose: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.rotate_close_anim) }
    private val fromBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.from_bottom_anim) }
    private val toBottom: Animation by lazy { AnimationUtils.loadAnimation(this, R.anim.to_bottom_anim) }
    private var clicked= false

    private lateinit var drawerLayout :DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLogo: ImageView
    private lateinit var tvDrawerUsername: TextView
    private lateinit var tvDrawerEmail: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_municipality_main)

        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)

        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager){tab, position ->
            when(position){
                0-> tab.text = "Home Care"
                1-> tab.text = "PCR"
            }
        }.attach()
        intent.getStringExtra("UserType")?.let {
            userType = it
        }
        intent.getStringExtra("UserToken")?.let {
            userToken = it
        }
        intent.getStringExtra("MunicipalityName")?.let {
            municipalityName = it
        }


        homeViewModel.selectedHomeCareForm.observe(this@HomeActivity, Observer {
                val dialog = FormContentDialog()
                dialog.show(supportFragmentManager, "View Form")
        })

        homeViewModel.selectedPcrForm.observe(this@HomeActivity, Observer {
            val dialog = PcrFormDialog()
            dialog.show(supportFragmentManager, "View Form")
        })
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

        topAppBar = findViewById(R.id.topAppBar)
        setSupportActionBar(topAppBar)
        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navView)

        topAppBar.setNavigationOnClickListener {
            drawerLayout.openDrawer(Gravity.LEFT)

            drawerLogo = findViewById(R.id.drawerLogo)
            tvDrawerUsername = findViewById(R.id.tvDrawerUsername)
            tvDrawerEmail = findViewById(R.id.tvDrawerEmail)

            setDrawerInfo(userType, municipalityName)

        }
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle menu item selected
            menuItem.isChecked = true
            drawerLayout.closeDrawers()
            true
        }
        topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.favorite -> {
                    // Handle favorite icon press
                    true
                }
                R.id.search -> {
                    // Handle search icon press
                    true
                }
                R.id.more -> {
                    // Handle more item (inside overflow menu) press
                    true
                }
                else -> false
            }
        }

    }

    private fun onAddButtonClicked(){
        setVisibility(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        setAnimation(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        clicked = !clicked
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
    private fun setDrawerInfo(userType: String, municipalityName: String){
        tvDrawerEmail.setText("$municipalityName@$userType.com")
        tvDrawerUsername.setText(municipalityName[0].toUpperCase()+municipalityName.substring(1))
        drawerLogo.setImageResource(R.drawable.ainwzeinlogo)
    }



}