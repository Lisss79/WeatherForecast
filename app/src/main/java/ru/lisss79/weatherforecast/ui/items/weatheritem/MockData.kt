package ru.lisss79.weatherforecast.ui.items.weatheritem

import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.weather.CurrentWeatherState
import ru.lisss79.weatherforecast.entities.weather.DailyWeatherState
import ru.lisss79.weatherforecast.entities.weather.UniversalTime
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime

object MockData {
    val usualCurrentWeatherState = UniversalWeatherState.Current(
        universalTime = UniversalTime.DateTime(ZonedDateTime.now()),
        weatherCode = 51,
        current = CurrentWeatherState(
            time = ZonedDateTime.now(),
            temperature = 20f,
            humidity = 99.99f,
            windSpeed = 3.33f,
            windDirection = 100f,
            precipitation = 0.1f,
            surfacePressure = 100f,
            cloudCover = 50f,
            weatherCode = 51
        )

    )
    val usualDailyWeatherState = UniversalWeatherState.Daily(
        universalTime = UniversalTime.DateTime(ZonedDateTime.now()),
        weatherCode = 51,
        daily = DailyWeatherState(
            time = LocalDate.now(),
            temperatureMin = 10.5f,
            temperatureMax = 17.3f,
            windSpeedMax = 15f,
            windGustsMax = 22f,
            windDirectionDominant = 45f,
            precipitationSum = 15f,
            precipitationProbability = 75f,
            weatherCode = 63,
            sunrise = LocalTime.now(),
            sunset = LocalTime.now()
        )
    )
    val usualCoords = Coords(11.11, -22.22)
}