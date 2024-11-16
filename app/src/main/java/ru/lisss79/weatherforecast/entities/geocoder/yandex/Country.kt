package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Country (

  @SerializedName("AddressLine"        ) var AddressLine        : String?             = null,
  @SerializedName("CountryNameCode"    ) var CountryNameCode    : String?             = null,
  @SerializedName("CountryName"        ) var CountryName        : String?             = null,
  @SerializedName("AdministrativeArea" ) var AdministrativeArea : ru.lisss79.weatherforecast.entities.geocoder.yandex.AdministrativeArea? = ru.lisss79.weatherforecast.entities.geocoder.yandex.AdministrativeArea()

)