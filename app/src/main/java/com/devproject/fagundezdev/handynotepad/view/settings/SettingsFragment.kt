package com.devproject.fagundezdev.handynotepad.view.settings

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.utils.Constants
import com.devproject.fagundezdev.handynotepad.utils.toast
import com.devproject.fagundezdev.handynotepad.view.auth.LoginActivity
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        setupListeners()

    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    // TEMPORAL
    private fun setupListeners() {
        // Export files option
        btnSettingExport.setOnClickListener {

            //********************************
            // Writing files in SD Card v1.0
            //********************************
            val state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                // Permissions temporal
                if (checkPermissions()) {
                    // Creating the file directory
                    var sdCard : File? = activity?.applicationContext?.getExternalFilesDir(null)
                    val timeStamp = SimpleDateFormat(Constants.DATE_PICTURE_FORMAT).format(Date())
                    val fileDir = File(sdCard?.absolutePath + "/handynotes_" + timeStamp +"/")

                    // Calling ViewModel to write the files
                    viewModel.writeFilesInSDCard(fileDir)

                } else {
                    // If permissions are not granted
                    // we request these permissions (READ and WRITE External Storage)
                    requestMyPermissions()
                }
            } else {
                // No SD Card is detected
                toast(getString(R.string.sd_card_detected_msg))
            }
            toast(getString(R.string.files_exported_msg))
            //**************************************************************************************
        }


        // Logout option
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



    /*FragmentManager fm = getActivity().getSupportFragmentManager();
      for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
        fm.popBackStack();
      }   */

    //***********************************************************************************
    //                              CHECKING PERMISSIONS
    //***********************************************************************************

    private fun checkPermissions(): Boolean {
        activity?.let {fragmentActivity ->
            val writeExternalPermision = ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            if(writeExternalPermision == PackageManager.PERMISSION_GRANTED) {
                return true
            }else {
                return false
            }
        }
        return false
    }


    private fun requestMyPermissions(): Boolean {
        var permissionDecision = false
        activity?.let {fragmentActivity ->
            val readExternalStoragePermission = ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.READ_EXTERNAL_STORAGE)
            val writeExternalStoragePermission = ContextCompat.checkSelfPermission(fragmentActivity, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

            val listPermissions = ArrayList<String>()

            if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                listPermissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            }

            if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
                listPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            }

            if (!listPermissions.isEmpty()){
                requestPermissions(listPermissions.toTypedArray<String>(), Constants.PERMISSION_FILE_CODE)
                return false
            }
            permissionDecision = true
        }

        return permissionDecision
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Constants.PERMISSION_FILE_CODE -> {
                if(grantResults.isNotEmpty() and (grantResults[0] == PackageManager.PERMISSION_GRANTED)){

                }else{
                    toast(getString(R.string.permissions_for_images))
                }
            }
        }
    }
}
