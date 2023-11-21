package me.vikas.mynoteskt.Database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import me.vikas.mynoteskt.Model.Notes
import me.vikas.mynoteskt.Util.Config

@Dao
interface NotesDao {

    @Query("select * from ${Config.TABLE_NAME} order by ${Config.COLUMN_NOTE_PINNED} desc")
    fun getNotes():LiveData<List<Notes>>;

    @Query("select * from ${Config.TABLE_NAME} where ${Config.COLUMN_ID} = :id")
    fun getNote(id:String): Notes;

    @Query("select * from ${Config.TABLE_NAME} where ${Config.COLUMN_ID} = :id")
    fun getNoteLive(id:String): LiveData<Notes>;

    @Insert
    fun newNote(notes: Notes)

    @Update
    fun updateNote(notes: Notes)

    @Delete
    fun deleteNote(notes: Notes)

    @Query("Delete from " + Config.TABLE_NAME)
    fun deleteData()

    @Query("Select * from " + Config.TABLE_NAME + " where " +
                Config.COLUMN_TITLE + " like :query " +
                " or " +
                Config.COLUMN_CONTENT + " like :query")
    fun searchNote(query: String?): List<Notes?>?
}