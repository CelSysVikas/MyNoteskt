package me.vikas.mynoteskt.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import me.vikas.mynoteskt.Model.Notes
import me.vikas.mynoteskt.R
import me.vikas.mynoteskt.ViewModel.NoteViewModel
import me.vikas.mynoteskt.databinding.ActivityNoteReaderBinding

class NoteReaderActivity : AppCompatActivity() {

    private lateinit var dataBinding:ActivityNoteReaderBinding
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding=DataBindingUtil.setContentView(this,R.layout.activity_note_reader)
        dataBinding.navigation.setNavigationOnClickListener { finish() }


        intent.getStringExtra("noteID")?.let {id->
            noteViewModel.getNoteLive(id).observe(this, Observer {
                initReader(it)
            })
        }
    }

    private fun initReader(note:Notes){
        dataBinding.noteTitle=note.title
        dataBinding.noteContent=note.content
        dataBinding.noteDate=getString(R.string.dateCreated) + note.dateTime
        note.colorCode?.let { initColor(it) }

        dataBinding.ibEditNote.setOnClickListener {
            val toEditor= Intent(this@NoteReaderActivity,NoteEditor::class.java)
            toEditor.putExtra("noteID", note.id.toString())
            startActivity(toEditor)
        }
    }

    private fun initColor(color: Int) {
        if (color != 0) {
            dataBinding.parent.setBackgroundColor(resources.getColor(color))
            dataBinding.navigation.setBackgroundColor(resources.getColor(color))
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(color)
        }
    }
}