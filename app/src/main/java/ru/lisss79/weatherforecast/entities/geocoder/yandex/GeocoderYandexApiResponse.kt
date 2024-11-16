package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class GeocoderYandexApiResponse (

  @SerializedName("response" ) var response : Response? = Response()

)