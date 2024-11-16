package ru.lisss79.weatherforecast.data.repositories.timezone

import ru.lisss79.weatherforecast.data.retrofit.TimeZoneApi
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.weather.WeatherException

class TimeZoneRepositoryTimeapi(val timeZoneApi: TimeZoneApi) : TimeZoneRepository {

    override suspend fun getTimeOffset(coords: Coords): Result<Int> {
        val response = kotlin.runCatching {
            timeZoneApi.getTimeZone(coords.latitude, coords.longitude)
        }
        val result = response.fold(
            onSuccess = {
                val currentOffset = it.body()?.currentUtcOffset?.seconds
                if (currentOffset != null) {
                    Result.success(currentOffset / 60)
                }
                else Result.failure(WeatherException("No info about place"))
            },
            onFailure = { Result.failure(it) }
        )
        return result
    }
}