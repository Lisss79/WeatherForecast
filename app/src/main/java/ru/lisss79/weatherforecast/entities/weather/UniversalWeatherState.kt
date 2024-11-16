package ru.lisss79.weatherforecast.entities.weather

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
}