package com.android.boilerplate.base.view

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.android.boilerplate.R
import com.android.boilerplate.aide.utils.DialogUtils
import com.android.boilerplate.base.model.data.remote.response.Result
import com.android.boilerplate.base.viewmodel.BaseViewModel
import com.android.boilerplate.model.data.local.preference.Preferences
import com.google.android.material.snackbar.Snackbar
import retrofit2.HttpException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import java.util.concurrent.TimeoutException

/**
 * @author Abdul Rahman
 */
@SuppressLint("Registered")
abstract class BaseActivity : AppCompatActivity(), BaseView {

    private var statusBarColor: Int = Color.WHITE
    private lateinit var dialog: Dialog
    private var availableNetwork: Network? = null
    private var originalSoftInputMode: Int? = null
    private lateinit var preferences: Preferences
    private lateinit var inputManager: InputMethodManager
    private lateinit var connectivityManager: ConnectivityManager

    abstract fun getViewModel(): BaseViewModel?

    abstract fun hasConnectivity(connectivity: Boolean)

    /**
     * @param newBase the default base context of the application
     * @return overridden configuration with user's selected night mode and language preferences
     */
    private fun getOverrideConfiguration(newBase: Context?): Configuration {
        val configuration = Configuration(newBase?.resources?.configuration)
        val appName = applicationInfo.loadLabel(packageManager).toString()
        val sharedPreferences = getSharedPreferences(appName, Context.MODE_PRIVATE)
        val uiMode = sharedPreferences.getInt(
            Preferences.KEY_THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        )
        AppCompatDelegate.setDefaultNightMode(uiMode)
        sharedPreferences.getString(Preferences.KEY_LANG, Preferences.KEY_DEFAULT)?.let {
            if (it != Preferences.KEY_DEFAULT) {
                configuration.setLocale(Locale(it))
            }
        }
        return configuration
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        applyOverrideConfiguration(getOverrideConfiguration(newBase))
    }

    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
        super.applyOverrideConfiguration(overrideConfiguration)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferences = Preferences(applicationContext)
        dialog = DialogUtils.createProgressDialog(this, false)
        inputManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        getViewModel()?.let { viewModel ->
            viewModel.event.observe(this) {
                when (it) {
                    is Result.Loading<*> -> {
                        loaderVisibility(visibility = it.loading as Boolean)
                    }

                    is Result.Failure<*> -> {
                        loaderVisibility(visibility = false)
                        takeActionOnError(exception = it.exception as Exception)
                    }

                    else -> {}
                }
            }
        }
        registerNetworkCallback()
    }

    override fun onDestroy() {
        unregisterNetworkCallback()
        super.onDestroy()
    }

    /**
     * Change Status Bar color
     */
    override fun changeStatusBarColor(color: Int) {
        statusBarColor = window.statusBarColor
        window.statusBarColor = ContextCompat.getColor(this, color)
    }

    /**
     * Reset Status Bar color
     */
    override fun resetStatusBarColor() {
        window.statusBarColor = statusBarColor
    }

    /**
     * Show/Hide system bars: status bar and navigation bar
     */
    override fun hideSystemBars(hide: Boolean, window: Window?, view: View?) {
        if (window != null && view != null) {
            WindowCompat.setDecorFitsSystemWindows(window, true)
            WindowInsetsControllerCompat(window, view).let { controller ->
                if (hide) {
                    controller.hide(WindowInsetsCompat.Type.systemBars())
                } else {
                    controller.show(WindowInsetsCompat.Type.systemBars())
                }
                controller.systemBarsBehavior =
                    WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }

    /**
     * Set soft input mode
     */
    override fun setSoftInputMode(mode: Int) {
        originalSoftInputMode = window.attributes.softInputMode
        window.setSoftInputMode(mode)
    }

    /**
     * Reset soft input mode
     */
    override fun resetSoftInputMode() {
        originalSoftInputMode?.let {
            window.setSoftInputMode(it)
        }
    }

    /**
     * Show system Keyboard
     */
    override fun showKeyboard(editText: EditText) {
        editText.post {
            editText.requestFocus()
            inputManager.showSoftInput(editText.rootView, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    /**
     * Hide system Keyboard
     */
    override fun hideKeyboard() {
        this.currentFocus?.let {
            inputManager.hideSoftInputFromWindow(it.applicationWindowToken, 0)
        }
    }

    override fun sessionExpire() {
        showToast(getString(R.string.error_session_expired))
    }

    override fun noConnectivity() {
        showToast(getString(R.string.error_no_internet_connectivity))
    }

    override fun loaderVisibility(visibility: Boolean) {
        if (::dialog.isInitialized) {
            if (visibility) {
                if (!dialog.isShowing)
                    dialog.show()
            } else {
                dialog.dismiss()
            }
        }
    }

    override fun takeActionOnError(exception: Exception) {
        when (exception) {
            is TimeoutException, is SocketTimeoutException -> {
                showToast(getString(R.string.error_request_time_out))
            }
            is UnknownHostException -> {
                showToast(getString(R.string.error_no_internet_connectivity))
            }
            is HttpException -> {
                when (exception.code()) {
                    401 -> {
                        // unauthorized: take user to auth flow
                        showToast(getString(R.string.error_session_expired))
                        preferences.clear()
                        //startActivity(Intent(this, AuthActivity::class.java))
                        finishAffinity()
                    }
                    502 -> {
                        showToast(getString(R.string.error_something_went_wrong))
                    }
                }
            }
            else -> {
                showToast(getString(R.string.error_something_went_wrong))
            }
        }
    }

    /**
     * Show system Toast
     */
    override fun showToast(message: String?) {
        message?.let {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Show system SnackBar
     */
    override fun showSnackBar(view: View, message: String) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            availableNetwork = network
            runOnUiThread { hasConnectivity(true) }
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            if (network == availableNetwork) {
                runOnUiThread { hasConnectivity(false) }
            }
        }
    }

    private fun registerNetworkCallback() {
        val networkRequest = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }
}