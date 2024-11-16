package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class MetaDataProperty (

  @SerializedName("GeocoderMetaData" ) var GeocoderMetaData : ru.lisss79.weatherforecast.entities.geocoder.yandex.GeocoderMetaData? = ru.lisss79.weatherforecast.entities.geocoder.yandex.GeocoderMetaData()

)