package ru.lisss79.weatherforecast.entities.weather

import java.time.LocalDate
import java.time.LocalTime

data class DailyWeatherState(
    val time: LocalDate? = null,
    val temperatureMin: Float? = null,
    val temperatureMax: Float? = null,
    val windSpeedMax: Float? = null,
    val windGustsMax: Float? = null,
    val windDirectionDominant: Float? = null,
    val precipitationSum: Float? = null,
    val precipitationProbability: Float? = null,
    val rainSum: Float? = null,
    val showersSum: Float? = null,
    val snowfallSum: Float? = null,
    val weatherCode: Int? = null,
    val sunrise: LocalTime? = null,
    val sunset: LocalTime? = null,
)
