package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class GeoObjectCollection (

    @SerializedName("metaDataProperty" ) var metaDataProperty : ru.lisss79.weatherforecast.entities.geocoder.yandex.MetaDataProperty?        = ru.lisss79.weatherforecast.entities.geocoder.yandex.MetaDataProperty(),
    @SerializedName("featureMember"    ) var featureMember    : ArrayList<ru.lisss79.weatherforecast.entities.geocoder.yandex.FeatureMember> = arrayListOf()

)