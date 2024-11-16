package ru.lisss79.weatherforecast.domain.weather

import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState


class GetCurrentAndDailyWeatherUseCase(weatherRepository: WeatherRepository) :
    WeatherUseCase(weatherRepository)
{

    suspend operator fun invoke(
        coords: Coords,
        currentRequest: List<String>,
        dailyRequest: List<String>
    ): Result<List<UniversalWeatherState?>> {
        val curString = currentRequest.joinToString(separator = ",")
        val dailyString = dailyRequest.joinToString(separator = ",")
        return weatherRepository.getCurrentAndDailyWeather(coords, curString, dailyString)
    }

}