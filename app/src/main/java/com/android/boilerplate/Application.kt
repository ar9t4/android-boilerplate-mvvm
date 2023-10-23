package com.android.boilerplate

import dagger.hilt.android.HiltAndroidApp

/**
 * @author Abdul Rahman
 */
@HiltAndroidApp
class Application : android.app.Application() {

    override fun onCreate() {
        super.onCreate()
    }
}