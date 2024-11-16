package ru.lisss79.weatherforecast.entities.weather

import java.time.ZonedDateTime

data class CurrentWeatherState(
    val time: ZonedDateTime? = null,
    val temperature: Float? = null,
    val humidity: Float? = null,
    val windSpeed: Float? = null,
    val windDirection: Float? = null,
    val precipitation: Float? = null,
    val rain: Float? = null,
    val showers: Float? = null,
    val snowfall: Float? = null,
    val surfacePressure: Float? = null,
    val cloudCover: Float? = null,
    val weatherCode: Int? = null,
    val isDay: Boolean? = null
)
