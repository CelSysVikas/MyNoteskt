package me.vikas.mynoteskt.Repository

import androidx.lifecycle.LiveData
import me.vikas.mynoteskt.Database.NotesDao
import me.vikas.mynoteskt.Model.Notes

class NotesRepo(val dao:NotesDao) {

    fun getAllNotes():LiveData<List<Notes>>{
        return dao.getNotes()
    }

    fun insertNote(note:Notes){
        dao.newNote(note)
    }

    fun getNote(id:String):Notes{
        return dao.getNote(id)
    }

    fun getNoteLive(id: String):LiveData<Notes>{
        return dao.getNoteLive(id)
    }

    fun updateNote(note: Notes){
        dao.updateNote(note)
    }

    fun deleteNote(note: Notes){
        dao.deleteNote(note)
    }

    fun deleteData(){
        dao.deleteData()
    }

    fun searchNote(query:String): List<Notes?>? {
       return dao.searchNote(query)
    }

}