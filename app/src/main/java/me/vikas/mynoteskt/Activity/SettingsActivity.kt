package me.vikas.mynoteskt.Activity

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import me.vikas.mynoteskt.R
import me.vikas.mynoteskt.Util.Config.changeAppTheme
import me.vikas.mynoteskt.Util.Config.changeLocale
import me.vikas.mynoteskt.Util.Preferences
import me.vikas.mynoteskt.ViewModel.NoteViewModel
import me.vikas.mynoteskt.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {

    private lateinit var dataBinding: ActivitySettingsBinding
    private lateinit var appPreferences: Preferences
    private val noteViewModel:NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        dataBinding.navigation.setNavigationOnClickListener{ finish() }

        appPreferences = Preferences(this).getInstance(this@SettingsActivity)!!

        appPreferences?.let { initData(it) }
        changeLang()
        changeTheme()
        deleteData()
    }

    private fun deleteData() {
        dataBinding.deleteData.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.deleteData))
                .setMessage(getString(R.string.deleteDataMessage))
                .setPositiveButton(getString(R.string.delete), DialogInterface.OnClickListener { dialog, which ->
                    noteViewModel.deleteData()
                    dialog.dismiss()
                })
                .setNegativeButton(getString(R.string.cancelbtn), DialogInterface.OnClickListener { dialog, which ->
                    dialog.dismiss()
                })
                .setCancelable(false)
                .show()
        }
    }

    private fun initData(preferences: Preferences) {

        dataBinding.currentLang = appPreferences.getLang()

        if (preferences.getTheme())
            dataBinding.currentTheme = getString(R.string.themeNight)
        else
            dataBinding.currentTheme = getString(R.string.themeDay)
    }

    private fun changeLang() {
        dataBinding.changeLang.setOnClickListener { v ->
            val language = arrayOf("English", "Hindi")
            val dialogBuilder = MaterialAlertDialogBuilder(this)
            dialogBuilder.setTitle("Pick Language")
            dialogBuilder.setItems(language) { dialog, which ->
                if (which == 0) {
                    changeLocale(this, "en")
                    appPreferences.saveLang("en")
                    dataBinding.currentLang = getString(R.string.english)
                    startActivity(Intent(this,MainActivity::class.java))
                }
                if (which == 1) {
                    changeLocale(this, "hi")
                    appPreferences.saveLang("hi")
                    dataBinding.currentLang = getString(R.string.hindi)
                    startActivity(Intent(this,MainActivity::class.java))
                }
            }
            dialogBuilder.show()
        }
    }

    private fun changeTheme() {
        dataBinding.changeTheme.setOnClickListener { v ->
            val theme = arrayOf("Day", "Night")
            val dialogBuilder = MaterialAlertDialogBuilder(this)
            dialogBuilder.setTitle("Pick Language")
            dialogBuilder.setItems(
                theme
            ) { dialog, which ->
                if (which == 0) {
                    changeAppTheme(this, false)
                    appPreferences.saveTheme(false)
                }
                if (which == 1) {
                    changeAppTheme(this, true)
                    appPreferences.saveTheme(true)
                }
            }
            dialogBuilder.show()
        }
    }
}