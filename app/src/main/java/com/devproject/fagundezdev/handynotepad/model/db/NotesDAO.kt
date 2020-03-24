package com.devproject.fagundezdev.handynotepad.model.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE

/********************************************
 * Notes Data Access Object
 * Queries definition
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
@Dao
interface NotesDAO {

    // Gets the current notes list and it
    // is updated automatically when the data
    // changes
    @Query("SELECT * FROM notes_table")
    fun getAllNotes():LiveData<List<Notes>>

    @Insert(onConflict = REPLACE)
    //suspend fun insertNote(note:Notes?)
    fun insertNote(note:Notes?) : Long

    @Update
    suspend fun updateNote(note:Notes?)

    @Delete
    suspend fun deleteNote(note:Notes)

    // Get the notes order by priority values (0,1,2)
    @Query("SELECT * FROM notes_table ORDER BY priority ASC")
    suspend fun getAllNotesByPriorityASC():List<Notes>

    @Query("SELECT * FROM notes_table ORDER BY priority DESC")
    suspend fun getAllNotesByPriorityDESC():List<Notes>

    @Query("SELECT * FROM notes_table GROUP BY isSelected")
    suspend fun getAllNotesByGroup():List<Notes>

    @Query("DELETE FROM notes_table")
    suspend fun deleteAllNotes()

    @Query("SELECT * FROM notes_table WHERE id = :idValue")
    suspend fun getNote(idValue : Long?) : Notes

    @Query("SELECT COUNT(id) FROM notes_table")
    fun getNumberNotes() : Int

    @Query("UPDATE notes_table SET isSelected = :value")
    fun selectAllNotes(value : Boolean)
}