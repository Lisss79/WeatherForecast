package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class BoundedBy (

  @SerializedName("Envelope" ) var Envelope : ru.lisss79.weatherforecast.entities.geocoder.yandex.Envelope? = ru.lisss79.weatherforecast.entities.geocoder.yandex.Envelope()

)