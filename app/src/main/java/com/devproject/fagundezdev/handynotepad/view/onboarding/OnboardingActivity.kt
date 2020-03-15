package com.devproject.fagundezdev.handynotepad.view.onboarding

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.adapters.OnboardingAdapter
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel

/*************************************************
 * Activity - OnboardingActivity
 * This activity will handle fragments that
 * shows basic information about app's features
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * ***********************************************/
class OnboardingActivity : AppCompatActivity(), InfoOneFragment.callPagerOne, InfoTwoFragment.callPagerTwo,
    InfoThreeFragment.callPagerThree, RegisterFragment.callPagerRegister {

    private lateinit var viewModel : NotesViewModel
    private lateinit var viewPager : ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        setupViewModel()
        setupViewPager()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun setupViewPager() {
        viewPager = findViewById(R.id.vpOnboardingContainer)
        val adapter = OnboardingAdapter(supportFragmentManager)
        viewPager.adapter = adapter
    }

    // Fragment One -> Fragment Two
    override fun onClickNextTwo() {
        viewPager.currentItem = 1
    }

    // Fragment Two -> Fragment Three
    // Fragment Two -> Fragment One
    override fun clickNextThree() {
        viewPager.currentItem = 2
    }

    override fun clickBackOne() {
        viewPager.currentItem = 0
    }

    // Fragment Three -> Register
    // Fragment Three -> Fragment Two
    override fun clickNextRegister() {
        viewPager.currentItem = 3
    }

    override fun clickBackTwo() {
        viewPager.currentItem = 1
    }

    override fun callBackThree() {
        viewPager.currentItem = 2
    }
}