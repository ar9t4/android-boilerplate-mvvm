package com.android.boilerplate.model.data.local.preference

import android.content.Context
import androidx.core.content.edit
import com.android.boilerplate.R
import com.android.boilerplate.model.data.remote.user.User
import com.google.gson.Gson
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author Abdul Rahman
 */
@Singleton
class Preferences @Inject constructor(@ApplicationContext private val context: Context) {

    private val gson = Gson()
    private val sharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)

    fun clear() {
        sharedPreferences.edit { clear() }
    }

    fun contains(key: String): Boolean = sharedPreferences.contains(key)

    fun setString(key: String, value: String?) {
        sharedPreferences.edit { putString(key, value) }
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun setInt(key: String, value: Int) {
        sharedPreferences.edit { putInt(key, value) }
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, -1)
    }

    fun setBoolean(key: String, value: Boolean) {
        sharedPreferences.edit { putBoolean(key, value) }
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun setFloat(key: String, value: Float) {
        sharedPreferences.edit { putFloat(key, value) }
    }

    fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, -1.0F)
    }

    fun setLong(key: String, value: Long) {
        sharedPreferences.edit { putLong(key, value) }
    }

    fun getLong(key: String): Long {
        return sharedPreferences.getLong(key, -1L)
    }

    fun setSignInUser(user: User) {
        sharedPreferences.edit { putString("user", gson.toJson(user)) }
    }

    fun getSignInUser(): User? {
        val user = sharedPreferences.getString("user", null)
        return gson.fromJson(user, User::class.java)
    }

    companion object {
        const val KEY_DEFAULT = "default"
        const val KEY_NOTIFICATION = "notification"
        const val KEY_THEME = "theme"
        const val KEY_LANG = "lang"
        const val KEY_IS_APP_SETUP = "is_app_setup"
    }
}