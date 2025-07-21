package com.android.boilerplate.model.data.local.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.boilerplate.base.model.data.remote.response.BaseResponse
import com.google.gson.annotations.SerializedName

/**
 * @author Abdul Rahman
 */
@Entity
data class RandomUser(
    @PrimaryKey(autoGenerate = true)
    @SerializedName("uid")
    var id: Int? = null,
    @SerializedName("gender")
    var gender: String? = null,
    @Embedded
    @SerializedName("name")
    var name: Name? = null,
    @Embedded
    @SerializedName("location")
    var location: Location? = null,
    @SerializedName("email")
    var email: String? = null,
    @Embedded
    @SerializedName("dob")
    var dob: Dob? = null,
    @SerializedName("phone")
    var phone: String? = null,
    @SerializedName("cell")
    var cell: String? = null,
    @Embedded
    @SerializedName("picture")
    var picture: Picture? = null,
) : BaseResponse()

data class Name(
    @SerializedName("title")
    var title: String? = null,
    @SerializedName("first")
    var first: String? = null,
    @SerializedName("last")
    var last: String? = null
) : BaseResponse()

data class Location(
    @Embedded
    @SerializedName("street")
    var street: Street? = null,
    @SerializedName("city")
    var city: String? = null,
    @SerializedName("state")
    var state: String? = null,
    @SerializedName("country")
    var country: String? = null,
    @SerializedName("postcode")
    var postcode: String? = null,
    @Embedded
    @SerializedName("coordinates")
    var coordinates: Coordinates? = null
) : BaseResponse()

data class Street(
    @SerializedName("number")
    var number: Int? = null,
    @SerializedName("name")
    var name: String? = null
) : BaseResponse()

data class Coordinates(
    @SerializedName("latitude")
    var latitude: String? = null,
    @SerializedName("longitude")
    var longitude: String? = null
) : BaseResponse()

data class Dob(
    @SerializedName("date")
    var date: String? = null,
    @SerializedName("age")
    var age: Int? = null
) : BaseResponse()

data class Picture(
    @SerializedName("large")
    var large: String? = null,
    @SerializedName("medium")
    var medium: String? = null,
    @SerializedName("thumbnail")
    var thumbnail: String? = null
) : BaseResponse()