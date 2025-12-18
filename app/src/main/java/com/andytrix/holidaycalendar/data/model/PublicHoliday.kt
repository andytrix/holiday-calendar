package com.andytrix.holidaycalendar.data.model

import com.google.gson.annotations.SerializedName

data class PublicHoliday(
    @SerializedName("date")
    val date: String,

    @SerializedName("localName")
    val localName: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("countryCode")
    val countryCode: String? = null,

    @SerializedName("fixed")
    val fixed: Boolean? = null,

    @SerializedName("global")
    val global: Boolean? = null,

    @SerializedName("counties")
    val counties: List<String>? = null,

    @SerializedName("launchYear")
    val launchYear: Int? = null,

    @SerializedName("types")
    val types: List<String>? = null
)
