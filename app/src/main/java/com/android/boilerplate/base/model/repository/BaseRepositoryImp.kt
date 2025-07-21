package com.android.boilerplate.base.model.repository

import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.model.data.remote.user.User

/**
 * @author Abdul Rahman
 */
open class BaseRepositoryImp(private val preferences: Preferences) {

    private var user: User? = null
    private var lastLocale: String? = null

    init {
        user = preferences.getSignInUser()
        lastLocale = preferences.getString(Preferences.KEY_LANG)
    }

    fun getSignedInUser() = user

    fun getSignedInUserId() = user?.id ?: ""

    fun hasLocaleChanged(): Boolean {
        val currentLocale = preferences.getString(Preferences.KEY_LANG)
        val result = lastLocale != currentLocale
        if (currentLocale != lastLocale) {
            lastLocale = currentLocale
        }
        return result
    }
}