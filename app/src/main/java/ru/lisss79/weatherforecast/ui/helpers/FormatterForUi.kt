package ru.lisss79.weatherforecast.ui.helpers

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.WeatherDetail
import ru.lisss79.weatherforecast.entities.weather.UniversalTime
import java.text.DecimalFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object FormatterForUi {

    fun detailsToText(details: List<Pair<Float?, String>>): List<String> {
        val comparator = object : Comparator<Pair<Float?, String>> {
            override fun compare(p0: Pair<Float?, String>?, p1: Pair<Float?, String>?): Int {
                if (p0 == null || p1 == null) return 0
                val c0 = WeatherDetail.getByFieldName(p0.second)
                val c1 = WeatherDetail.getByFieldName(p1.second)
                if (c0 == null || c1 == null) return 0
                return c0.compareTo(c1)
            }
        }
        val sortedDetails = details.sortedWith(comparator)
        val result = sortedDetails.map { detailToText(it.first, it.second) }
        return result
    }

    private fun detailToText(detail: Float?, key: String): String {
        val out = when (key) {
            WeatherDetail.TEMPERATURE.fieldName -> temperatureToText(detail, "Temperature")
            WeatherDetail.TEMPERATURE_MIN.fieldName -> temperatureToText(detail, "Min temperature")
            WeatherDetail.TEMPERATURE_MAX.fieldName -> temperatureToText(detail, "Max temperature")
            WeatherDetail.HUMIDITY.fieldName -> detailToFloatText(detail, "Humidity")
            WeatherDetail.WIND_SPEED.fieldName -> detailToFloatText(detail, "Wind speed", "m/s")
            WeatherDetail.WIND_SPEED_MAX.fieldName -> detailToFloatText(detail, "Wind speed max", "m/s")
            WeatherDetail.WIND_GUSTS_MAX.fieldName -> detailToFloatText(detail, "Wind gusts max", "m/s")
            WeatherDetail.WIND_DIRECTION.fieldName -> windDirectionToText(detail, "Wind direction")
            WeatherDetail.WIND_DIRECTION_DOMINANT.fieldName -> windDirectionToText(detail, "Dominant wind direction")
            WeatherDetail.PRECIPITATION.fieldName -> detailToFloatText(detail, "Precipitation", "mm")
            WeatherDetail.RAIN.fieldName -> detailToFloatText(detail, "Rain", "mm")
            WeatherDetail.SHOWERS.fieldName -> detailToFloatText(detail, "Showers", "mm")
            WeatherDetail.SNOWFALL.fieldName -> detailToFloatText(detail, "Snowfall", "cm")
            WeatherDetail.PRECIPITATION_SUM.fieldName -> detailToFloatText(detail, "Total precipitation", "mm")
            WeatherDetail.RAIN_SUM.fieldName -> detailToFloatText(detail, "Total rain", "mm")
            WeatherDetail.SHOWERS_SUM.fieldName -> detailToFloatText(detail, "Total showers", "mm")
            WeatherDetail.SNOWFALL_SUM.fieldName -> detailToFloatText(detail, "Total snowfall", "cm")
            WeatherDetail.PRECIPITATION_PROBABILITY.fieldName -> detailToFloatText(detail, "Precipitation probability")
            WeatherDetail.SURFACE_PRESSURE.fieldName -> surfacePressureToText(detail)
            WeatherDetail.CLOUD_COVER.fieldName -> detailToFloatText(detail, "Cloud cover")
            WeatherDetail.WEATHER_CODE.fieldName -> weatherCodeToText(detail)
            WeatherDetail.IS_DAY.fieldName -> isDayToText(detail)
            WeatherDetail.SUNRISE.fieldName -> detailToLocalTimeText(detail, "Sunrise")
            WeatherDetail.SUNSET.fieldName -> detailToLocalTimeText(detail, "Sunset")
            else -> "Incorrect data"
        }
        return out
    }

    fun universalTimeToText(universalTime: UniversalTime?, place: Site? = null): String {
        return when (universalTime) {
            is UniversalTime.DateOnly -> dateToText(universalTime.date)
            is UniversalTime.DateTime -> dateTimeToText(universalTime.dateTime, place?.utcOffset)
            null -> "no data"
        }
    }

    private fun dateTimeToText(time: ZonedDateTime?, utcOffset: Int?): String {
        val formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss", Locale.getDefault())
        val out = time?.let {
            formatter.format(it)
        } ?: "no data"
        return out
    }

    private fun dateToText(time: LocalDate?): String {
        val formatter = DateTimeFormatter
            .ofPattern("dd.MM.yyyy", Locale.getDefault())
        val out = time?.let {
            formatter.format(it)
        } ?: "no data"
        return out
    }

    private fun detailToLocalTimeText(time: Float?, propertyName: String): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
        val out = time?.let {
            val hour = time.toInt() / 100
            val min = time.toInt() % 100
            val localTime = LocalTime.of(hour, min)
            formatter.format(localTime)
        } ?: "no data"
        return "$propertyName: $out"
    }

    fun temperatureToText(temperature: Float?, propertyName: String): String {
        val decimalFormat = DecimalFormat("+#.##;-#.##")
        val out = temperature?.let {
            buildString {
                append(decimalFormat.format(it))
                append("°C")
            }
        } ?: "no data"
        return "$propertyName: $out"
    }

    fun placeToText(
        place: Site?,
        justPlace: Boolean = false,
        countryAfterLocality: Boolean = false
    ): String {
        if (place == null) return "Can't read place name"

        val addressPart = place.address.run {
            listOf(
                locality, subLocality, subAdminArea, adminArea, tertiaryAdminArea, country, "somewhere"
            )
        }.first { it != null }

        val countryName = place.address.country
        val placeName = if (countryName != null) {
            if (!countryAfterLocality) "${place.address.country}, $addressPart"
            else "$addressPart, ${place.address.country}"
        }
        else addressPart
        val out = if (justPlace) placeName
        else String.format("Current place: $placeName")
        return out
    }

    fun coordsToText(coords: Coords?): String {
        if (coords == null) return "Can't read coordinates"
        val lat = String.format(Locale.getDefault(), "%+.2f", coords.latitude)
        val lon = String.format(Locale.getDefault(), "%+.2f", coords.longitude)
        val out = String
            .format("Current coordinates: $lat°, $lon°")
        return out
    }

    fun windDirectionToText(windDirection: Float?, propertyName: String): String {
        val out = windDirection?.let {
            val decimalFormat = DecimalFormat("#")
            buildString {
                append(decimalFormat.format(it))
                append("°")
            }
        } ?: "no data"
        return "$propertyName: $out"
    }

    fun surfacePressureToText(surfacePressure: Float?): String {
        val out = surfacePressure?.let {
            val decimalFormat = DecimalFormat("#.##")
            buildString {
                append(decimalFormat.format(it * 0.75))
                append("mmHg")
                //append("hPa")
            }
        } ?: "no data"
        return "Pressure: $out"
    }

    fun detailToFloatText(
        detail: Float?,
        propertyName: String,
        propertyUnit: String = "%"
    ): String {
        val out = detail?.let {
            val decimalFormat = DecimalFormat("#.##")
            buildString {
                append(decimalFormat.format(it))
                append(propertyUnit)
            }
        } ?: "no data"
        return "$propertyName: $out"
    }

    fun weatherCodeToText(weatherCode: Float?): String {
        val out = WeatherCode.getWeatherText(weatherCode?.toInt())
        return "Type: $out"
    }

    fun isDayToText(isDay: Float?): String {
        val out = if (isDay == 1f) "is day" else "is night"
        return "Now $out"
    }

}