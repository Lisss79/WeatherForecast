package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class GeocoderResponseMetaData (

    @SerializedName("Point"   ) var Point   : ru.lisss79.weatherforecast.entities.geocoder.yandex.Point?  = ru.lisss79.weatherforecast.entities.geocoder.yandex.Point(),
    @SerializedName("request" ) var request : String? = null,
    @SerializedName("results" ) var results : String? = null,
    @SerializedName("found"   ) var found   : String? = null

)