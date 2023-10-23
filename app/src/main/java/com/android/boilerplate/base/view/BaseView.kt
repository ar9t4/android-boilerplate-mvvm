package com.android.boilerplate.base.view

import android.view.View
import android.view.Window
import android.widget.EditText

/**
 * @author Abdul Rahman
 */
interface BaseView {
    fun changeStatusBarColor(color: Int)
    fun resetStatusBarColor()
    fun hideSystemBars(hide: Boolean, window: Window?, view: View?)
    fun setSoftInputMode(mode: Int)
    fun resetSoftInputMode()
    fun showKeyboard(editText: EditText)
    fun hideKeyboard()

    /**
     * Global implementation is in BaseActivity but if you want to use customized screen based
     * Session Expiry Handling you can override this method in your activity or fragment
     */
    fun sessionExpire()

    /**
     * Global implementation is in BaseActivity but if you want to use customized screen based
     * Connectivity Handling you can override this method in your activity or fragment
     */
    fun noConnectivity()

    /**
     * Global implementation is in BaseActivity but if you want to use customized screen based
     * Loader Handling you can override this method in your activity or fragment
     */
    fun loaderVisibility(visibility: Boolean)

    /**
     * Global implementation is in BaseActivity but if you want to use customized screen based
     * Error Handling you can override this method in your activity or fragment
     */
    fun takeActionOnError(exception: Exception)

    /**
     * Global implementation is in BaseActivity but if you want to use customized Toast
     * you can override this method in your activity or fragment
     */
    fun showToast(message: String?)

    /**
     * Global implementation is in BaseActivity but if you want to use customized SnackBar
     * you can override this method in your activity or fragment
     */
    fun showSnackBar(view: View, message: String)
}