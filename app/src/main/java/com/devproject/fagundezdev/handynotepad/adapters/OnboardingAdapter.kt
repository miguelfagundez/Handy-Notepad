package com.devproject.fagundezdev.handynotepad.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.devproject.fagundezdev.handynotepad.view.onboarding.InfoOneFragment
import com.devproject.fagundezdev.handynotepad.view.onboarding.InfoThreeFragment
import com.devproject.fagundezdev.handynotepad.view.onboarding.InfoTwoFragment
import com.devproject.fagundezdev.handynotepad.view.onboarding.RegisterFragment

class OnboardingAdapter(fragmentManager : FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> InfoOneFragment()
            1 -> InfoTwoFragment()
            2 -> InfoThreeFragment()
            else -> RegisterFragment()
        }
    }

    override fun getCount(): Int {
        return 4
    }
}