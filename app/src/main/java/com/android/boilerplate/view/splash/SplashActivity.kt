package com.android.boilerplate.view.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.android.boilerplate.R
import com.android.boilerplate.aide.workers.PeriodicWorkerUtils
import com.android.boilerplate.base.view.BaseActivity
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.ActivitySplashBinding
import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.view.main.MainActivity
import com.android.boilerplate.view.setup.SetupActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : BaseActivity() {

    @Inject
    lateinit var preferences: Preferences
    private lateinit var binding: ActivitySplashBinding

    override fun getViewModel(): BaseViewModel? = null

    override fun hasConnectivity(connectivity: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
    }

    override fun onResume() {
        super.onResume()
        // set default settings preferences
        setDefaultPreferences()
        // launch setup or main activity
        launchNextActivity()
    }

    private fun setDefaultPreferences() {
        if (!preferences.contains(Preferences.KEY_NOTIFICATION)) {
            preferences.setBoolean(Preferences.KEY_NOTIFICATION, true)
            PeriodicWorkerUtils.createPeriodicWorker(applicationContext)
        }
    }

    private fun launchNextActivity() {
        if (!preferences.contains(Preferences.KEY_IS_APP_SETUP)) {
            startActivity(Intent(this@SplashActivity, SetupActivity::class.java))
        } else {
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
        }
        finish()
    }
}