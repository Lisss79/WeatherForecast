package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class PostalCode (

  @SerializedName("PostalCodeNumber" ) var PostalCodeNumber : String? = null

)