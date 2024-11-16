package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Components (

  @SerializedName("kind" ) var kind : String? = null,
  @SerializedName("name" ) var name : String? = null

)