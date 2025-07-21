package com.android.boilerplate.base.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.DialogFragment

/**
 * @author Abdul Rahman
 */
abstract class BaseDialogFragment : DialogFragment(), BaseView {

    private var activity: BaseActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            activity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
    }

    override fun hideSystemBars(hide: Boolean, window: Window?, view: View?) {
        activity?.hideSystemBars(hide, window, view)
    }

    override fun setSoftInputMode(mode: Int) {
        activity?.setSoftInputMode(mode)
    }

    override fun resetSoftInputMode() {
        activity?.resetSoftInputMode()
    }

    override fun showKeyboard(editText: EditText) {
        activity?.showKeyboard(editText)
    }

    override fun hideKeyboard() {
        activity?.hideKeyboard()
    }

    override fun sessionExpire() {
        activity?.sessionExpire()
    }

    override fun noConnectivity() {
        activity?.noConnectivity()
    }

    override fun loaderVisibility(visibility: Boolean) {
        activity?.loaderVisibility(visibility)
    }

    override fun takeActionOnError(exception: Exception) {
        activity?.takeActionOnError(exception)
    }

    override fun showToast(message: String?) {
        activity?.showToast(message)
    }

    override fun showSnackBar(view: View, message: String) {
        activity?.showSnackBar(view, message)
    }

    fun callBackKeyHandling(function: () -> Unit) {
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    function()
                }
            }
        activity?.onBackPressedDispatcher?.addCallback(this, callback)
    }
}