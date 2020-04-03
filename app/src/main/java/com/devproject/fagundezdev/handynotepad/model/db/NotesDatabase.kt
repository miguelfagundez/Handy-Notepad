package com.devproject.fagundezdev.handynotepad.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.devproject.fagundezdev.handynotepad.utils.Constants

/********************************************
 * Notes Database
 * Room database
 * @author: Miguel Fagundez
 * @date: March 06th, 2020
 * @version: 1.0
 * *******************************************/
@Database(entities = [Notes::class], version = Constants.DATABASE_VERSION, exportSchema = false)
abstract class NotesDatabase : RoomDatabase() {

    abstract fun NotesDao(): NotesDAO

    companion object {

        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Context): NotesDatabase? {

            val tempInstance = INSTANCE

            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(NotesDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        NotesDatabase::class.java,
                        Constants.DATABASE_NAME
                    )
                    // TODO : Check allowMainThreadQueries option ***
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE
        }
    }
}