package com.android.boilerplate.base.model.data.remote.response

/**
 * @author Abdul Rahman
 */
sealed class Result<T>(
    val loading: Boolean? = null,
    val data: T? = null,
    val exception: Exception? = null
) {
    class Loading<T>(data: T? = null, loading: Boolean? = null) :
        Result<T>(data = data, loading = loading)

    class Success<T>(data: T? = null) : Result<T>(data = data)
    class Failure<T>(data: T? = null, exception: Exception? = null) :
        Result<T>(data = data, exception = exception)
}