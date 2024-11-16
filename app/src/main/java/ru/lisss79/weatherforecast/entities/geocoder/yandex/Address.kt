package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Address (

  @SerializedName("country_code" ) var countryCode : String?               = null,
  @SerializedName("formatted"    ) var formatted   : String?               = null,
  @SerializedName("postal_code"  ) var postalCode  : String?               = null,
  @SerializedName("Components"   ) var Components  : ArrayList<Components> = arrayListOf()

)