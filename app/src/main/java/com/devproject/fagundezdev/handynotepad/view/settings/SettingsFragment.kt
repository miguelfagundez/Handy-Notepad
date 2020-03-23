package com.devproject.fagundezdev.handynotepad.view.settings

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.view.auth.LoginActivity
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import timber.log.Timber

/********************************************
 * Fragment - NoteDetailsFragment
 * This fragment will manage app's setting
 *     ----- Under construction -----
 * @author: Miguel Fagundez
 * @date: March 14th, 2020
 * @version: 1.0
 * *******************************************/
class SettingsFragment : Fragment() {

    private lateinit var viewModel : NotesViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel()

        btnSettingLogout.setOnClickListener {
            // logOut user: Putting false into SharedPreferences
            viewModel.logoutUser()
            // Calling a new Login Activity
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
            // Destroy the Fragment
            val fragments = activity?.supportFragmentManager
            if(fragments?.backStackEntryCount?:0 > 0) fragments?.popBackStack()
            // Destroy the Activity
            activity?.finish()
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }
}
