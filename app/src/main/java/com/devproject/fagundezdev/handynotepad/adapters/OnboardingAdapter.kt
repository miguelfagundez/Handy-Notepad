package com.devproject.fagundezdev.handynotepad.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.devproject.fagundezdev.handynotepad.view.onboarding.InfoOneFragment
import com.devproject.fagundezdev.handynotepad.view.onboarding.InfoThreeFragment
import com.devproject.fagundezdev.handynotepad.view.onboarding.InfoTwoFragment
import com.devproject.fagundezdev.handynotepad.view.onboarding.RegisterFragment

/********************************************
 * Adapter - OnboardingAdapter
 * OnBoarding at the beginning of the App
 * @author: Miguel Fagundez
 * @date: March 12th, 2020
 * @version: 1.0
 * *******************************************/
class OnboardingAdapter(fragmentManager : FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    //***********************************************
    // Showing three features and register fragment
    //***********************************************
    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> InfoOneFragment()
            1 -> InfoTwoFragment()
            2 -> InfoThreeFragment()
            else -> RegisterFragment()
        }
    }

    // Number of views
    override fun getCount(): Int {
        return 4
    }
}