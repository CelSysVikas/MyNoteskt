package me.vikas.mynoteskt.Util

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {

    private var appPreferences: SharedPreferences
    private var editor: SharedPreferences.Editor

    private val MY_PREFERENCE:String = "MyAppPreferences"
    private val lang = "lang"
    private val currentTheme = "isDark"


    private var preferenceInstance: Preferences? = null

    init {
        appPreferences=context.getSharedPreferences(MY_PREFERENCE,Context.MODE_PRIVATE)
        editor=appPreferences.edit()
    }

    @Synchronized
    fun getInstance(context: Context?): Preferences? {
        if (preferenceInstance == null) {
            preferenceInstance = context?.let { Preferences(it) }
        }
        return preferenceInstance
    }

    fun saveTheme(isDark:Boolean){
        editor.putBoolean(currentTheme,isDark)
        editor.apply()
    }

    fun getTheme():Boolean{
        return appPreferences.getBoolean(currentTheme,false)
    }

    fun saveLang(langCode:String){
        editor.putString(lang,langCode)
        editor.apply()
    }

    fun getLang(): String? {
        return appPreferences.getString(lang,"en")
    }

    fun reset(){
        editor.clear().apply()
    }
}