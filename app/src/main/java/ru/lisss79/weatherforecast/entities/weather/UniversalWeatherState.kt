package ru.lisss79.weatherforecast.entities.weather

import ru.lisss79.weatherforecast.ui.items.weatheritem.MockData
import java.time.ZonedDateTime

sealed class UniversalWeatherState(
    open val universalTime: UniversalTime,
    open val weatherCode: Int?
) {
    abstract fun getDay(): Boolean?
    abstract fun getTemperature(): Float?
    abstract fun getCloudCover(): Float?
    abstract fun getPrecipitation(): Float?

    class Current(
        override val universalTime: UniversalTime,
        override val weatherCode: Int?,
        val current: CurrentWeatherState
    ): UniversalWeatherState(universalTime, weatherCode) {
        override fun getDay() = current.isDay
        override fun getTemperature() = current.temperature
        override fun getCloudCover() = current.cloudCover
        override fun getPrecipitation() = current.precipitation
    }

    class Hourly(
        override val universalTime: UniversalTime,
        override val weatherCode: Int?,
        val hourly: HourlyWeatherState
    ): UniversalWeatherState(universalTime, weatherCode) {
        override fun getDay() = hourly.isDay
        override fun getTemperature() = hourly.temperature
        override fun getCloudCover() = hourly.cloudCover
        override fun getPrecipitation() = hourly.precipitation
    }

    class Daily(
        override val universalTime: UniversalTime,
        override val weatherCode: Int?,
        val daily: DailyWeatherState
    ): UniversalWeatherState(universalTime, weatherCode) {
        override fun getDay() = null
        override fun getTemperature() =
            if (daily.temperatureMin != null && daily.temperatureMax != null)
                    (daily.temperatureMin + daily.temperatureMax) / 2
            else null
        override fun getCloudCover() = null
        override fun getPrecipitation() = daily.precipitationSum?.div(12)
    }

    companion object {
        fun create(): UniversalWeatherState = MockData.usualCurrentWeatherState
    }

    class Builder(
        var weatherCode: Int? = null,
        var cloudiness: Int = 0,
        var isDay: Boolean = true
    ) {
        fun cloudiness(c: Int): Builder {
            cloudiness = c
            return this
        }
        fun weatherCode(w: Int?): Builder {
            weatherCode = w
            return this
        }
        fun isDay(i: Boolean): Builder {
            isDay = i
            return this
        }
        fun build(): UniversalWeatherState {
            val c = CurrentWeatherState(
                cloudCover = cloudiness.toFloat(),
                weatherCode = weatherCode,
                isDay = isDay
            )
            return Current(
                universalTime = UniversalTime.DateTime(ZonedDateTime.now()),
                current = c,
                weatherCode = weatherCode
            )
        }
    }
}