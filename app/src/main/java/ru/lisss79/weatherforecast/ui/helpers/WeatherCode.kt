package ru.lisss79.weatherforecast.ui.helpers

import ru.lisss79.weatherforecast.R
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

data class WeatherCode(val code: Int?, val text: String, val icon: Int) {

    companion object {
        private val unknownValue = "unknown"
        private val unknownIcon = R.drawable.unknown
        private fun getUnknownWeatherCode(code: Int? = null) =
            WeatherCode(code, unknownValue, unknownIcon)

        private val codeMapper = mapOf(
            0 to WeatherCode(0, "clear sky", R.drawable.sun),
            1 to WeatherCode(1, "mainly clear", R.drawable.sun_and_cloud),
            2 to WeatherCode(2, "partly cloudy", R.drawable.sun_and_cloud),
            3 to WeatherCode(2, "overcast", R.drawable.cloud),
            45 to WeatherCode(45, "fog", R.drawable.cloud),
            48 to WeatherCode(48, "depositing rime fog", R.drawable.sun_and_cloud),
            51 to WeatherCode(51, "light drizzle", R.drawable.drizzle),
            53 to WeatherCode(53, "moderate drizzle", R.drawable.drizzle),
            55 to WeatherCode(55, "dense intensity drizzle", R.drawable.drizzle),
            56 to WeatherCode(56, "light freezing drizzle", R.drawable.drizzle),
            57 to WeatherCode(57, "dense intensity freezing drizzle", R.drawable.drizzle),
            61 to WeatherCode(61, "slight rain", R.drawable.rain),
            63 to WeatherCode(63, "moderate rain", R.drawable.rain),
            65 to WeatherCode(65, "heavy intensity rain", R.drawable.rain),
            66 to WeatherCode(66, "light freezing rain", R.drawable.rain),
            67 to WeatherCode(67, "heavy intensity freezing rain", R.drawable.rain),
            71 to WeatherCode(71, "slight snow fall", R.drawable.snow),
            73 to WeatherCode(73, "moderate snow fall", R.drawable.snow),
            75 to WeatherCode(75, "heavy intensity snow fall", R.drawable.snow),
            77 to WeatherCode(77, "snow grains", R.drawable.snow),
            80 to WeatherCode(80, "slight rain showers", R.drawable.shower),
            81 to WeatherCode(81, "moderate rain showers", R.drawable.shower),
            82 to WeatherCode(82, "violent rain showers", R.drawable.shower),
            85 to WeatherCode(85, "slight snow showers", R.drawable.shower),
            86 to WeatherCode(86, "heavy snow showers", R.drawable.shower),
            95 to WeatherCode(95, "slight or moderate thunderstorm", R.drawable.thunder),
            96 to WeatherCode(96, "thunderstorm with slight hail", R.drawable.thunder_and_rain),
            99 to WeatherCode(99, "thunderstorm with heavy hail", R.drawable.thunder_and_rain)
        )

        fun getWeatherText(code: Int?): String {
            if (code == null) return unknownValue
            return codeMapper.getOrDefault(code, getUnknownWeatherCode(code)).text
        }

        fun getWeatherCode(universalWeatherState: UniversalWeatherState?): WeatherCode {
            val code = universalWeatherState?.weatherCode ?: return getUnknownWeatherCode()
            var currentCode = codeMapper.getOrDefault(code, getUnknownWeatherCode(code))
            if (universalWeatherState.getDay() == false) {
                currentCode = when(code) {
                    0 -> currentCode.copy(icon = R.drawable.moon)
                    1 -> currentCode.copy(icon = R.drawable.moon_and_cloud)
                    2 -> currentCode.copy(icon = R.drawable.moon_and_cloud)
                    48 -> currentCode.copy(icon = R.drawable.moon_and_cloud)
                    else -> currentCode
                }
            }
            return currentCode
        }
    }
}
