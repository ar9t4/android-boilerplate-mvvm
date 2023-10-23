package com.android.boilerplate.base.model.repository

import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.model.data.remote.user.User

/**
 * @author Abdul Rahman
 */
open class BaseRepositoryImp(preferences: Preferences) {

    private var user: User? = null

    init {
        user = preferences.getSignInUser()
    }

    fun getSignedInUser() = user

    fun getSignedInUserId() = user?.id ?: ""
}