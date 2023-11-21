package me.vikas.mynoteskt.Database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import me.vikas.mynoteskt.Util.Config
import me.vikas.mynoteskt.Model.Notes

@Database(entities = [Notes::class], exportSchema = false, version = 1)
abstract class NotesDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: NotesDatabase? = null

        fun getInstance(context: Application): NotesDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        // Create the database instance
        private fun buildDatabase(context: Application): NotesDatabase {
            return Room.databaseBuilder(context, NotesDatabase::class.java, Config.TABLE_NAME)
                .allowMainThreadQueries()
                .build()
        }
    }


    abstract fun getDao(): NotesDao
}