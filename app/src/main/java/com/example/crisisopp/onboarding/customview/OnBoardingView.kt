package com.example.crisisopp.onboarding.customview

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager2.widget.ViewPager2
import com.example.crisisopp.R
import com.example.crisisopp.logIn.view.LoginActivity
import com.example.crisisopp.onboarding.entity.OnBoardingPage
import com.example.crisisopp.onboarding.entity.OnBoardingPagerAdapter
import com.example.crisisopp.onboarding.prefsmanager.OnBoardingPrefManager
import com.google.android.material.button.MaterialButton
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import setParallaxTransformation

class OnBoardingView @JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) :
    FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val numberOfPages by lazy { OnBoardingPage.values().size }
    private val prefManager: OnBoardingPrefManager
    lateinit var slider: ViewPager2
    lateinit var nextBtn: MaterialButton
    lateinit var skipBtn: MaterialButton
    lateinit var startBtn: MaterialButton
    lateinit var onboardingRoot: MotionLayout
    init {
        val view = LayoutInflater.from(context).inflate(R.layout.onboarding_view, this, true)
        slider = view.findViewById(R.id.slider)
        nextBtn = view.findViewById(R.id.nextBtn)
        skipBtn = view.findViewById(R.id.skipBtn)
        startBtn = view.findViewById(R.id.startBtn)
        onboardingRoot = view.findViewById(R.id.onboardingRoot)
        setUpSlider(view)
        addingButtonsClickListeners()
        prefManager = OnBoardingPrefManager(view.context)
    }



    private fun setUpSlider(view: View) {
        with(slider) {
            adapter = OnBoardingPagerAdapter()
            setPageTransformer { page, position ->
                setParallaxTransformation(page, position)
            }

            addSlideChangeListener()

            val wormDotsIndicator = view.findViewById<WormDotsIndicator>(R.id.page_indicator)
            wormDotsIndicator.setViewPager2(this)
        }
    }


    private fun addSlideChangeListener() {

        slider.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)
                if (numberOfPages > 1) {
                    val newProgress = (position + positionOffset) / (numberOfPages - 1)
                    onboardingRoot.progress = newProgress
                }
            }
        })
    }

    private fun addingButtonsClickListeners() {
        nextBtn.setOnClickListener { navigateToNextSlide() }
        skipBtn.setOnClickListener {
            setFirstTimeLaunchToFalse()
            navigateToMainActivity()
        }
        startBtn.setOnClickListener {
            setFirstTimeLaunchToFalse()
            navigateToMainActivity()
        }
    }

    private fun setFirstTimeLaunchToFalse() {
        prefManager.isFirstTimeLaunch = false
    }

    private fun navigateToNextSlide() {
        val nextSlidePos: Int = slider?.currentItem?.plus(1) ?: 0
        slider?.setCurrentItem(nextSlidePos, true)
    }

    private fun navigateToMainActivity(){
        val intent = Intent(context , LoginActivity::class.java)
        context.startActivity(intent)
    }
}