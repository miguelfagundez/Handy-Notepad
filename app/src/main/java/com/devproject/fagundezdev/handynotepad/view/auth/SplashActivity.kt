package com.devproject.fagundezdev.handynotepad.view.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.view.home.HomeActivity
import com.devproject.fagundezdev.handynotepad.view.onboarding.OnboardingActivity
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel

/*
* Class name: SplashActivity
* I handle initial variables/components
* I validate the current state of the application
*   1: First time -> Open OnBoardinActivity
*   2: No Logging -> Login Activity
*   3: Logging Successfully -> Notes List Activity
* */
class SplashActivity : AppCompatActivity() {

    val TAG = "SplashActivity"

    private lateinit var viewModel : NotesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupViewModel()
        callInitialAppState()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }


    private fun callInitialAppState() {
        // Validate the current state
        // 1: First time -> Open OnBoardinActivity
        // 2: No Logging -> Login Activity
        // 3: Logging Successfully -> Notes List Activity

        if (viewModel.isUserRegistered() == true){
            if(viewModel.isUserLogged() == true){
                // Home Activity
                val intent : Intent = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                // Login Activity
                val intent : Intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }else{
            // OnBoarding Activity
            val intent : Intent = Intent(this@SplashActivity, OnboardingActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}
