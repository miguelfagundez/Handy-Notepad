package com.devproject.fagundezdev.handynotepad.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.devproject.fagundezdev.handynotepad.model.db.Notes
import com.devproject.fagundezdev.handynotepad.model.db.NotesDAO
import com.devproject.fagundezdev.handynotepad.model.sharedpreferences.NoteSharedPreferences
import com.devproject.fagundezdev.handynotepad.utils.Constants

/********************************************
 * Repository - NotesRepository
 * This class connect ViewModel with Model class
 * Room Database / Shared Preferences
 * @author: Miguel Fagundez
 * @date: March 08th, 2020
 * @version: 1.0
 * *******************************************/
class NotesRepository(private val notesDao:NotesDAO?) {

    val TAG = "Test"
    var listNotes : LiveData<List<Notes>>? = notesDao?.getAllNotes()
    var numberNotes = listNotes?.value?.size?:0

    init {

    }

    fun createNote(id: Int?, title: String, description: String, body: String, imageUrl: String,
                   priority: Int, selected: Boolean, creationDate: String, editDate: String) : Notes {

        var note = Notes()

        note.id = id
        note.title = title
        note.description = description
        note.body = body
        note.image_url = imageUrl
        note.priority = priority
        note.isSelected = selected?:false
        note.creation_date = creationDate
        note.edit_date = editDate

        return note
    }

    //*********************************************
    // Room Database
    //*********************************************
    suspend fun insertNote(id: Int?, title: String, description: String, body: String, imageUrl: String,
                           priority: Int, selected: Boolean, creationDate: String, editDate: String){
        notesDao?.insertNote(createNote(id, title, description, body, imageUrl, priority, selected, creationDate, editDate))
        numberNotes += 1
    }

    suspend fun updateNote(id: Int?, title: String, description: String, body: String, imageUrl: String,
                           priority: Int, selected: Boolean, creationDate: String, editDate: String){
        notesDao?.insertNote(createNote(id, title, description, body, imageUrl, priority, selected, creationDate, editDate))
    }

    suspend fun updateSelected(id:Int?, selected: Boolean){
        var note = notesDao?.getNote(id)
        note?.isSelected = selected
        notesDao?.updateNote(note)
    }

    suspend fun deleteAll(){
        notesDao?.deleteAllNotes()
        numberNotes = 0
    }

    suspend fun deleteSelectedNotes() {
        val notes = listNotes?.value
        var size = notes?.size?:0
        size -= 1
        Log.d(TAG, "SIZE: $size")
        if (size >= 0){
            for(i in 0..size){
                if(notes?.get(i)?.isSelected == true) {
                    notesDao?.deleteNote(notes.get(i))
                    numberNotes -= 1
                    Log.d(TAG, "Note deleted(position): $i")
                }
            }
        }
    }

    suspend fun checkBoxUnsuscribed() {
        val notes = listNotes?.value
        var size = notes?.size?:0
        size -= 1
        Log.d(TAG, "SIZE: $size")
        for(i in 0..size){
            if(notes?.get(i)?.isSelected == true) {
                notes?.get(i)?.isSelected = false
                notesDao?.updateNote(notes.get(i))
                Log.d(TAG, "i: $i")
            }
        }
    }

    //*********************************************
    // Share preferences
    //*********************************************
    // Writing
    fun writingLoginCredentials(username : String, password : String){
        NoteSharedPreferences.write(Constants.USERNAME, username)
        NoteSharedPreferences.write(Constants.PASSWORD, password)
    }

    // Reading
    fun checkingLoginCredentials(username : String, password : String):Boolean{
        var data_checked = NoteSharedPreferences.readString(Constants.USERNAME)
        if (data_checked != username) return false
        data_checked = NoteSharedPreferences.readString(Constants.PASSWORD)
        if (data_checked != password) return false
        return true
    }

    // Register USER
    fun registerUserSuccessfully(){
        NoteSharedPreferences.write(Constants.IS_REGISTER_SUCCESSFULLY, true)
    }

    fun isRegistered():Boolean?{
        return NoteSharedPreferences.readBoolean(Constants.IS_REGISTER_SUCCESSFULLY)
    }

    // Log In USER
    fun loginUser(value : Boolean){
        NoteSharedPreferences.write(Constants.IS_LOGGED_IN, value)
    }

    fun isLogged(): Boolean? {
        return NoteSharedPreferences.readBoolean(Constants.IS_LOGGED_IN)
    }
}