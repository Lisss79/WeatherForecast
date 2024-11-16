package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Premise (

  @SerializedName("PremiseNumber" ) var PremiseNumber : String?     = null,
  @SerializedName("PostalCode"    ) var PostalCode    : ru.lisss79.weatherforecast.entities.geocoder.yandex.PostalCode? = ru.lisss79.weatherforecast.entities.geocoder.yandex.PostalCode()

)