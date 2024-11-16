package ru.lisss79.weatherforecast.entities.timezone

import com.google.gson.annotations.SerializedName

data class TimeZoneResponse (
    @SerializedName("timeZone"         ) var timeZone         : String?           = null,
    @SerializedName("currentLocalTime" ) var currentLocalTime : String?           = null,
    @SerializedName("currentUtcOffset" ) var currentUtcOffset : CurrentUtcOffset? = CurrentUtcOffset()
)