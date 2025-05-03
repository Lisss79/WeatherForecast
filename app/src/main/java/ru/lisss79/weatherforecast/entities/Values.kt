package ru.lisss79.weatherforecast.entities

object Values {
    val selectedPlaceDefault = -1
    var locationSensitivityMeters = 100
    var hmsIsAvailable = false
    var HUAWEI_API_KEY = ""
    var YANDEX_API_KEY = ""
    var MAPSCO_API_KEY = ""
}

enum class ForecastMode(val menuName: String) {
    HOURLY("Hourly"),
    DAILY("Daily"),
    DIFFERENT_PLACES("Different places");

    companion object {
        val default = HOURLY
    }
}

enum class WeatherDetail(val menuName: String, val queryName: String, val fieldName: String) {
    TEMPERATURE("Temperature", ru.lisss79.weatherforecast.entities.TEMPERATURE, "temperature"),
    TEMPERATURE_MIN("Temperature min", ru.lisss79.weatherforecast.entities.TEMPERATURE_MIN, "temperatureMin"),
    TEMPERATURE_MAX("Temperature max", ru.lisss79.weatherforecast.entities.TEMPERATURE_MAX, "temperatureMax"),
    HUMIDITY("Humidity", ru.lisss79.weatherforecast.entities.HUMIDITY, "humidity"),
    WIND_SPEED("Wind speed", ru.lisss79.weatherforecast.entities.WIND_SPEED, "windSpeed"),
    WIND_DIRECTION("Wind direction", ru.lisss79.weatherforecast.entities.WIND_DIRECTION, "windDirection"),
    WIND_SPEED_MAX("Wind speed mex", ru.lisss79.weatherforecast.entities.WIND_SPEED_MAX, "windSpeedMax"),
    WIND_GUSTS_MAX("Wind gusts max", ru.lisss79.weatherforecast.entities.WIND_GUSTS_MAX, "windGustsMax"),
    WIND_DIRECTION_DOMINANT("Wind direction dominant", ru.lisss79.weatherforecast.entities.WIND_DIRECTION_DOMINANT, "windDirectionDominant"),
    PRECIPITATION("Precipitation", ru.lisss79.weatherforecast.entities.PRECIPITATION, "precipitation"),
    PRECIPITATION_SUM("Precipitation sum", ru.lisss79.weatherforecast.entities.PRECIPITATION_SUM, "precipitationSum"),
    PRECIPITATION_PROBABILITY("Precipitation probability", ru.lisss79.weatherforecast.entities.PRECIPITATION_PROBABILITY, "precipitationProbability"),
    RAIN("Rain", ru.lisss79.weatherforecast.entities.RAIN, "rain"),
    SHOWERS("Showers", ru.lisss79.weatherforecast.entities.SHOWERS, "showers"),
    SNOWFALL("Snowfall", ru.lisss79.weatherforecast.entities.SNOWFALL, "snowfall"),
    RAIN_SUM("Rain sum", ru.lisss79.weatherforecast.entities.RAIN_SUM, "rainSum"),
    SHOWERS_SUM("Showers sum", ru.lisss79.weatherforecast.entities.SHOWERS_SUM, "showersSum"),
    SNOWFALL_SUM("Snowfall sum", ru.lisss79.weatherforecast.entities.SNOWFALL_SUM, "snowfallSum"),
    SURFACE_PRESSURE("Surface pressure", ru.lisss79.weatherforecast.entities.SURFACE_PRESSURE, "surfacePressure"),
    CLOUD_COVER("Cloud cover", ru.lisss79.weatherforecast.entities.CLOUD_COVER, "cloudCover"),
    WEATHER_CODE("Weather code", ru.lisss79.weatherforecast.entities.WEATHER_CODE, "weatherCode"),
    IS_DAY("Is day", ru.lisss79.weatherforecast.entities.IS_DAY, "isDay"),
    SUNRISE("Sunrise time", ru.lisss79.weatherforecast.entities.SUNRISE, "sunrise"),
    SUNSET("Sunset time", ru.lisss79.weatherforecast.entities.SUNSET, "sunset");

    companion object {
        val default = setOf(TEMPERATURE)
        fun getByFieldName(fieldName: String): WeatherDetail? {
            WeatherDetail.entries.forEach {
                if (fieldName == it.fieldName) return it
            }
            return null
        }
        private fun getByQueryName(queryName: String): WeatherDetail? {
            WeatherDetail.entries.forEach {
                if (queryName == it.queryName) return it
            }
            return null
        }
        fun queryNamesToFieldNames(queryNames: List<String>): List<String> {
            return queryNames.mapNotNull { getByQueryName(it)?.fieldName }
        }

        fun getAllWeatherQueries(): WeatherQuery {
            val q1 = CurrentWeatherDetails.entries.map { it.queryName }
            val q2 = HourlyWeatherDetails.entries.map { it.queryName }
            val q3 = DailyWeatherDetails.entries.map { it.queryName }
            return WeatherQuery(
                currentQuery = q1,
                hourlyQuery = q2,
                dailyQuery = q3
            )
        }

    }
}

