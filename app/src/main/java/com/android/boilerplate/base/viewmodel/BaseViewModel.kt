package com.android.boilerplate.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.boilerplate.base.model.data.remote.response.Result

/**
 * @author Abdul Rahman
 */
open class BaseViewModel : ViewModel() {

    val event = MutableLiveData<Result<Any>>()

    /**
     * Used for Global implementation in BaseActivity but if you want to use customized screen based
     * Loader Handling you can override loaderVisibility method in your activity or fragment
     */
    fun showLoader(show: Boolean) {
        event.postValue(Result.Loading(loading = show))
    }

    /**
     * Used for Global implementation in BaseActivity but if you want to use customized screen based
     * Error Handling you can override takeActionOnError method in your activity or fragment
     */
    fun handleException(exception: Exception) {
        showLoader(show = false)
        takeActionOnError(exception = exception)
    }

    private fun takeActionOnError(exception: Exception) {
        event.postValue(Result.Failure(exception = exception))
    }
}