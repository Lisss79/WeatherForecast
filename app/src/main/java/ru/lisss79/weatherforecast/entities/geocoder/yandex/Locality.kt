package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Locality (

  @SerializedName("LocalityName" ) var LocalityName : String?       = null,
  @SerializedName("Thoroughfare" ) var Thoroughfare : ru.lisss79.weatherforecast.entities.geocoder.yandex.Thoroughfare? = ru.lisss79.weatherforecast.entities.geocoder.yandex.Thoroughfare()

)