enum class CurrentWeatherDetails(val menuName: String, val queryName: String) {
    TEMPERATURE("Temperature", ru.lisss79.weatherforecast.entities.TEMPERATURE),
    HUMIDITY("Humidity", ru.lisss79.weatherforecast.entities.HUMIDITY),
    WIND_SPEED("Wind speed", ru.lisss79.weatherforecast.entities.WIND_SPEED),
    WIND_DIRECTION("Wind direction", ru.lisss79.weatherforecast.entities.WIND_DIRECTION),
    PRECIPITATION("Precipitation", ru.lisss79.weatherforecast.entities.PRECIPITATION),
    RAIN("Rain", ru.lisss79.weatherforecast.entities.RAIN),
    SHOWERS("Showers", ru.lisss79.weatherforecast.entities.SHOWERS),
    SNOWFALL("Snowfall", ru.lisss79.weatherforecast.entities.SNOWFALL),
    SURFACE_PRESSURE("Surface pressure", ru.lisss79.weatherforecast.entities.SURFACE_PRESSURE),
    CLOUD_COVER("Cloud cover", ru.lisss79.weatherforecast.entities.CLOUD_COVER),
    WEATHER_CODE("Weather code", ru.lisss79.weatherforecast.entities.WEATHER_CODE),
    IS_DAY("Is day", ru.lisss79.weatherforecast.entities.IS_DAY);

    companion object {
        val default = setOf(TEMPERATURE)
    }
}

enum class HourlyWeatherDetails(val menuName: String, val queryName: String) {
    TEMPERATURE("Temperature", ru.lisss79.weatherforecast.entities.TEMPERATURE),
    HUMIDITY("Humidity", ru.lisss79.weatherforecast.entities.HUMIDITY),
    WIND_SPEED("Wind speed", ru.lisss79.weatherforecast.entities.WIND_SPEED),
    WIND_DIRECTION("Wind direction", ru.lisss79.weatherforecast.entities.WIND_DIRECTION),
    PRECIPITATION("Precipitation", ru.lisss79.weatherforecast.entities.PRECIPITATION),
    RAIN("Rain", ru.lisss79.weatherforecast.entities.RAIN),
    SHOWERS("Showers", ru.lisss79.weatherforecast.entities.SHOWERS),
    SNOWFALL("Snowfall", ru.lisss79.weatherforecast.entities.SNOWFALL),
    SURFACE_PRESSURE("Surface pressure", ru.lisss79.weatherforecast.entities.SURFACE_PRESSURE),
    CLOUD_COVER("Cloud cover", ru.lisss79.weatherforecast.entities.CLOUD_COVER),
    WEATHER_CODE("Weather code", ru.lisss79.weatherforecast.entities.WEATHER_CODE),
    IS_DAY("Is day", ru.lisss79.weatherforecast.entities.IS_DAY);

    companion object {
        val default = setOf(TEMPERATURE)
    }
}

enum class DailyWeatherDetails(val menuName: String, val queryName: String) {
    TEMPERATURE_MIN("Temperature min", ru.lisss79.weatherforecast.entities.TEMPERATURE_MIN),
    TEMPERATURE_MAX("Temperature max", ru.lisss79.weatherforecast.entities.TEMPERATURE_MAX),
    WIND_SPEED_MAX("Wind speed mex", ru.lisss79.weatherforecast.entities.WIND_SPEED_MAX),
    WIND_GUSTS_MAX("Wind gusts max", ru.lisss79.weatherforecast.entities.WIND_GUSTS_MAX),
    WIND_DIRECTION_DOMINANT("Wind direction dominant", ru.lisss79.weatherforecast.entities.WIND_DIRECTION_DOMINANT),
    PRECIPITATION_SUM("Precipitation sum", ru.lisss79.weatherforecast.entities.PRECIPITATION_SUM),
    PRECIPITATION_PROBABILITY("Precipitation probability", ru.lisss79.weatherforecast.entities.PRECIPITATION_PROBABILITY),
    RAIN_SUM("Rain sum", ru.lisss79.weatherforecast.entities.RAIN_SUM),
    SHOWERS_SUM("Showers sum", ru.lisss79.weatherforecast.entities.SHOWERS_SUM),
    SNOWFALL_SUM("Snowfall sum", ru.lisss79.weatherforecast.entities.SNOWFALL_SUM),
    WEATHER_CODE("Weather code", ru.lisss79.weatherforecast.entities.WEATHER_CODE),
    SUNRISE("Sunrise time", ru.lisss79.weatherforecast.entities.SUNRISE),
    SUNSET("Sunset time", ru.lisss79.weatherforecast.entities.SUNSET);

    companion object {
        val default = setOf(TEMPERATURE_MIN, TEMPERATURE_MAX)
    }
}

data class WeatherQuery(
    val currentQuery: List<String> = CurrentWeatherDetails.default.map { it.queryName },
    val hourlyQuery: List<String> = HourlyWeatherDetails.default.map { it.queryName },
    val dailyQuery: List<String> = DailyWeatherDetails.default.map { it.queryName }
)

enum class PlacesSortingMode(val subscription: String) {
    UNSORTED("Unsorted order"),
    DISTANCE("Sorted by distance from default one"),
    NAME("Sorted by name");

    companion object {
        val default = UNSORTED
    }
}

enum class SettingsScreens {
    MAIN, DETAILS
}