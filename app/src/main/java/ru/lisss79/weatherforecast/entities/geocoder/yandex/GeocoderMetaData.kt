package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class GeocoderMetaData (

    @SerializedName("precision"      ) var precision      : String?         = null,
    @SerializedName("text"           ) var text           : String?         = null,
    @SerializedName("kind"           ) var kind           : String?         = null,
    @SerializedName("Address"        ) var Address        : ru.lisss79.weatherforecast.entities.geocoder.yandex.Address?        = ru.lisss79.weatherforecast.entities.geocoder.yandex.Address(),
    @SerializedName("AddressDetails" ) var AddressDetails : ru.lisss79.weatherforecast.entities.geocoder.yandex.AddressDetails? = ru.lisss79.weatherforecast.entities.geocoder.yandex.AddressDetails()

)