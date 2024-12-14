package com.namseox.st086_spranki_music.utils

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class SharedPreferenceUtils @Inject constructor(context: Context) {
    private val MYAPPLICATION = "MY_APPLICATION"
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(MYAPPLICATION, Context.MODE_PRIVATE)


    fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }


    //check number name playlist
    fun putNumber(key: String, value : Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value).apply()
    }

    fun getNumber(key: String): Int {
        return sharedPreferences.getInt(key,0)
    }

    //
    fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }
    companion object : SingletonHolder<SharedPreferenceUtils, Context>(::SharedPreferenceUtils)

}
