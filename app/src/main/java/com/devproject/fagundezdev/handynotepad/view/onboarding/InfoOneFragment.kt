package com.devproject.fagundezdev.handynotepad.view.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devproject.fagundezdev.handynotepad.R
import kotlinx.android.synthetic.main.fragment_one_onboarding.*

class InfoOneFragment : Fragment() {

    lateinit var callPager : callPagerOne

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callPager = context as callPagerOne
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_one_onboarding, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvOnetoTwo.setOnClickListener {
            callPager.onClickNextTwo()
        }
    }
    interface callPagerOne{
        fun onClickNextTwo()
    }
}