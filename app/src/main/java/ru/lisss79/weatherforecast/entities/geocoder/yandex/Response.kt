package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class Response (

  @SerializedName("GeoObjectCollection" ) var GeoObjectCollection : ru.lisss79.weatherforecast.entities.geocoder.yandex.GeoObjectCollection? = ru.lisss79.weatherforecast.entities.geocoder.yandex.GeoObjectCollection()

)