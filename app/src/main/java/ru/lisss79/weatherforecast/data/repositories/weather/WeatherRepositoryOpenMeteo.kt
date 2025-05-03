package ru.lisss79.weatherforecast.data.repositories.weather

import ru.lisss79.weatherforecast.data.retrofit.WeatherApi
import ru.lisss79.weatherforecast.entities.AUTO
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.DataMappers
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

class WeatherRepositoryOpenMeteo(val weatherApi: WeatherApi) : WeatherRepository {

    override suspend fun getCurrentWeather(
        coords: Coords, currentRequest: String
    ): Result<UniversalWeatherState?> {
        val tz = AUTO
        val response = kotlin.runCatching {
            weatherApi.getCurrentWeather(
                lat = coords.latitude,
                lon = coords.longitude,
                tz = tz,
                currentRequest = currentRequest
            )
        }
        val result = response.fold(
            onSuccess = { Result.success(DataMappers.currentDataToState(it.body())) },
            onFailure = { Result.failure(it) }
        )
        return result
    }

    override suspend fun getCurrentAndHourlyWeather(
        coords: Coords, currentRequest: String, hourlyRequest: String
    ): Result<List<UniversalWeatherState?>> {
        val tz = AUTO
        val response = kotlin.runCatching {
            weatherApi.getCurrentAndHourlyWeather(
                lat = coords.latitude,
                lon = coords.longitude,
                currentRequest = currentRequest,
                hourlyRequest = hourlyRequest,
                tz = tz
            )
        }
        val result = response.fold(
            onSuccess = { Result.success(DataMappers.currentAndHourlyDataToState(it.body())) },
            onFailure = { Result.failure(it) }
        )
        return result
    }

    override suspend fun getCurrentAndDailyWeather(
        coords: Coords, currentRequest: String, dailyRequest: String
    ): Result<List<UniversalWeatherState?>> {
        val tz = AUTO
        val response = kotlin.runCatching {
            weatherApi.getCurrentAndDailyWeather(
                lat = coords.latitude,
                lon = coords.longitude,
                currentRequest = currentRequest,
                dailyRequest = dailyRequest,
                tz = tz
            )
        }
        val result = response.fold(
            onSuccess = { Result.success(DataMappers.currentAndDailyDataToState(it.body())) },
            onFailure = { Result.failure(it) }
        )
        return result
    }

}