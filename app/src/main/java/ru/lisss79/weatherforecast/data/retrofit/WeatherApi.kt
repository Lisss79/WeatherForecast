package ru.lisss79.weatherforecast.data.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.lisss79.weatherforecast.entities.AUTO
import ru.lisss79.weatherforecast.entities.weather.WeatherData

interface WeatherApi {

    @GET("forecast")
    suspend fun getCurrentWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("timezone") tz: String = AUTO,
        @Query("current") currentRequest: String,
        @Query("wind_speed_unit") windSpeed: String = "ms"
    ): Response<WeatherData>

    @GET("forecast")
    suspend fun getCurrentAndHourlyWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("timezone") tz: String = AUTO,
        @Query("current") currentRequest: String,
        @Query("hourly") hourlyRequest: String,
        @Query("wind_speed_unit") windSpeed: String = "ms"
    ): Response<WeatherData>

    @GET("forecast")
    suspend fun getCurrentAndDailyWeather(
        @Query("latitude") lat: Double,
        @Query("longitude") lon: Double,
        @Query("timezone") tz: String = AUTO,
        @Query("current") currentRequest: String,
        @Query("daily") dailyRequest: String,
        @Query("forecast_days") forecastDays: Int = 16,
        @Query("wind_speed_unit") windSpeed: String = "ms"
    ): Response<WeatherData>

}