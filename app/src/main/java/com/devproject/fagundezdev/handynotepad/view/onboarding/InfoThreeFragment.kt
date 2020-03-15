package com.devproject.fagundezdev.handynotepad.view.onboarding

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.devproject.fagundezdev.handynotepad.R
import kotlinx.android.synthetic.main.fragment_three_onboarding.*
import kotlinx.android.synthetic.main.fragment_two_onboarding.*

/********************************************
 * Fragment - InfoThreeFragment
 * This fragment shows info about:
 * - Save your notes in external memory
 * @author: Miguel Fagundez
 * @date: March 08th, 2020
 * @version: 1.0
 * *******************************************/
class InfoThreeFragment : Fragment() {

    lateinit var callThree : callPagerThree

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callThree = context as callPagerThree
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_three_onboarding, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvThreeToRegister.setOnClickListener {
            callThree.clickNextRegister()
        }

        tvThreeToTwo.setOnClickListener {
            callThree.clickBackTwo()
        }
    }
    interface callPagerThree{
        fun clickNextRegister()
        fun clickBackTwo()
    }
}