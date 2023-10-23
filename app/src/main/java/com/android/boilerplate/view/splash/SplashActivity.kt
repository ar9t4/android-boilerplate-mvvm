package com.android.boilerplate.view.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.Lifecycle
import com.android.boilerplate.R
import com.android.boilerplate.aide.workers.PeriodicWorkerUtils
import com.android.boilerplate.base.view.BaseActivity
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun getViewModel(): BaseViewModel? = null

    override fun hasConnectivity(connectivity: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        Handler(Looper.getMainLooper()).postDelayed({
            if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
                setDefaultPreferences()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 2000)
    }

    private fun setDefaultPreferences() {
        if (!preferences.contains(Preferences.KEY_NOTIFICATION)) {
            preferences.setBoolean(Preferences.KEY_NOTIFICATION, true)
            PeriodicWorkerUtils.createPeriodicWorker(applicationContext)
        }
    }
}