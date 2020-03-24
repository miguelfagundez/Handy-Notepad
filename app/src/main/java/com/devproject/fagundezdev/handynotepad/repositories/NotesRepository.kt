package com.devproject.fagundezdev.handynotepad.repositories

import android.provider.Settings
import android.util.Log
import androidx.lifecycle.LiveData
import com.devproject.fagundezdev.handynotepad.model.db.Notes
import com.devproject.fagundezdev.handynotepad.model.db.NotesDAO
import com.devproject.fagundezdev.handynotepad.model.sharedpreferences.NoteSharedPreferences
import com.devproject.fagundezdev.handynotepad.utils.Constants
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

/********************************************
 * Repository - NotesRepository
 * This class connect ViewModel with Model class
 * Room Database / Shared Preferences
 * @author: Miguel Fagundez
 * @date: March 08th, 2020
 * @version: 1.0
 * *******************************************/
class NotesRepository(private val notesDao:NotesDAO?) {

    var listNotes : LiveData<List<Notes>>? = null
    var numberNotes = listNotes?.value?.size?:0

    init {
            listNotes = notesDao?.getAllNotes()
    }

    //**********************************
    // Creat note option for utils uses
    //**********************************
    fun createNote(id: Long?, title: String, description: String, body: String, imageUrl: String,
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
    fun insertNote(id: Long?, title: String, description: String, body: String, imageUrl: String,
                   priority: Int, selected: Boolean, creationDate: String, editDate: String) : Long{
        numberNotes += 1
        return notesDao?.insertNote(createNote(id, title, description, body, imageUrl, priority, selected, creationDate, editDate))?:0
    }

    suspend fun updateNote(id: Long?, title: String, description: String, body: String, imageUrl: String,
                           priority: Int, selected: Boolean, creationDate: String, editDate: String){
        notesDao?.insertNote(createNote(id, title, description, body, imageUrl, priority, selected, creationDate, editDate))
    }

    suspend fun updateSelected(id:Long?, selected: Boolean){
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

        if (size >= 0){
            for(i in 0..size){
                if(notes?.get(i)?.isSelected == true) {
                    notesDao?.deleteNote(notes.get(i))
                    numberNotes -= 1
                }
            }
        }
    }

    suspend fun checkBoxUnsuscribed() {
        val notes = listNotes?.value
        var size = notes?.size?:0
        size -= 1

        for(i in 0..size){
            if(notes?.get(i)?.isSelected == true) {
                notes?.get(i)?.isSelected = false
                notesDao?.updateNote(notes.get(i))
            }
        }
    }


    fun selectAllNotes(value : Boolean) {
        notesDao?.selectAllNotes(value)
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

    // Asking if user is already logged
    fun isLogged(): Boolean? {
        return NoteSharedPreferences.readBoolean(Constants.IS_LOGGED_IN)
    }

    // Writing notes in a files into SD Card
    fun writeFilesInSDCard(fileDir: File) {
        val job = GlobalScope.launch {
            val notes = notesDao?.getAllNotesByPriorityASC()
            val number = notesDao?.getNumberNotes()?:0
            fileDir.mkdir()

            for (i in 0..number-1){
                var myFile = File(fileDir,notes?.get(i)?.title + ".txt")
                var osFile: FileOutputStream?
                try {
                    osFile = FileOutputStream(myFile)
                    osFile.write(notes?.get(i)?.body?.toByteArray())
                    osFile.close()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
    }


}