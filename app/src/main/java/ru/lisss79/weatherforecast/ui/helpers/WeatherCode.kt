package ru.lisss79.weatherforecast.ui.helpers

import ru.lisss79.weatherforecast.R
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

data class WeatherCode(val code: Int?, val text: String) {

    companion object {
        private val unknownValue = "unknown"
        private val unknownIcon = R.drawable.unknown
        private fun getUnknownWeatherCode(code: Int? = null) =
            WeatherCode(code, unknownValue)

        private val codeMapper = mapOf(
            0 to WeatherCode(0, "clear sky"),
            1 to WeatherCode(1, "mainly clear"),
            2 to WeatherCode(2, "partly cloudy"),
            3 to WeatherCode(3, "overcast"),
            45 to WeatherCode(45, "fog"),
            48 to WeatherCode(48, "depositing rime fog"),
            51 to WeatherCode(51, "light drizzle"),
            53 to WeatherCode(53, "moderate drizzle"),
            55 to WeatherCode(55, "dense intensity drizzle"),
            56 to WeatherCode(56, "light freezing drizzle"),
            57 to WeatherCode(57, "dense intensity freezing drizzle"),
            61 to WeatherCode(61, "slight rain"),
            63 to WeatherCode(63, "moderate rain"),
            65 to WeatherCode(65, "heavy intensity rain"),
            66 to WeatherCode(66, "light freezing rain"),
            67 to WeatherCode(67, "heavy intensity freezing rain"),
            71 to WeatherCode(71, "slight snow fall"),
            73 to WeatherCode(73, "moderate snow fall"),
            75 to WeatherCode(75, "heavy intensity snow fall"),
            77 to WeatherCode(77, "snow grains"),
            80 to WeatherCode(80, "slight rain showers"),
            81 to WeatherCode(81, "moderate rain showers"),
            82 to WeatherCode(82, "violent rain showers"),
            85 to WeatherCode(85, "slight snow showers"),
            86 to WeatherCode(86, "heavy snow showers"),
            95 to WeatherCode(95, "slight or moderate thunderstorm"),
            96 to WeatherCode(96, "thunderstorm with slight hail"),
            99 to WeatherCode(99, "thunderstorm with heavy hail")
        )

        fun getWeatherText(code: Int?): String {
            if (code == null) return unknownValue
            return codeMapper.getOrDefault(code, getUnknownWeatherCode(code)).text
        }

    }
}