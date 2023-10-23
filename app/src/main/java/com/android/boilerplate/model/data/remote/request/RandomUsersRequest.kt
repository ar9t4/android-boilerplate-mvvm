package com.android.boilerplate.model.data.remote.request

import android.os.Parcelable
import com.android.boilerplate.base.model.data.remote.request.BaseRequest
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Abdul Rahman
 */
@Parcelize
data class RandomUsersRequest(
    @SerializedName("results")
    var results: Int? = null
) : Parcelable, BaseRequest()