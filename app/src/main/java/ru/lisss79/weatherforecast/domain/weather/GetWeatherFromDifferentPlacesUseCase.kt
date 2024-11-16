package ru.lisss79.weatherforecast.domain.weather

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.entities.weather.WeatherException

class GetWeatherFromDifferentPlacesUseCase(weatherRepository: WeatherRepository) :
    WeatherUseCase(weatherRepository)
{

    suspend operator fun invoke(
        sites: Set<Site>,
        currentRequest: List<String>,
        selectedPlace: Int = Values.selectedPlaceDefault
    ): Result<List<UniversalWeatherState?>> {
        val currentString = currentRequest.joinToString(separator = ",")
        val states = mutableListOf<UniversalWeatherState>()
        var error = false
        for (site in sites) {
            val coords = site.location.run { Coords(lat, lng) }
            val localResult = weatherRepository.getCurrentWeather(coords, currentString).getOrNull()
            if (localResult != null) states.add(localResult)
            else error = true
        }
        return if (!error) Result.success(states)
        else Result.failure(WeatherException("Can't get weather data"))
    }
}