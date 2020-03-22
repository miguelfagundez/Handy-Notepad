package com.devproject.fagundezdev.handynotepad.adapters

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.listeners.NoteClickListener
import com.devproject.fagundezdev.handynotepad.model.db.Notes
import kotlinx.android.synthetic.main.note_item_layout.view.*
import timber.log.Timber

/********************************************
 * Adapter - NotesAdapter
 * RecycleView in HomeActivity
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
class NotesAdapter(private val context: Context, private val itemClickListener: NoteClickListener) : RecyclerView.Adapter<NotesAdapter.ViewHolder>() {

    private var inflater = LayoutInflater.from(context)
    private var notes = emptyList<Notes>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.note_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentNote = notes[position]
        holder.apply {
            // Data into cardView
            textTitle.text = currentNote.title
            textDescription.text = currentNote.description
            checkBox.isChecked = currentNote.isSelected
            tvDate.text = currentNote.creation_date

            // Image component
            if(currentNote.image_url.isNullOrEmpty() || currentNote.image_url.isNullOrBlank()){
                // Default image (logo app)
                Glide.with(image).load(R.drawable.ic_launcher_foreground).into(image)

                Timber.i("Default Image")
            }else{

                Timber.i("Gallery Image")
                Timber.i("Adapter: ${currentNote.image_url}")
                // Custom image (Camera || Gallery)
                Glide.with(image).load(currentNote.image_url).into(image)
            }

            // CheckBox - Options for deleting
            itemView.cbItemMarkCompleted.setOnClickListener {
                currentNote.isSelected = itemView.cbItemMarkCompleted.isChecked
                itemClickListener.onCheckBoxPressed(currentNote.id, currentNote.isSelected)

            }

            // Listener for CardView item - Showing notes details
            itemView.setOnClickListener {
                itemClickListener.onDetailsNote(currentNote.id, currentNote.title, currentNote.description, currentNote.body,
                currentNote.image_url, currentNote.priority, currentNote.isSelected, currentNote.creation_date, currentNote.edit_date)
            }
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }

    inner class ViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        var textTitle : TextView = itemView.findViewById(R.id.tvItemTitle)
        var textDescription : TextView = itemView.findViewById(R.id.tvItemDescription)
        var image : ImageView = itemView.findViewById(R.id.ivItemNoteImage)
        var checkBox : CheckBox = itemView.findViewById(R.id.cbItemMarkCompleted)
        var tvDate : TextView = itemView.findViewById(R.id.tvNoteCreationDate)
    }

    //********************************************
    // Adding a note this method will be called
    //********************************************
    fun setNotes(notes:List<Notes>){
        this.notes = notes
        notifyDataSetChanged()
    }
}