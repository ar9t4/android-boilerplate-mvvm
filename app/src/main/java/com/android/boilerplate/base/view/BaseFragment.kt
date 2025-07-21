package com.android.boilerplate.base.view

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.EditText
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.android.boilerplate.base.model.data.remote.response.Result
import com.android.boilerplate.base.viewmodel.BaseViewModel

/**
 * @author Abdul Rahman
 */
abstract class BaseFragment : Fragment(), BaseView {

    private var activity: BaseActivity? = null

    abstract fun getViewModel(): BaseViewModel?

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity) {
            activity = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()
        getViewModel()?.let { viewModel ->
            viewModel.event.observe(viewLifecycleOwner) {
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