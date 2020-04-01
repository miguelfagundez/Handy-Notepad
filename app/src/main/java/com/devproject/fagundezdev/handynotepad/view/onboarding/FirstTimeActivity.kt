package com.devproject.fagundezdev.handynotepad.view.onboarding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.adapters.FirstTimePagerAdapter
import com.devproject.fagundezdev.handynotepad.utils.Constants
import com.devproject.fagundezdev.handynotepad.view.onboarding.OnboardingData
import com.devproject.fagundezdev.handynotepad.view.onboarding.RegisterFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_first_time.*

/*************************************************
 * Activity: FirstTimeActivity
 * I handle initial variables/components
 * I show important features of app
 *   1: First time -> Open Onboarding Screens
 * ************************************************/
class FirstTimeActivity : AppCompatActivity() {

    // Controling navegation between OnBoarding screens
    private var position : Int = 0
    // ViewPager Adapter
    private lateinit var adapter : FirstTimePagerAdapter
    // Animation
    private lateinit var btnAnimation : Animation
    // Register Fragment
    private val registerFragment = RegisterFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time)

        // Set up Views
        setupViews()
        // Set up the view pager
        setupViewPager()
        // Set up view visibility
        setupViewVisibility()
        // setup buttons listeners
        setupListeners()

    }

    private fun setupViews() {
        btnAnimation = AnimationUtils.loadAnimation(this, R.anim.btn_animation)
    }

    private fun setupViewVisibility() {
        // Button Back visibility
        btnBack.isVisible = position != 0

        // Button next and Go to Register visibility
        if (position == Constants.NUM_ONBOARDING_SCREENS-1) {
            btnNext.visibility = View.INVISIBLE
            btnGoToRegister.visibility = View.VISIBLE
            btnGoToRegister.animation = btnAnimation
        }else{
            btnNext.visibility = View.VISIBLE
            btnGoToRegister.visibility = View.INVISIBLE
        }
    }

    private fun setupOnboardingData() : ArrayList<OnboardingData> {
        // OnBoarding Data
        var list = ArrayList<OnboardingData>()
        list.add(
            OnboardingData(
                getString(R.string.title_onboarding),
                getString(R.string.one_time_register),
                R.drawable.data_protection_mobile_min
            )
        )
        list.add(
            OnboardingData(
                getString(R.string.title_picture_onboarding),
                getString(R.string.notes_pictures),
                R.drawable.take_picture_min
            )
        )
        list.add(
            OnboardingData(
                getString(R.string.title_memory_onboarding),
                getString(R.string.notes_external_card),
                R.drawable.phone_memory_min
            )
        )
        return list
    }

    private fun setupViewPager() {
        // Init adapter
        adapter = FirstTimePagerAdapter(this, setupOnboardingData())
        vpIntro.adapter = adapter
        // Set up TabLayout with this viewpager
        tabPagerIndicator.setupWithViewPager(vpIntro)
    }

    private fun setupListeners() {

        // NEXT btn
        btnNext.setOnClickListener {
            position = vpIntro.currentItem
            if (position < Constants.NUM_ONBOARDING_SCREENS){
                position += 1
                vpIntro.setCurrentItem(position)
                setupViewVisibility()
            }
        }
        // BACK btn
        btnBack.setOnClickListener {
            position = vpIntro.currentItem
            if (position > 0){
                position -= 1
                vpIntro.setCurrentItem(position)
                setupViewVisibility()
            }
        }
        // Tab View Pager
        tabPagerIndicator.addOnTabSelectedListener(object:TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {

            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                position = p0?.position?:0
                if (position == Constants.NUM_ONBOARDING_SCREENS - 1) setupViewVisibility()
                if (position >= 0) setupViewVisibility()
            }

        })
        // Go to Register btn
        btnGoToRegister.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                .replace(R.id.flGoToRegister, registerFragment)
                .addToBackStack(registerFragment.tag)
                .commit()

        }
    }
}
