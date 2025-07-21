package com.android.boilerplate.view.setup

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import com.android.boilerplate.R
import com.android.boilerplate.base.view.BaseActivity
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.ActivitySetupBinding
import com.android.boilerplate.model.data.local.preference.Preferences
import com.android.boilerplate.view.main.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class SetupActivity : BaseActivity() {

    @Inject
    lateinit var preferences: Preferences
    private lateinit var navController: NavController
    private lateinit var binding: ActivitySetupBinding

    override fun getViewModel(): BaseViewModel? = null

    override fun hasConnectivity(connectivity: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup)
        navController = findNavController(R.id.nav_host_setup)
        navController.addOnDestinationChangedListener(navDestinationListener)
    }

    override fun onStart() {
        super.onStart()
        binding.btnNext.setOnClickListener { onNextClicked() }
    }

    override fun onDestroy() {
        super.onDestroy()
        navController.removeOnDestinationChangedListener(navDestinationListener)
    }

    private val navDestinationListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.dest_readyToUse -> binding.btnNext.text = getString(R.string.str_continue)
                else -> binding.btnNext.text = getString(R.string.next)
            }
        }

    private fun onNextClicked() {
        val navOptions = NavOptions.Builder()
            .setEnterAnim(R.anim.slide_in_from_right)
            .setPopExitAnim(R.anim.slide_out_from_left)
            .build()
        val currentDestinationId = navController.currentDestination?.id
        when (currentDestinationId) {
            R.id.dest_languages -> navController.navigate(
                resId = R.id.dest_themes, args = null, navOptions = navOptions
            )

            R.id.dest_themes -> {
                navController.navigate(
                    resId = R.id.dest_readyToUse, args = null, navOptions = navOptions
                )
                lifecycleScope.launch {
                    binding.btnNext.apply {
                        alpha = 0.25f
                        isEnabled = false
                    }
                    delay(2000)
                    binding.btnNext.apply {
                        alpha = 1f
                        isEnabled = true
                    }
                }
            }

            else -> launchMainActivity()
        }
    }

    private fun launchMainActivity() {
        preferences.setBoolean(Preferences.KEY_IS_APP_SETUP, true)
        startActivity(Intent(this@SetupActivity, MainActivity::class.java))
        finish()
    }
}