package ru.lisss79.weatherforecast.data.repositories.weather

import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

interface WeatherRepository {
    suspend fun getCurrentWeather(
        coords: Coords, currentRequest: String
    ): Result<UniversalWeatherState?>
    suspend fun getCurrentAndHourlyWeather(
        coords: Coords, currentRequest: String, hourlyRequest: String
    ): Result<List<UniversalWeatherState?>>
    suspend fun getCurrentAndDailyWeather(
        coords: Coords, currentRequest: String, dailyRequest: String
    ): Result<List<UniversalWeatherState?>>
}