package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class AddressDetails(

    @SerializedName("Country") var Country: Country? = Country()

)