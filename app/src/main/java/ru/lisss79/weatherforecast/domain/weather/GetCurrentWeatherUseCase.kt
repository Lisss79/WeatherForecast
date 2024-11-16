package ru.lisss79.weatherforecast.domain.weather

import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

class GetCurrentWeatherUseCase(weatherRepository: WeatherRepository) :
    WeatherUseCase(weatherRepository)
{

    suspend operator fun invoke(
        coords: Coords,
        currentRequest: List<String>
    ): Result<UniversalWeatherState?> {
        val currentString = currentRequest.joinToString(separator = ",")
        return weatherRepository.getCurrentWeather(coords, currentString)
    }
}