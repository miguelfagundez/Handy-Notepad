package com.devproject.fagundezdev.handynotepad.utils

/********************************************
 * Constants
 * Singlenton class that handle app constants
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
object Constants {

    //***********************************************************
    // Note model
    //***********************************************************
    const val ID = "note_id"
    const val TITLE = "note_title"
    const val DESCRIPTION = "note_description"
    const val BODY = "note_body"
    const val IMAGE_URL = "note_image"
    const val PRIORITY = "note_priority"
    const val IS_SELECTED = "note_is_selected"
    const val CREATION_DATE = "note_creation_date"
    const val LAST_EDIT_DATE = "note_last_edit_date"

    //***********************************************************
    // Shared preferences
    //***********************************************************
    const val SHARED_PREFERENCES_NAME = "handy_notes_pref"
    const val USERNAME = "username"
    const val PASSWORD = "password"

    //***********************************************************
    // Checking User Session
    //***********************************************************
    const val IS_REGISTER_SUCCESSFULLY = "register_successfully"
    const val IS_LOGGED_IN = "is_logged_in"

    //***********************************************************
    // Room Database
    //***********************************************************
    const val DATABASE_NAME = "handy_notes_db"
    const val DATABASE_VERSION = 1

    //***********************************************************
    // Activity/Fragment Flow control
    //***********************************************************
    const val IS_ADD_NOTE = "adding_new_note"

    //***********************************************************
    // Format, permissions, and other general uses
    //***********************************************************
    const val DATE_FORMAT = "EEE, d MMM yyyy HH:mm"
    const val DATE_PICTURE_FORMAT = "yyyyMMddHHmmss"
    const val REQUEST_CODE_CAMERA = 2
    const val REQUEST_CODE_GALLERY = 4
    const val PERMISSION_IMAGE_CODE = 135
    const val PERMISSION_FILE_CODE = 248
    const val REQUEST_USES_PERMISSIONS = 6248
}