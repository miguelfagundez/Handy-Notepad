package com.devproject.fagundezdev.handynotepad.model.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/********************************************
 * Entity - Table Notes
 * Table structure definition
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
* *******************************************/
@Entity (tableName = "notes_table")
data class Notes(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int? = null,
    @ColumnInfo(name = "title")
    var title : String = "title",
    @ColumnInfo(name = "description")
    var description : String = "",
    @ColumnInfo(name = "body")
    var body : String = "",
    @ColumnInfo(name = "image_url")
    var image_url : String = "",
    @ColumnInfo(name = "priority")
    var priority : Int = 0,
    @ColumnInfo(name = "isSelected")
    var isSelected : Boolean = false,
    @ColumnInfo(name = "creation_date")
    var creation_date : String = "",
    @ColumnInfo(name = "edit_date")
    var edit_date : String = "") {
}