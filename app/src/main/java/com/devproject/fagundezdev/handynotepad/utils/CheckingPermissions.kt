package com.devproject.fagundezdev.handynotepad.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import com.devproject.fagundezdev.handynotepad.R

class CheckingPermissions : AppCompatActivity(){

    var listPermissions = ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checking_permissions)
    }


    fun checkingMyPermissions():Boolean {
        val cameraPermission = ContextCompat.checkSelfPermission(this@CheckingPermissions, android.Manifest.permission.CAMERA)
        val writeExternalStoragePermission = ContextCompat.checkSelfPermission(this@CheckingPermissions, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        return writeExternalStoragePermission == PackageManager.PERMISSION_GRANTED &&
                cameraPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestMyPermissions():Boolean{

        val cameraPermission = ContextCompat.checkSelfPermission(this@CheckingPermissions, android.Manifest.permission.CAMERA)
        val readExternalStoragePermission = ContextCompat.checkSelfPermission(this@CheckingPermissions, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeExternalStoragePermission = ContextCompat.checkSelfPermission(this@CheckingPermissions, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (cameraPermission != PackageManager.PERMISSION_GRANTED){
            listPermissions.add(android.Manifest.permission.CAMERA)
        }

        if (readExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
            listPermissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        }

        if (writeExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
            listPermissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        if (!listPermissions.isEmpty()){
            requestPermissions(this, listPermissions.toTypedArray<String>(), Constants.REQUEST_USES_PERMISSIONS)
            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            Constants.REQUEST_USES_PERMISSIONS -> {
                if(grantResults.isNotEmpty() and (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    toast(getString(R.string.permissions_granted_msg))
                }else{
                    toast(getString(R.string.permissions_for_images))
                }
            }
        }
    }

}