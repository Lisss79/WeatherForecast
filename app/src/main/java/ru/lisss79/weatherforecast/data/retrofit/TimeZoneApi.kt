package ru.lisss79.weatherforecast.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lisss79.weatherforecast.entities.timezone.TimeZoneResponse

interface TimeZoneApi {

    @GET("coordinate")
    suspend fun getTimeZone(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
    ): Response<TimeZoneResponse>

}