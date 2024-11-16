package ru.lisss79.weatherforecast.di

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.lisss79.weatherforecast.data.retrofit.GeocoderMapsCoApi
import ru.lisss79.weatherforecast.data.retrofit.GeocoderYandexApi
import ru.lisss79.weatherforecast.data.retrofit.TimeZoneApi
import ru.lisss79.weatherforecast.data.retrofit.WeatherApi

val apiModule = DI.Module(name = "Api") {
    bindSingleton<GeocoderYandexApi> {
        Retrofit.Builder()
            .baseUrl("https://geocode-maps.yandex.ru/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocoderYandexApi::class.java)
    }
    bindSingleton<GeocoderMapsCoApi> {
        Retrofit.Builder()
            .baseUrl("https://geocode.maps.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeocoderMapsCoApi::class.java)
    }
    bindSingleton<TimeZoneApi> {
        Retrofit.Builder()
            .baseUrl("https://timeapi.io/api/timezone/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TimeZoneApi::class.java)
    }
    bindSingleton<WeatherApi> {
        Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}