package com.android.boilerplate.view.main

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.android.boilerplate.R
import com.android.boilerplate.aide.extensions.gone
import com.android.boilerplate.aide.extensions.visible
import com.android.boilerplate.base.view.BaseActivity
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author Abdul Rahman
 */
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun getViewModel(): BaseViewModel? = null

    override fun hasConnectivity(connectivity: Boolean) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.apply {
            // setup bottom nav with navigation ui
            navController = findNavController(R.id.nav_host_main)
            bottomNavMain.setupWithNavController(navController = navController)
            navController.addOnDestinationChangedListener(destinationChangedListener)
        }
    }

    override fun onDestroy() {
        navController.removeOnDestinationChangedListener(destinationChangedListener)
        super.onDestroy()
    }

    private val destinationChangedListener =
        NavController.OnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.dest_main, R.id.dest_settings, R.id.dest_feedback, R.id.dest_more -> {
                    bottomNavigationVisibility(show = true)
                }
                else -> {
                    bottomNavigationVisibility(show = false)
                }
            }
        }

    private fun bottomNavigationVisibility(show: Boolean) {
        binding.bottomNavMain.apply {
            if (show) visible() else gone()
        }
    }

    fun selectBottomNavigationTab(selectedItemId: Int? = null) {
        selectedItemId?.let {
            binding.bottomNavMain.selectedItemId = it
        }
    }
}