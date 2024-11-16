package ru.lisss79.weatherforecast.entities.timezone

import com.google.gson.annotations.SerializedName

data class CurrentUtcOffset (
    @SerializedName("seconds"      ) var seconds      : Int? = null,
    @SerializedName("milliseconds" ) var milliseconds : Long? = null,
    @SerializedName("ticks"        ) var ticks        : Long? = null,
    @SerializedName("nanoseconds"  ) var nanoseconds  : Long? = null
)
