package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Thoroughfare (

  @SerializedName("ThoroughfareName" ) var ThoroughfareName : String?  = null,
  @SerializedName("Premise"          ) var Premise          : ru.lisss79.weatherforecast.entities.geocoder.yandex.Premise? = ru.lisss79.weatherforecast.entities.geocoder.yandex.Premise()

)