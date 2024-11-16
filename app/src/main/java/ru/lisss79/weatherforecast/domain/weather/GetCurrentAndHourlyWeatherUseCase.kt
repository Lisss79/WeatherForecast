package ru.lisss79.weatherforecast.domain.weather

import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState


class GetCurrentAndHourlyWeatherUseCase(weatherRepository: WeatherRepository) :
    WeatherUseCase(weatherRepository)
{

    suspend operator fun invoke(
        coords: Coords,
        currentRequest: List<String>,
        hourlyRequest: List<String>
    ): Result<List<UniversalWeatherState?>?> {
        val curString = currentRequest.joinToString(separator = ",")
        val hourlyString = hourlyRequest.joinToString(separator = ",")
        return weatherRepository.getCurrentAndHourlyWeather(coords, curString, hourlyString)
    }

}