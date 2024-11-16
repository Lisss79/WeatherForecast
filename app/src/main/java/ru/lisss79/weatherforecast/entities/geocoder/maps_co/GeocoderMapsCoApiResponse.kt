package ru.lisss79.weatherforecast.entities.geocoder.maps_co

import com.google.gson.annotations.SerializedName
import com.huawei.hms.site.api.model.Coordinate


data class GeocoderMapsCoApiResponse(

    @SerializedName("place_id") var placeId: Long? = null,
    @SerializedName("licence") var licence: String? = null,
    @SerializedName("osm_type") var osmType: String? = null,
    @SerializedName("osm_id") var osmId: Long? = null,
    @SerializedName("lat") var lat: String? = null,
    @SerializedName("lon") var lon: String? = null,
    @SerializedName("display_name") var displayName: String? = null,
    @SerializedName("address") var address: Address? = Address(),
    @SerializedName("boundingbox") var boundingbox: ArrayList<String> = arrayListOf(),
    @SerializedName("error") var error: String? = null

) {
    fun getCoordinates(): Coordinate? {
        try {
            val latD = lat!!.toDouble()
            val lonD = lon!!.toDouble()
            return Coordinate(latD, lonD)
        }
        catch (exc: Exception) {
            return null
        }
    }
}