package com.android.boilerplate.model.data.aide

import com.android.boilerplate.base.model.data.remote.response.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * @author Abdul Rahman
 */

data class SettingItem(
    @SerializedName("id")
    val id: Int,
    @SerializedName("key")
    val key: String,
    @SerializedName("value")
    var value: String
) : BaseResponse()