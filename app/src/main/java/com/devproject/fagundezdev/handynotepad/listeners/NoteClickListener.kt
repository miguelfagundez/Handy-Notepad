package com.devproject.fagundezdev.handynotepad.listeners


/********************************************
 * Listener - NoteClickListener
 * Connect AdapterClass with HomeActivity
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
interface NoteClickListener {

    fun onDetailsNote(id: Long?, title: String, description: String, body: String, imageUrl: String,
                      priority: Int, selected: Boolean, creationDate: String, editDate: String)
    fun onCheckBoxPressed(id:Long?, selected:Boolean)
}