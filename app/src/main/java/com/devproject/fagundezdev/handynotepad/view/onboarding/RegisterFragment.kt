package com.devproject.fagundezdev.handynotepad.view.onboarding

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.utils.toast
import com.devproject.fagundezdev.handynotepad.view.auth.LoginActivity
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_register.*

/********************************************
 * Fragment - RegisterFragment
 * This fragment shows info about:
 * - Basic username/password register
 * @author: Miguel Fagundez
 * @date: March 08th, 2020
 * @version: 1.0
 * *******************************************/
class RegisterFragment : Fragment() {

    private lateinit var callBack : callPagerRegister
    private lateinit var viewModel : NotesViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callBack = context as callPagerRegister
        setupViewModel()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun setupListeners() {
        ivRegisterArrowBack.setOnClickListener {
            callBack.callBackThree()
        }

        btnRegister.setOnClickListener {
            checkRegisterData()
        }
    }

    interface callPagerRegister{
        fun callBackThree()
    }

    private fun checkRegisterData() {
        val response = viewModel.checkingCredentialsAndRegister(etRegisterUsername.text.toString(), etRegisterConfirmUsername.text.toString(),
        etRegisterPassword.text.toString(), etRegisterConfirmPassword.text.toString())

        // Going to login Activity
        if (response.validate == true) {
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }
        toast(response.message)

    }
}