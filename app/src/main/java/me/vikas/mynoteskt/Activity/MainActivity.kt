package me.vikas.mynoteskt.Activity

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.vikas.mynoteskt.Adapter.NotesAdapter
import me.vikas.mynoteskt.Interface.ItemNoteClickEvents
import me.vikas.mynoteskt.Model.Notes
import me.vikas.mynoteskt.R
import me.vikas.mynoteskt.Util.Config
import me.vikas.mynoteskt.Util.Preferences
import me.vikas.mynoteskt.ViewModel.NoteViewModel
import me.vikas.mynoteskt.databinding.ActivityMainBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Suppress("UNCHECKED_CAST")
class MainActivity : AppCompatActivity(), ItemNoteClickEvents {

    private lateinit var dataBinding: ActivityMainBinding
    private lateinit var appPreferences: Preferences
    private val noteViewModel: NoteViewModel by viewModels()
    private var isExposed:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        dataBinding = DataBindingUtil.setContentView(this@MainActivity, R.layout.activity_main)
        appPreferences = Preferences(this).getInstance(this)!!
        isExposed=false

        initNavigation()
        initPrefrences()
        initSearch()

        noteViewModel.getAllNotes().observe(this, Observer {
            if (!it.isEmpty()) {
                dataBinding.noteDataView.visibility=View.GONE
                dataBinding.rvNotes.visibility=View.VISIBLE

                dataBinding.rvNotes.addItemDecoration(
                    DividerItemDecoration(
                        this,
                        DividerItemDecoration.VERTICAL
                    )
                )
                dataBinding.rvNotes.adapter = NotesAdapter(this, this, it)
                dataBinding.rvNotes.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            }else{
                dataBinding.rvNotes.visibility=View.GONE
                dataBinding.noteDataView.visibility=View.VISIBLE

            }
        })

        dataBinding.fabNoteEditor.setOnClickListener {
            startActivity(
                Intent(
                    this@MainActivity,
                    NoteEditor::class.java
                )
            )
        }
    }

    private fun initSearch() {
        dataBinding.ibSearch.setOnClickListener {
            if (!isExposed) {
                dataBinding.textField.visibility = View.VISIBLE
                dataBinding.ibSearch.setImageDrawable(getDrawable(R.drawable.ic_close))
                isExposed=true
            }else{
                dataBinding.textField.visibility = View.INVISIBLE
                dataBinding.ibSearch.setImageDrawable(getDrawable(R.drawable.ic_search))
                isExposed=false
            }
        }

        dataBinding.searchQuery.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {
            searchNote(charSequence.toString())
            }

            override fun afterTextChanged(editable: Editable?) {}
        })
    }

    private fun searchNote(query:String){
       var list: List<Notes?>? = noteViewModel.searchNote("%$query%")
        dataBinding.rvNotes.adapter=NotesAdapter(this,this, list as List<Notes>)
    }

    private fun initPrefrences() {
        appPreferences?.let {
            Config.changeLocale(this, appPreferences.getLang())
            Config.changeAppTheme(this, appPreferences.getTheme())
        }
    }

    private fun initNavigation() {
        dataBinding.ibSettings.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    SettingsActivity::class.java
                )
            )
        }
    }

    override fun onItemClick(position: Int, noteId: Int) {
        var toReader = Intent(this, NoteReaderActivity::class.java)
        toReader.putExtra("noteID", noteId.toString())
        startActivity(toReader)
    }

    override fun onMoreTextClick(position: Int, exposeLayout: LinearLayout, tvInfo: TextView) {
        if (isExposed) {
            exposeLayout.visibility = View.GONE
            tvInfo.text = getString(R.string.showOptions)
            dataBinding.rvNotes.adapter?.notifyItemChanged(position)
            isExposed = false
        } else {
            exposeLayout.visibility = View.VISIBLE
            tvInfo.text = getString(R.string.hideOptions)
            dataBinding.rvNotes.adapter?.notifyItemChanged(position)
            isExposed = true
        }
    }

    override fun onPinBtnClick(position: Int, note: Notes) {
        note.isPinned = !note.isPinned
        noteViewModel.updateNote(note)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCopyBtnClick(position: Int, note: Notes) {
        val currentDateTimeString =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"))
        noteViewModel.insertNote(
            note.copy(
                dateTime = currentDateTimeString,
                id = 0,
                title = "copied: ${note.title}"
            )
        )
    }

    @SuppressLint("ResourceType")
    override fun onDeleteBtnClick(position: Int, note: Notes) {
        MaterialAlertDialogBuilder(this).setTitle(getString(R.string.noteDiscard))
            .setMessage(getString(R.string.discardWarn))
            .setCancelable(false)
            .setPositiveButton(
                getString(R.string.discardBtn),
                DialogInterface.OnClickListener { dialog, which ->
                    noteViewModel.deleteNote(note)
                    Config.makeToast(this, getString(R.string.noteDiscard))
                })
            .setNegativeButton(
                getString(R.string.cancelbtn),
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            .show()
    }

    override fun onEditBtnClick(position: Int, note: Notes) {
        val toEditor = Intent(this@MainActivity, NoteEditor::class.java)
        toEditor.putExtra("noteID", note.id.toString())

        startActivity(toEditor)
    }
}