package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class SubAdministrativeArea (

  @SerializedName("SubAdministrativeAreaName" ) var SubAdministrativeAreaName : String?   = null,
  @SerializedName("Locality"                  ) var Locality                  : ru.lisss79.weatherforecast.entities.geocoder.yandex.Locality? = ru.lisss79.weatherforecast.entities.geocoder.yandex.Locality()

)