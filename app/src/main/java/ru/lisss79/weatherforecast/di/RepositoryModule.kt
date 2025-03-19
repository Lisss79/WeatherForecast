package ru.lisss79.weatherforecast.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepository
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryAndroid
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryMapsCo
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryYandex
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepository
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryGoogle
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryHuawei
import ru.lisss79.weatherforecast.data.repositories.timezone.TimeZoneRepository
import ru.lisss79.weatherforecast.data.repositories.timezone.TimeZoneRepositoryTimeapi
import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepository
import ru.lisss79.weatherforecast.data.repositories.weather.WeatherRepositoryOpenMeteo
import ru.lisss79.weatherforecast.data.retrofit.GeocoderMapsCoApi
import ru.lisss79.weatherforecast.data.retrofit.GeocoderYandexApi
import ru.lisss79.weatherforecast.data.retrofit.TimeZoneApi
import ru.lisss79.weatherforecast.data.retrofit.WeatherApi

val repositoryModule = DI.Module(name = "Repository") {
    bindSingleton<TimeZoneRepository> {
        TimeZoneRepositoryTimeapi(instance<TimeZoneApi>())
    }
    bindSingleton<WeatherRepository> {
        WeatherRepositoryOpenMeteo(instance<WeatherApi>())
    }
    bindSingleton<GeocoderRepository>(tag = ANDROID) {
        GeocoderRepositoryAndroid()
    }
    bindSingleton<GeocoderRepository>(tag = YANDEX) {
        GeocoderRepositoryYandex(instance<GeocoderYandexApi>())
    }
    bindSingleton<GeocoderRepository>(tag = MAPSCO) {
        GeocoderRepositoryMapsCo(instance<GeocoderMapsCoApi>())
    }
    bindSingleton<LocationRepository>(tag = HUAWEI) {
        LocationRepositoryHuawei()
    }
    bindSingleton<LocationRepository>(tag = GOOGLE) {
        LocationRepositoryGoogle()
    }
    bindSingleton<GeocoderRepositoryFactory> {
        GeocoderRepositoryFactory(di)
    }
    bindSingleton<LocationRepositoryFactory> {
        LocationRepositoryFactory(di)
    }
}