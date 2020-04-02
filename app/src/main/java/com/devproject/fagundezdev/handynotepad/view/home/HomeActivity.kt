package com.devproject.fagundezdev.handynotepad.view.home

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.adapters.NotesAdapter
import com.devproject.fagundezdev.handynotepad.listeners.NoteClickListener
import com.devproject.fagundezdev.handynotepad.model.db.Notes
import com.devproject.fagundezdev.handynotepad.utils.Constants
import com.devproject.fagundezdev.handynotepad.utils.toast
import com.devproject.fagundezdev.handynotepad.view.details.NoteDetailsFragment
import com.devproject.fagundezdev.handynotepad.view.settings.SettingsFragment
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.activity_home.*
import timber.log.Timber


/********************************************
 * Activity - HomeActivity
 * Main windows app (List of notes)
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
class HomeActivity : AppCompatActivity() {

    private lateinit var viewModel : NotesViewModel

    private lateinit var itemClickListener: NoteClickListener
    private val noteDetailsFragment = NoteDetailsFragment()
    private val settingsFragment = SettingsFragment()
    private lateinit var adapter : NotesAdapter

    // Note Details Fragment (Update Note or Add Note)
    private var isUpdatingNote = true
    // How many checkbox have been pressed
    private var checkBoxPressed = 0
    // Set all selected items
    private var allNotesChecked = false
    // Sorting value
    private var isAscending = true

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

        setupNoteClickListener()
        setupRecyclerView()
        setupViewModel()
        setupFloatingActionButtons()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
        viewModel.listNotes?.observe(this@HomeActivity, Observer { notes ->
            notes?.let {listOfNotes ->
                adapter.setNotes(listOfNotes)
            }
        })
    }

    private fun setupRecyclerView() {

        adapter = NotesAdapter(this, itemClickListener)
        rvListNotes.adapter = adapter
        rvListNotes.layoutManager = LinearLayoutManager(this)
    }

    private fun setupNoteClickListener() {
        itemClickListener = object : NoteClickListener{
            override fun onDetailsNote(id: Long?, title: String, description: String, body: String, imageUrl: String,
                                       priority: Int, selected: Boolean, creationDate: String, editDate: String) {
                isUpdatingNote = true

                val bundle = Bundle()

                bundle.putLong(Constants.ID, id?:-1)
                bundle.putString(Constants.TITLE, title)
                bundle.putString(Constants.DESCRIPTION, description)
                bundle.putString(Constants.BODY, body)
                bundle.putBoolean(Constants.IS_SELECTED, selected)
                bundle.putInt(Constants.PRIORITY, priority)
                bundle.putString(Constants.IMAGE_URL, imageUrl)

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

            override fun onCheckBoxPressed(id:Long?, selected:Boolean) {
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
    }

    //*************************************************
    // Menu inflater - Action Bar
    //*************************************************
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        // Invisible - it is visible for note details only
        menu?.let {menu ->
            menu.findItem(R.id.menu_delete_note)?.isVisible = false
            menu.findItem(R.id.menu_copy_note)?.isVisible = false
            //***********************************************
            //  -- Need to check Last edit (Date format) --
            // -- For managing sort correctly --
            //***********************************************
            menu.findItem(R.id.menu_order_last_edit_asc).isVisible = false
            //menu.findItem(R.id.menu_order_date_desc).isVisible = false
            //menu.findItem(R.id.menu_order_date_asc).isVisible = false
        }

        return super.onCreateOptionsMenu(menu)
    }

    // Action if button is pressed: calling settings
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            // MENU: Settings
            R.id.menu_action_settings -> {
                supportFragmentManager.beginTransaction()
                    .setCustomAnimations(
                        R.anim.slide_in,
                        R.anim.slide_out,
                        R.anim.slide_in,
                        R.anim.slide_out
                    )
                    .replace(R.id.detailFragmentContainer, settingsFragment)
                    .addToBackStack(settingsFragment.tag)
                    .commit()
            }
            // MENU: Select all card views
            R.id.menu_select_all_settings -> {
                allNotesChecked = !allNotesChecked

                when(allNotesChecked){
                    true ->  checkBoxPressed = 1
                    else -> checkBoxPressed = 0
                }
                viewModel.selectAllNotes(allNotesChecked)

            }
            // MENU: Sort ASC (A..Z)
            R.id.menu_order_asc -> {
                viewModel.getListNotesAsc()?.let { listAsc ->
                    adapter.setNotes(listAsc)
                }
            }
            // MENU: Sort DESC (Z..A)
            R.id.menu_order_desc -> {
                viewModel.getListNotesDesc()?.let { listDesc ->
                    adapter.setNotes(listDesc)
                }
            }

            // MENU: Sort by creation_date
            R.id.menu_order_date_asc ->{
                viewModel.getListNotesDateAsc()?.let { listAsc ->
                adapter.setNotes(listAsc)
            }}
            R.id.menu_order_date_desc -> {
                viewModel.getListNotesDateDesc()?.let { listDesc ->
                    adapter.setNotes(listDesc)
                }
            }

            // MENU: Sort by last_edit
            //R.id.menu_order_last_edit_asc ->{
                //viewModel.getListNotesLastEditAsc()?.let { listAsc ->
                    //adapter.setNotes(listAsc)
                //}}

        }
        return super.onOptionsItemSelected(item)
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
                Timber.i("Entre a borrar")
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


    override fun onPause() {
        // Avoiding databse problems
        viewModel.checkBoxUnsuscribed()
        // Cleaning UI checkboxs
        checkBoxPressed = 0
        super.onPause()
    }
}
