package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class FeatureMember (

  @SerializedName("GeoObject" ) var GeoObject : ru.lisss79.weatherforecast.entities.geocoder.yandex.GeoObject? = ru.lisss79.weatherforecast.entities.geocoder.yandex.GeoObject()

)