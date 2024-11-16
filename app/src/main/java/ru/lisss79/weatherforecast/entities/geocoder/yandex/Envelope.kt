package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Envelope (

  @SerializedName("lowerCorner" ) var lowerCorner : String? = null,
  @SerializedName("upperCorner" ) var upperCorner : String? = null

)