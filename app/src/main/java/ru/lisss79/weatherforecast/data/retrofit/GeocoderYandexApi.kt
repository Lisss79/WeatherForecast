package ru.lisss79.weatherforecast.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lisss79.weatherforecast.entities.geocoder.yandex.GeocoderYandexApiResponse

interface GeocoderYandexApi {

    @GET("1.x")
    suspend fun getAddress(
        @Query("apikey") apiKey: String,
        @Query("geocode") coords: String,
        @Query("sco") sco: String = "latlong",
        @Query("format") format: String = "json"
    ): Response<GeocoderYandexApiResponse>

    @GET("1.x")
    suspend fun getPlace(
        @Query("apikey") apiKey: String,
        @Query("geocode") name: String,
        @Query("results") results: Int = 5,
        @Query("format") format: String = "json"
    ): Response<GeocoderYandexApiResponse>

}