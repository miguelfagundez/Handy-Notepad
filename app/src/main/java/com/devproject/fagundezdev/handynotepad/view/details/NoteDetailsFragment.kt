package com.devproject.fagundezdev.handynotepad.view.details

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.devproject.fagundezdev.handynotepad.BuildConfig
import com.devproject.fagundezdev.handynotepad.R
import com.devproject.fagundezdev.handynotepad.utils.Constants
import com.devproject.fagundezdev.handynotepad.utils.toast
import com.devproject.fagundezdev.handynotepad.viewmodel.NotesViewModel
import kotlinx.android.synthetic.main.fragment_notes_details.*
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

/********************************************
 * Fragment - NoteDetailsFragment
 * This fragment handle:
 *  - Adding notes
 *  - Editing notes
 * @author: Miguel Fagundez
 * @date: March 07th, 2020
 * @version: 1.0
 * *******************************************/
class NoteDetailsFragment : Fragment() {

    val TAG = "NoteDetailsFragment"
    private lateinit var viewModel : NotesViewModel

    //*****************************
    // Notes variables, Temp data
    //*****************************
    var noteID : Int? = null
    var title = ""
    var description = ""
    var body = ""
    var image_url = ""
    var priority = 0
    var isSelected : Boolean = false
    var creation_date = ""
    var edit_date = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupImageDialog()
    }

    private fun setupImageDialog() {
        // Image was pressed
        ivDetailsImage.setOnClickListener {
            // Asking for permissions to access:
            // 1. Camera
            // 2. Gallery
            if (requestPermissions()) {
                setupOptionDialog()
            }
        }
    }

    private fun setupOptionDialog() {
        val view = LayoutInflater.from(activity).inflate(R.layout.dialog_selector_permissions, null)
        val textViewCamera = view.findViewById<TextView>(R.id.tvCameraPermissionMessage)
        val textViewGallery = view.findViewById<TextView>(R.id.tvGalleryPermissionMessage)
        val dialog = AlertDialog.Builder(activity)
            .setView(view)
            .setCancelable(true)
            .create()

        // Gallery was pressed
        textViewGallery.setOnClickListener {
            val intentGallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intentGallery, Constants.REQUEST_CODE_GALLERY)
            toast("Gallery selected")
            dialog.hide()
        }

        // Camera was pressed
        textViewCamera.setOnClickListener {
            val intentCamera = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            var imageFile: File? = null
            val timeStamp = SimpleDateFormat(Constants.DATE_PICTURE_FORMAT).format(Date())
            val fileName = "Picture_" + timeStamp + "_"
            val directory = activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            imageFile = File.createTempFile(fileName, ".jpg", directory)
            if(imageFile != null){
                //TODO Checking context!!
                val imageUri = FileProvider.getUriForFile(context!!, BuildConfig.APPLICATION_ID + ".provider", imageFile)
                Log.d("Test","${imageFile.absolutePath}")
                image_url = imageFile.absolutePath
                intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
                startActivityForResult(intentCamera, Constants.REQUEST_CODE_CAMERA)
            }
            toast("Camera selected")
            dialog.hide()
        }

        dialog.show()
    }

    private fun requestPermissions(): Boolean {
        //TODO Checking activity!!.applicationContext (Double bang)
        val cameraPermission = ContextCompat.checkSelfPermission(activity!!.applicationContext, android.Manifest.permission.CAMERA)
        val readExternalStoragePermission = ContextCompat.checkSelfPermission(activity!!.applicationContext, android.Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeExternalStoragePermission = ContextCompat.checkSelfPermission(activity!!.applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)

        val listPermissions = ArrayList<String>()

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
            requestPermissions(listPermissions.toTypedArray<String>(), Constants.PERMISSION_CODE)
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
            Constants.PERMISSION_CODE -> {
                if(grantResults.isNotEmpty() and (grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                    setupOptionDialog()
                }else{
                    toast(getString(R.string.permissions_for_images))
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                Constants.REQUEST_CODE_GALLERY -> {sendGalleryImageToImageView(data)}
                Constants.REQUEST_CODE_CAMERA -> { sendCameraImageToImageView(data)}
            }
        }
    }

    private fun sendCameraImageToImageView(data: Intent?) {
        Glide.with(this).load(image_url).into(ivDetailsImage)
    }

    private fun sendGalleryImageToImageView(data: Intent?) {
        val selectedGalleryImage = data?.data?: Uri.EMPTY
        ivDetailsImage.setImageURI(selectedGalleryImage)
        image_url = selectedGalleryImage.toString()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProviders.of(this).get(NotesViewModel::class.java)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        cleanComponents()

        //***********************
        // Update or Add options
        //***********************
        val isUpdating = arguments?.getBoolean(Constants.IS_ADD_NOTE)
        if (isUpdating == true){
            //****************************
            // Update our note
            //****************************
            arguments?.getInt(Constants.ID).let { noteID ->
                if (noteID != -1){

         //TODO("Refactor code - if/else - One call arguments")

                    title = arguments?.getString(Constants.TITLE)?:""

                    etDetailsTitle.setText(title)
                    etDetailsDescription.setText(arguments?.getString(Constants.DESCRIPTION))
                    etDetailsBody.setText(arguments?.getString(Constants.BODY))
                    tvDetailsCreationDateNote.setText(arguments?.getString(Constants.CREATION_DATE))
                    tvDetailsLastEditNote.setText(arguments?.getString(Constants.LAST_EDIT_DATE))
                    creation_date = tvDetailsCreationDateNote.text.toString()
                    val image = arguments?.getString(Constants.IMAGE_URL)
                    image_url = image?:""
                    if (image.isNullOrEmpty()||image.isNullOrBlank()){
                        toast("True")
                        Glide.with(ivDetailsImage).load(R.drawable.ic_launcher_foreground).into(ivDetailsImage)
                    }else{
                        toast("False")
                        Log.d("Test","False")
                        Log.d("Test","Details Fragment: $image")
                        Glide.with(ivDetailsImage).load(image).into(ivDetailsImage)
                    }


                    //ivDetailsImage.setImageURI(Uri.parse(arguments?.getString(Constants.IMAGE_URL)))
                }
            }

            fabDetailsDone.setImageResource(R.drawable.ic_edit)
            fabDetailsDone.setOnClickListener {
                if(etDetailsTitle.text.isNotBlank()){

                    val simpleCurrentDate = SimpleDateFormat(Constants.DATE_FORMAT)
                    val simpleStringDate = simpleCurrentDate.format(Date())

                    noteID = arguments?.getInt(Constants.ID)
                    title = etDetailsTitle.text.toString()
                    description = etDetailsDescription.text.toString()
                    body = etDetailsBody.text.toString()
                    priority = 1
                    isSelected = arguments?.getBoolean(Constants.IS_SELECTED)?:false
                    edit_date = simpleStringDate

                    viewModel.update(noteID, title, description, body, image_url,priority, isSelected, creation_date, edit_date)

                    activity?.onBackPressed()
                }else{
                    toast("Title cannot be empty")
                }
            }
        }else{
            //****************************
            // Add a new note
            //****************************

            val simpleCurrentDate = SimpleDateFormat(Constants.DATE_FORMAT)
            val simpleStringDate = simpleCurrentDate.format(Date())

            tvDetailsCreationDateNote.setText(simpleStringDate)

            fabDetailsDone.setImageResource(R.drawable.ic_done)
            fabDetailsDone.setOnClickListener {
                if(etDetailsTitle.text.isNotBlank()){
                    noteID = null
                    title = etDetailsTitle.text.toString()
                    description = etDetailsDescription.text.toString()
                    body = etDetailsBody.text.toString()
                    priority = 1
                    //image_url = ""
                    isSelected = false
                    creation_date = simpleStringDate
                    edit_date = ""

                    viewModel.insert(noteID, title, description, body, image_url,priority, isSelected, creation_date, edit_date)

                    activity?.onBackPressed()
                }else{
                    toast("Title cannot be empty")
                }
            }
        }

    }

    fun cleanComponents(){
        etDetailsTitle.setText("")
        etDetailsDescription.setText("")
        etDetailsBody.setText("")
        tvDetailsCreationDateNote.setText("")
        tvDetailsLastEditNote.setText("")
        image_url = ""
    }

}