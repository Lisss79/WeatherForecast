package ru.lisss79.weatherforecast.entities.weather

import android.location.Location

class WeatherException(
    val exceptionMessage: String = "",
    val universalWeatherState: UniversalWeatherState? = null,
    val location: Location? = null
): Exception(exceptionMessage) {
}