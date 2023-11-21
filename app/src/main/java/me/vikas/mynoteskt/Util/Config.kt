package me.vikas.mynoteskt.Util

import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

object Config {
    const val TABLE_NAME = "my_notes"
    const val COLUMN_ID = "id"
    const val COLUMN_TITLE = "title"
    const val COLUMN_CONTENT = "content"
    const val COLUMN_DATE_TIME = "date_time"
    const val COLUMN_COLOR_CODE = "color_code"
    const val COLUMN_NOTE_PINNED = "pinned_note"

    fun makeToast(context: Context,message:String){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show()
    }

    fun changeLocale(activity: Activity, langCode: String?) {
        if (langCode != null) {
            val locale = Locale(langCode)
            Locale.setDefault(locale)

            val resources = activity.resources
            val configuration = resources.configuration
            configuration.setLocale(locale)

            resources.updateConfiguration(configuration, resources.displayMetrics)
        }
    }

    fun changeAppTheme(activity: Activity, isDark: Boolean) {
        if (isDark) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
}