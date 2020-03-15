package com.devproject.fagundezdev.handynotepad.view.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.utils.ResponseObj
import com.devproject.fagundezdev.handynotepad.utils.toast
import com.devproject.fagundezdev.handynotepad.view.home.HomeActivity
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_login.*

/********************************************
 * Activity - LoginActivity
 * This activity show the login screen
 * @author: Miguel Fagundez
 * @date: March 07th, 2020
 * @version: 1.0
 * *******************************************/
class LoginActivity : AppCompatActivity() {

    private lateinit var viewModel : NotesViewModel
    private lateinit var response : ResponseObj

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setupViewModel()
        setupListeners()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    private fun setupListeners() {
        //******************************************
        // Sign In View
        //******************************************
        btnLoginSignIn.setOnClickListener {
            response = viewModel.checkingLoginCredentials(etLoginUsername.text.toString(),
                etLoginPassword.text.toString())
            // If credentials are validated, then go to home screen
            if (response.validate == true){
                viewModel.isSignInChecked(cbLoginStaySignIn.isChecked)
                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }
            toast(response.message)
        }
    }

}