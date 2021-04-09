package com.example.crisisopp.home.view

import android.content.Intent
import android.os.Bundle
import android.view.View.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.example.crisisopp.LocalMunicipality.HomeCareFormDialog
import com.example.crisisopp.LocalMunicipality.CreatePcrForm
import com.example.crisisopp.LocalMunicipality.FormContentDialog
import com.example.crisisopp.LocalMunicipality.PcrFormDialog
import com.example.crisisopp.R
import com.example.crisisopp.RecyclerView.HomeCareAppointmentFragment
import com.example.crisisopp.RecyclerView.HomeCareFormsFragment
import com.example.crisisopp.RecyclerView.PcrFormsFragment
import com.example.crisisopp.extensions.emailDomain
import com.example.crisisopp.extensions.municipalityName
import com.example.crisisopp.home.viewmodel.HomeViewModel
import com.example.crisisopp.home.viewmodel.HomeViewModelFactory
import com.example.crisisopp.logIn.view.LoginActivity
import com.example.crisisopp.viewpager.ViewPagerAdapter
import com.example.crisisopp.viewpager.ViewPagerAppointmentsAdapter
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class HomeActivity : AppCompatActivity() {
    val TAG = "JNCICUBIUBQRV"

    private val homeViewModel by viewModels<HomeViewModel> {
        HomeViewModelFactory(
            userType!!,
            municipalityName!!
        )
    }
    private lateinit var toggle: ActionBarDrawerToggle
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
    private lateinit var appBarLayout: AppBarLayout
    private lateinit var materialToolbar: CollapsingToolbarLayout

    private val rotateOpen: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_open_anim
        )
    }
    private val rotateClose: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.rotate_close_anim
        )
    }
    private val fromBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.from_bottom_anim
        )
    }
    private val toBottom: Animation by lazy {
        AnimationUtils.loadAnimation(
            this,
            R.anim.to_bottom_anim
        )
    }
    private var clicked = false
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var drawerLogo: ImageView
    private lateinit var tvDrawerUsername: TextView
    private lateinit var tvDrawerEmail: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_layout_test)

        val auth = Firebase.auth
        val userEmail = auth.currentUser.email
        userType = userEmail.emailDomain
        municipalityName = userEmail.municipalityName
        userId = auth.currentUser.uid


        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = findViewById<ViewPager2>(R.id.view_pager)
        val farahUser = homeViewModel.farahUser()

        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.Open, R.string.Close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        var navView: NavigationView = findViewById(R.id.nav_view_test)
        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.appointments -> {
                    if(farahUser){
                        viewPager.visibility = GONE
                        val farahFragment = findViewById<FrameLayout>(R.id.farah_recycler_view)
                        val farahAppointmentFragment = HomeCareAppointmentFragment()
                        tabLayout.visibility = GONE
                        farahFragment.visibility = VISIBLE
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.farah_recycler_view, farahAppointmentFragment)
                            .commitNow()
                    }else{
                        mainFab.visibility = GONE
                        val adapter = ViewPagerAppointmentsAdapter(supportFragmentManager, lifecycle)
                        viewPager.adapter = adapter
                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                            when (position) {
                                0 -> {
                                    tab.text = "HomeCare Appointments"
                                }
                                1 -> {
                                    tab.text = "PCR Appointments"
                                }
                            }
                        }.attach()

                    }


                }
                R.id.logout -> {
                    homeViewModel.logout()
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                }
                R.id.change_language -> {
                        //TODO: Change Language
                }
                R.id.forms -> {
                    if (farahUser) {
                        viewPager.visibility = GONE
                        val farahFragment = findViewById<FrameLayout>(R.id.farah_recycler_view)
                        tabLayout.visibility = GONE
                        farahFragment.visibility = VISIBLE
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.farah_recycler_view, HomeCareFormsFragment())
                            .commitNow()
                    }else{
                        val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
                        viewPager.adapter = adapter
                        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                            when (position) {
                                0 -> {
                                    tab.text = "Home Care"
                                    tab.icon = resources.getDrawable(R.drawable.ic_home_care, theme)
                                }
                                1 -> {
                                    tab.text = "PCR"
                                    tab.icon = resources.getDrawable(R.drawable.ic_corona_virus_rv, theme)
                                }
                            }
                        }.attach()
                    }

                }

            }
            true
        }


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
        if (homeViewModel.canCreateForm()) {
            mainFab.visibility = VISIBLE
            tabLayout.visibility = VISIBLE
            mainFab.setOnClickListener {
                onAddButtonClicked()
            }
        } else mainFab.visibility = GONE

        if (farahUser) {
            viewPager.visibility = GONE
            val farahFragment = findViewById<FrameLayout>(R.id.farah_recycler_view)
            tabLayout.visibility = GONE
            farahFragment.visibility = VISIBLE
            supportFragmentManager.beginTransaction()
                .replace(R.id.farah_recycler_view, HomeCareFormsFragment())
                .commitNow()
        } else {
            val adapter = ViewPagerAdapter(supportFragmentManager, lifecycle)
            viewPager.adapter = adapter
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                when (position) {
                    0 -> {
                        tab.text = "Home Care"
                        tab.icon = resources.getDrawable(R.drawable.ic_home_care, theme)
                    }
                    1 -> {
                        tab.text = "PCR"
                        tab.icon = resources.getDrawable(R.drawable.ic_corona_virus_rv, theme)
                    }
                }
            }.attach()
        }

        homeCareFab = findViewById(R.id.fab_home_care)
        pcrFab = findViewById(R.id.fab_pcr)
        pcrText = findViewById(R.id.text_view_pcr)
        homeCareText = findViewById(R.id.text_view_home_care)
        homeCareFab.setOnClickListener {
            val dialog = HomeCareFormDialog()
            dialog.show(supportFragmentManager, "Create Form")
        }
        pcrFab.setOnClickListener {
            val pcrDialog = CreatePcrForm()
            pcrDialog.show(supportFragmentManager, "Create PCR Form")
        }

    }

    private fun onAddButtonClicked() {
        setVisibility(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        setAnimation(clicked, pcrFab, pcrText, homeCareFab, homeCareText)
        clicked = !clicked
    }


    private fun setAnimation(
        clicked: Boolean,
        pcrFAB: FloatingActionButton,
        pcrText: TextView,
        homeCareFab: FloatingActionButton,
        homeCareText: TextView
    ) {
        if (!clicked) {
            pcrFAB.startAnimation(fromBottom)
            homeCareFab.startAnimation(fromBottom)
            mainFab.startAnimation(rotateOpen)
            homeCareText.startAnimation(fromBottom)
            pcrText.startAnimation(fromBottom)
        } else {
            pcrFAB.startAnimation(toBottom)
            homeCareFab.startAnimation(toBottom)
            mainFab.startAnimation(rotateClose)
            pcrText.startAnimation(toBottom)
            homeCareText.startAnimation(toBottom)
        }
    }

    private fun setVisibility(
        clicked: Boolean,
        pcrFAB: FloatingActionButton,
        pcrText: TextView,
        homeCareFab: FloatingActionButton,
        homeCareText: TextView
    ) {
        if (!clicked) {
            pcrFAB.visibility = VISIBLE
            homeCareFab.visibility = VISIBLE
            homeCareText.visibility = VISIBLE
            pcrText.visibility = VISIBLE
        } else {
            pcrFAB.visibility = GONE
            homeCareFab.visibility = GONE
            homeCareText.visibility = GONE
            pcrText.visibility = GONE
        }
    }
}
