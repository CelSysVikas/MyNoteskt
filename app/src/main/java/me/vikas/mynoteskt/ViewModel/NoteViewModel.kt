package me.vikas.mynoteskt.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import me.vikas.mynoteskt.Database.NotesDatabase
import me.vikas.mynoteskt.Model.Notes
import me.vikas.mynoteskt.Repository.NotesRepo

class NoteViewModel(application: Application) : AndroidViewModel(application) {

    val notesRepo: NotesRepo

    init {
        val database = NotesDatabase.getInstance(application)
        notesRepo = NotesRepo(database.getDao())
    }

    fun getAllNotes(): LiveData<List<Notes>> {
        return notesRepo.getAllNotes()
    }

    fun insertNote(note: Notes) {
        notesRepo.insertNote(note)
    }

    fun getNote(id: String): Notes {
        return notesRepo.getNote(id)
    }

    fun getNoteLive(id: String): LiveData<Notes> {
        return notesRepo.getNoteLive(id)
    }

    fun updateNote(note: Notes) {
        notesRepo.updateNote(note)
    }

    fun deleteNote(note: Notes){
        notesRepo.deleteNote(note)
    }

    fun deleteData(){
        notesRepo.deleteData()
    }

    fun searchNote(query:String): List<Notes?>? {
        return notesRepo.searchNote(query)
    }

}