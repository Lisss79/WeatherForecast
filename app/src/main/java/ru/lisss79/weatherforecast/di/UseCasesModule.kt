package ru.lisss79.weatherforecast.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepository
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.timezone.TimeZoneRepository
import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository
import ru.lisss79.weatherforecast.domain.coords.GetCoordsUseCase
import ru.lisss79.weatherforecast.domain.places.GetPlaceByCoordsUseCase
import ru.lisss79.weatherforecast.domain.places.GetPlacesByNameUseCase
import ru.lisss79.weatherforecast.domain.timezone.GetTimeOffsetUseCase
import ru.lisss79.weatherforecast.domain.weather.GetCurrentAndDailyWeatherUseCase
import ru.lisss79.weatherforecast.domain.weather.GetCurrentAndHourlyWeatherUseCase
import ru.lisss79.weatherforecast.domain.weather.GetCurrentWeatherUseCase
import ru.lisss79.weatherforecast.domain.weather.GetWeatherFromDifferentPlacesUseCase

val useCasesModule = DI.Module(name = "UseCase") {
    bindSingleton<GetCurrentWeatherUseCase> {
        GetCurrentWeatherUseCase(instance<WeatherRepository>())
    }
    bindSingleton<GetCurrentAndHourlyWeatherUseCase> {
        GetCurrentAndHourlyWeatherUseCase(instance<WeatherRepository>())
    }
    bindSingleton<GetCurrentAndDailyWeatherUseCase> {
        GetCurrentAndDailyWeatherUseCase(instance<WeatherRepository>())
    }
    bindSingleton<GetWeatherFromDifferentPlacesUseCase> {
        GetWeatherFromDifferentPlacesUseCase(instance<WeatherRepository>())
    }
    bindSingleton<GetPlacesByNameUseCase> {
        GetPlacesByNameUseCase(
            instance<GeocoderRepositoryFactory>(), instance<TimeZoneRepository>()
        )
    }
    bindSingleton<GetPlaceByCoordsUseCase> {
        GetPlaceByCoordsUseCase(
            instance<GeocoderRepositoryFactory>(), instance<TimeZoneRepository>()
        )
    }
    bindSingleton<GetCoordsUseCase> {
        GetCoordsUseCase(instance<LocationRepositoryFactory>())
    }
    bindSingleton<GetTimeOffsetUseCase> {
        GetTimeOffsetUseCase(instance<TimeZoneRepository>())
    }
}
