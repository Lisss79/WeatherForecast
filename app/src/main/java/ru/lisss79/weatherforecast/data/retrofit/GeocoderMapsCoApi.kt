package ru.lisss79.weatherforecast.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lisss79.weatherforecast.entities.geocoder.maps_co.GeocoderMapsCoApiResponse

interface GeocoderMapsCoApi {

        @GET("reverse")
        suspend fun getAddress(
            @Query("api_key") apiKey: String,
            @Query("lat") lat: Double,
            @Query("lon") lon: Double,
        ): Response<GeocoderMapsCoApiResponse>

        @GET("search")
        suspend fun getPlace(
            @Query("api_key") apiKey: String,
            @Query("q") name: String,
        ): Response<List<GeocoderMapsCoApiResponse>>

}