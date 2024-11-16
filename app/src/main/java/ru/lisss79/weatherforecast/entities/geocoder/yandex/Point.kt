package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Point (

  @SerializedName("pos" ) var pos : String? = null

)