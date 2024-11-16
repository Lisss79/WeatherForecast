package ru.lisss79.weatherforecast.entities.geocoder.yandex

import com.google.gson.annotations.SerializedName


data class GeoObject (

    @SerializedName("metaDataProperty" ) var metaDataProperty : ru.lisss79.weatherforecast.entities.geocoder.yandex.MetaDataProperty? = ru.lisss79.weatherforecast.entities.geocoder.yandex.MetaDataProperty(),
    @SerializedName("name"             ) var name             : String?           = null,
    @SerializedName("description"      ) var description      : String?           = null,
    @SerializedName("boundedBy"        ) var boundedBy        : ru.lisss79.weatherforecast.entities.geocoder.yandex.BoundedBy?        = ru.lisss79.weatherforecast.entities.geocoder.yandex.BoundedBy(),
    @SerializedName("uri"              ) var uri              : String?           = null,
    @SerializedName("Point"            ) var Point            : ru.lisss79.weatherforecast.entities.geocoder.yandex.Point?            = ru.lisss79.weatherforecast.entities.geocoder.yandex.Point()

)