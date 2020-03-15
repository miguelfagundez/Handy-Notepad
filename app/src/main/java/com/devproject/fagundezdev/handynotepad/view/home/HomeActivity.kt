package com.devproject.fagundezdev.handynotepad.view.home

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.adapters.NotesAdapter
import com.devproject.fagundezdev.handynotepad.listeners.NoteClickListener
import com.devproject.fagundezdev.handynotepad.utils.Constants
import com.devproject.fagundezdev.handynotepad.utils.toast
import com.devproject.fagundezdev.handynotepad.view.details.NoteDetailsFragment
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_home.*


/********************************************
 * Activity - HomeActivity
 * Main windows app (List of notes)
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
class HomeActivity : AppCompatActivity() {

    val TAG = "HomeActivity"

    private lateinit var viewModel : NotesViewModel

    private lateinit var itemClickListener: NoteClickListener
    private val noteDetailsFragment = NoteDetailsFragment()
    private lateinit var adapter : NotesAdapter

    // Note Details Fragment (Update Note or Add Note)
    private var isUpdatingNote = true
    // How many checkbox have been pressed
    private var checkBoxPressed = 0

    //*****************************
    // Notes variables, Temp data
    //*****************************
    var title = ""
    var description = ""
    var body = ""
    var priority = 0
    var creation_date = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkBoxPressed = 0

        setupRecyclerView()
        setupViewModel()
        setupFloatingActionButtons()
    }

    private fun setupFloatingActionButtons() {
        //****************************************
        // Adding a new note
        //****************************************
        fabAddNewNote.setOnClickListener {
            toast(getString(R.string.new_note))
            isUpdatingNote = false
            val bundle = Bundle()
            bundle.putBoolean(Constants.IS_ADD_NOTE, isUpdatingNote)
            noteDetailsFragment.arguments = bundle

            supportFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.slide_in,
                    R.anim.slide_out,
                    R.anim.slide_in,
                    R.anim.slide_out
                )
                .replace(R.id.detailFragmentContainer, noteDetailsFragment)
                .addToBackStack(noteDetailsFragment.tag)
                .commit()
        }
        //****************************************
        // Deleting notes (one or many)
        //****************************************
        fabDeleteNotes.setOnClickListener {
            if (checkBoxPressed > 0){
                //******************************
                // Create an Alert Dialog
                // Asking about to delete notes
                //******************************
                val alertDialog = AlertDialog.Builder(this)
                alertDialog.apply {
                    setMessage(getString(R.string.dialog_message))
                    setTitle(getString(R.string.dialog_title))
                    setPositiveButton(getString(R.string.dialog_delete),DialogInterface.OnClickListener { dialogInterface, i ->
                        viewModel.deleteSelectedNotes(checkBoxPressed)
                        toast(getString(R.string.delete_some_notes))
                        checkBoxPressed = 0
                    })
                    setNegativeButton(getString(R.string.dialog_cancel), DialogInterface.OnClickListener { dialogInterface, i ->
                        dialogInterface.cancel()
                    })
                }
                alertDialog.create()
                alertDialog.show()

            }else{
                toast(getString(R.string.none_notes_selected))
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        viewModel.listNotes?.observe(this@HomeActivity, Observer { notes ->
            notes?.let {
                adapter.setNotes(it)
            }
        })
    }

    private fun setupRecyclerView() {

        itemClickListener = object : NoteClickListener{
            override fun onDetailsNote(id: Int?, title: String, description: String, body: String, imageUrl: String,
                                       priority: Int, selected: Boolean, creationDate: String, editDate: String) {
                isUpdatingNote = true

                val bundle = Bundle()

                bundle.putInt(Constants.ID, id?:-1)
                bundle.putString(Constants.TITLE, title)
                bundle.putString(Constants.DESCRIPTION, description)
                bundle.putString(Constants.BODY, body)
                bundle.putBoolean(Constants.IS_SELECTED, selected)
                bundle.putInt(Constants.PRIORITY, priority)
                bundle.putString(Constants.IMAGE_URL, imageUrl)
                Log.d("Test","Home ACtivity: $imageUrl")
                bundle.putBoolean(Constants.IS_ADD_NOTE, isUpdatingNote)
                bundle.putString(Constants.CREATION_DATE, creationDate)
                bundle.putString(Constants.LAST_EDIT_DATE, editDate)

                noteDetailsFragment.arguments = bundle

                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.slide_in,
                        R.anim.slide_out
                    )
                    .replace(R.id.detailFragmentContainer, noteDetailsFragment)
                    .addToBackStack(noteDetailsFragment.tag)
                    .commit()
            }

            override fun onCheckBoxPressed(id:Int?, selected:Boolean) {
                if (selected == true){
                    checkBoxPressed += 1
                }else{
                    checkBoxPressed -= 1
                }
                // Update note into Database
                // Ready for delete
                viewModel.updateSelected(id, selected)
            }

        }
        adapter = NotesAdapter(this, itemClickListener)
        rvListNotes.adapter = adapter
        rvListNotes.layoutManager = LinearLayoutManager(this)
    }

    override fun onPause() {
        // Avoiding databse problems
        viewModel.checkBoxUnsuscribed()
        // Cleaning UI checkboxs
        checkBoxPressed = 0
        super.onPause()
    }
}
