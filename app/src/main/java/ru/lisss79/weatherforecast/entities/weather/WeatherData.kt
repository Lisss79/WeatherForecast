package ru.lisss79.weatherforecast.entities.weather

import com.google.gson.annotations.SerializedName
import ru.lisss79.weatherforecast.entities.CLOUD_COVER
import ru.lisss79.weatherforecast.entities.CURRENT
import ru.lisss79.weatherforecast.entities.DAILY
import ru.lisss79.weatherforecast.entities.HOURLY
import ru.lisss79.weatherforecast.entities.HUMIDITY
import ru.lisss79.weatherforecast.entities.INTERVAL
import ru.lisss79.weatherforecast.entities.IS_DAY
import ru.lisss79.weatherforecast.entities.PRECIPITATION
import ru.lisss79.weatherforecast.entities.PRECIPITATION_PROBABILITY
import ru.lisss79.weatherforecast.entities.PRECIPITATION_SUM
import ru.lisss79.weatherforecast.entities.RAIN
import ru.lisss79.weatherforecast.entities.RAIN_SUM
import ru.lisss79.weatherforecast.entities.SHOWERS
import ru.lisss79.weatherforecast.entities.SHOWERS_SUM
import ru.lisss79.weatherforecast.entities.SNOWFALL
import ru.lisss79.weatherforecast.entities.SNOWFALL_SUM
import ru.lisss79.weatherforecast.entities.SUNRISE
import ru.lisss79.weatherforecast.entities.SUNSET
import ru.lisss79.weatherforecast.entities.SURFACE_PRESSURE
import ru.lisss79.weatherforecast.entities.TEMPERATURE
import ru.lisss79.weatherforecast.entities.TEMPERATURE_MAX
import ru.lisss79.weatherforecast.entities.TEMPERATURE_MIN
import ru.lisss79.weatherforecast.entities.TIME
import ru.lisss79.weatherforecast.entities.TIMEZONE
import ru.lisss79.weatherforecast.entities.UTC_OFFSET_SECONDS
import ru.lisss79.weatherforecast.entities.WEATHER_CODE
import ru.lisss79.weatherforecast.entities.WIND_DIRECTION
import ru.lisss79.weatherforecast.entities.WIND_DIRECTION_DOMINANT
import ru.lisss79.weatherforecast.entities.WIND_GUSTS_MAX
import ru.lisss79.weatherforecast.entities.WIND_SPEED
import ru.lisss79.weatherforecast.entities.WIND_SPEED_MAX

// Сюда добавляем нужные поля из ответа API
data class WeatherData(
    @SerializedName(TIMEZONE) val timezone : String? = null,
    @SerializedName(UTC_OFFSET_SECONDS) val utcOffsetSeconds : Int? = null,
    @SerializedName(CURRENT) var current : Current? = Current(),
    @SerializedName(HOURLY) var hourly : Hourly? = Hourly(),
    @SerializedName(DAILY) var daily : Daily? = Daily()
) {
    data class Current (
        @SerializedName(TIME) var time : String? = null,
        @SerializedName(INTERVAL) var interval : Int? = null,
        @SerializedName(TEMPERATURE) var temperature2m : Float? = null,
        @SerializedName(HUMIDITY) var relativeHumidity2m : Float? = null,
        @SerializedName(WIND_SPEED) var windSpeed10m : Float? = null,
        @SerializedName(WIND_DIRECTION) var windDirection10m : Float? = null,
        @SerializedName(PRECIPITATION) var precipitation : Float? = null,
        @SerializedName(RAIN) var rain : Float? = null,
        @SerializedName(SHOWERS) var showers : Float? = null,
        @SerializedName(SNOWFALL) var snowfall : Float? = null,
        @SerializedName(SURFACE_PRESSURE) var surfacePressure : Float? = null,
        @SerializedName(CLOUD_COVER) var cloudCover: Float? = null,
        @SerializedName(WEATHER_CODE) var weatherCode: Int? = null,
        @SerializedName(IS_DAY) var isDay: Int? = 1
    )

    data class Hourly (
        @SerializedName(TIME) var time : ArrayList<String> = arrayListOf(),
        @SerializedName(TEMPERATURE) var temperature2m : ArrayList<Float> = arrayListOf(),
        @SerializedName(HUMIDITY) var relativeHumidity2m : ArrayList<Float> = arrayListOf(),
        @SerializedName(WIND_SPEED) var windSpeed10m : ArrayList<Float> = arrayListOf(),
        @SerializedName(WIND_DIRECTION) var windDirection10m : ArrayList<Float> = arrayListOf(),
        @SerializedName(PRECIPITATION) var precipitation : ArrayList<Float> = arrayListOf(),
        @SerializedName(RAIN) var rain : ArrayList<Float> = arrayListOf(),
        @SerializedName(SHOWERS) var showers : ArrayList<Float> = arrayListOf(),
        @SerializedName(SNOWFALL) var snowfall : ArrayList<Float> = arrayListOf(),
        @SerializedName(SURFACE_PRESSURE) var surfacePressure : ArrayList<Float> = arrayListOf(),
        @SerializedName(CLOUD_COVER) var cloudCover : ArrayList<Float> = arrayListOf(),
        @SerializedName(WEATHER_CODE) var weatherCode: ArrayList<Int> = arrayListOf(),
        @SerializedName(IS_DAY) var isDay: ArrayList<Int> = arrayListOf()
    )

    data class Daily (
        @SerializedName(TIME) var time : ArrayList<String> = arrayListOf(),
        @SerializedName(TEMPERATURE_MIN) var temperature2mMin : ArrayList<Float> = arrayListOf(),
        @SerializedName(TEMPERATURE_MAX) var temperature2mMax : ArrayList<Float> = arrayListOf(),
        @SerializedName(WIND_SPEED_MAX) var windSpeed10mMax : ArrayList<Float> = arrayListOf(),
        @SerializedName(WIND_GUSTS_MAX) var windGusts10mMax : ArrayList<Float> = arrayListOf(),
        @SerializedName(WIND_DIRECTION_DOMINANT) var windDirection10mDominant : ArrayList<Float> = arrayListOf(),
        @SerializedName(PRECIPITATION_SUM) var precipitationSum : ArrayList<Float> = arrayListOf(),
        @SerializedName(RAIN_SUM) var rainSum : ArrayList<Float> = arrayListOf(),
        @SerializedName(SHOWERS_SUM) var showersSum : ArrayList<Float> = arrayListOf(),
        @SerializedName(SNOWFALL_SUM) var snowfallSum : ArrayList<Float> = arrayListOf(),
        @SerializedName(PRECIPITATION_PROBABILITY) var precipitationProbabilityMean : ArrayList<Float> = arrayListOf(),
        @SerializedName(WEATHER_CODE) var weatherCode: ArrayList<Int> = arrayListOf(),
        @SerializedName(SUNRISE) var sunrise: ArrayList<String> = arrayListOf(),
        @SerializedName(SUNSET) var sunset: ArrayList<String> = arrayListOf(),
    )

}
