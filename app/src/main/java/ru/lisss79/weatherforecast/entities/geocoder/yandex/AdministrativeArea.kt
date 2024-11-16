package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class AdministrativeArea (

    @SerializedName("AdministrativeAreaName" ) var AdministrativeAreaName : String?                  = null,
    @SerializedName("SubAdministrativeArea"  ) var SubAdministrativeArea  : ru.lisss79.weatherforecast.entities.geocoder.yandex.SubAdministrativeArea?   = null,
    @SerializedName("Locality"               ) var Locality               : ru.lisss79.weatherforecast.entities.geocoder.yandex.Locality?                = ru.lisss79.weatherforecast.entities.geocoder.yandex.Locality()

)