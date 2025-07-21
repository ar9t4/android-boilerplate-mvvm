package com.android.boilerplate.model.data.remote.user

import android.os.Parcelable
import com.android.boilerplate.aide.extensions.capitalizeFirstChar
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

/**
 * @author Abdul Rahman
 */
@Parcelize
data class User(
    @SerializedName("_id")
    val id: String? = null,
    @SerializedName("userId")
    var userId: String? = null,
    @SerializedName("name")
    var name: String? = null,
    @SerializedName("email")
    var email: String? = null,
    @SerializedName("profilePic")
    var profilePic: String? = null,
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null,
    @SerializedName("userType")
    var userType: String? = UserType.User.value,
    @SerializedName("loginType")
    var loginType: String? = null,
    @SerializedName("accessToken")
    var accessToken: String? = null
) : Parcelable {

    fun getFormattedName(): String {
        return "${getFirstName()} ${getLastName()}"
    }

    fun getFirstName(): String {
        try {
            name?.let {
                return it.split(" ")[0].capitalizeFirstChar()
            } ?: return ""
        } catch (exception: Exception) {
            return ""
        }
    }

    private fun getLastName(): String {
        try {
            name?.let {
                return it.split(" ")[1].capitalizeFirstChar()
            } ?: return ""
        } catch (exception: Exception) {
            return ""
        }
    }

    fun getProvider(providerId: String): String {
        return providerId.split(".")[0]
    }
}

@Parcelize
enum class UserType(val value: String) : Parcelable {
    User("user")
}