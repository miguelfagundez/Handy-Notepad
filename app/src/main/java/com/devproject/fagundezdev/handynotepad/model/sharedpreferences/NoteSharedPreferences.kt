package com.devproject.fagundezdev.handynotepad.model.sharedpreferences

import android.content.Context
import android.content.SharedPreferences
import com.devproject.fagundezdev.handynotepad.utils.Constants

/********************************************
 * NoteSharedPreferences Object
 * Singlenton object
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
object NoteSharedPreferences {

    private var sharedPreferences : SharedPreferences? = null

    fun getSharedPreferences(appContext: Context) {
        if(sharedPreferences == null){
            sharedPreferences = appContext.getSharedPreferences(Constants.SHARED_PREFERENCES_NAME, Context.MODE_PRIVATE)
        }
    }

    //****************************************
    // Write options available:
    // String, Boolean, Int
    //****************************************
    fun write(key:String, value:String){
        val editor = sharedPreferences?.edit()
        editor?.putString(key, value)
        editor?.apply()
    }

    fun write(key:String, value:Int){
        val editor = sharedPreferences?.edit()
        editor?.putInt(key, value)
        editor?.apply()
    }
    fun write(key:String, value:Boolean){
        val editor = sharedPreferences?.edit()
        editor?.putBoolean(key, value)
        editor?.apply()
    }

    //****************************************
    // Read options available:
    // String, Boolean, Int
    //****************************************
    fun readBoolean(key:String):Boolean? {
        return sharedPreferences?.getBoolean(key,false)
    }

    fun readInt(key:String):Int? {
        return sharedPreferences?.getInt(key,-1)
    }

    fun readString(key:String):String? {
        return sharedPreferences?.getString(key,"none")
    }




}