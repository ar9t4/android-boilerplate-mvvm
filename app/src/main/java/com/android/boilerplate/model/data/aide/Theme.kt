package com.android.boilerplate.model.data.aide

import com.android.boilerplate.base.model.data.remote.response.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * @author Abdul Rahman
 */

data class Theme(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("selected")
    var selected: Boolean = false
) : BaseResponse()