package ru.lisss79.weatherforecast.domain.weather

import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository

abstract class WeatherUseCase(val weatherRepository: WeatherRepository) {
}