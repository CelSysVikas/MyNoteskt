package me.vikas.mynoteskt.Activity

import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.vikas.mynoteskt.Util.Config
import me.vikas.mynoteskt.Model.Notes
import me.vikas.mynoteskt.R
import me.vikas.mynoteskt.ViewModel.NoteViewModel
import me.vikas.mynoteskt.databinding.ActivityNoteEditorBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoteEditor : AppCompatActivity() {

    private lateinit var dataBinding: ActivityNoteEditorBinding
    private val notesViewModel: NoteViewModel by viewModels()
    private var colorCode: Int? = null
    private var updateNote = false
    private var noteID: String? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_note_editor)

        initNavigation()

        intent.getStringExtra("noteID")?.let { id ->
            notesViewModel.notesRepo.getNoteLive(id).observe(this, Observer {
                updateNote = true
                noteID = id
                initEditor(it)
            })
        }

    }

    private fun initEditor(note: Notes) {
        note.colorCode?.let { initColor(note.colorCode) }
        dataBinding.noteTitle = note.title
        dataBinding.noteContent = note.content
    }

    private fun initColor(color: Int) {
        if (color != 0) {
            dataBinding.parent.setBackgroundColor(resources.getColor(color))
            dataBinding.toolbarNavigation.setBackgroundColor(resources.getColor(color))
            val window = window
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(color)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initNavigation() {
        dataBinding.toolbarNavigation.setNavigationOnClickListener { finish() }
        dataBinding.toolbarNavigation.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.saveNote -> {
                    if (!updateNote) {
                        if (!dataBinding.tvNoteTitle.text.toString().trim()
                                .isEmpty() || !dataBinding.tvNoteContent.text.toString().trim()
                                .isEmpty()
                        ) {
                            saveNote(
                                dataBinding.tvNoteTitle.text.toString(),
                                dataBinding.tvNoteContent.text.toString()
                            )
                        } else {
                            Config.makeToast(this, getString(R.string.error_empty_Note))
                        }
                    } else {
                        if (!dataBinding.tvNoteTitle.text.toString().trim()
                                .isEmpty() || !dataBinding.tvNoteContent.text.toString().trim()
                                .isEmpty()
                        ) {
                            updateNote(
                                dataBinding.tvNoteTitle.text.toString(),
                                dataBinding.tvNoteContent.text.toString()
                            )
                        } else {
                            Config.makeToast(this, getString(R.string.error_empty_Note))
                        }
                    }
                }

                R.id.discardNote -> {
                    MaterialAlertDialogBuilder(this).setTitle(getString(R.string.discardWarn))
                        .setMessage(getString(R.string.error_empty_NoteMessage))
                        .setPositiveButton(
                            getString(R.string.discardBtn),
                            DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                                finish()
                                Config.makeToast(this@NoteEditor, getString(R.string.noteDiscard))
                            })
                        .setNegativeButton(
                            getString(R.string.cancelbtn),
                            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                        .setCancelable(false)
                        .show()
                }
            }

            return@setOnMenuItemClickListener true
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveNote(title: String, content: String) {
        val newNote: Notes
        val currentDateTimeString =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))

        newNote = if (colorCode != null) {
            Notes(title, content, currentDateTimeString, colorCode!!)
        } else {
            Notes(title, content, currentDateTimeString)
        }
        Config.makeToast(this@NoteEditor, getString(R.string.noteCreated))
        notesViewModel.insertNote(newNote)
        finish()
    }

    private fun updateNote(title: String, content: String) {
        val updateNote = noteID?.let { notesViewModel.getNote(it) }
        if (updateNote != null) {
            updateNote.title = title
            updateNote.content = content
            if (this.colorCode != null) {
                updateNote.colorCode = this.colorCode!!
            }
        }
        updateNote?.let {
            notesViewModel.updateNote(it)
            Config.makeToast(this, getString(R.string.noteUpdated))
            finish()
        }

    }

    fun onClick(v: View) {
        when (v.id) {
            dataBinding.btColor1.id -> {
                colorCode = R.color.color1
                initColor(colorCode!!)
            }

            dataBinding.btColor2.id -> {
                colorCode = R.color.color2
                initColor(colorCode!!)
            }

            dataBinding.btColor3.id -> {
                colorCode = R.color.color3
                initColor(colorCode!!)
            }

            dataBinding.btColor4.id -> {
                colorCode = R.color.color4
                initColor(colorCode!!)
            }
        }
    }
}