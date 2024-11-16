package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.lisss79.weatherforecast.entities.TIME
import ru.lisss79.weatherforecast.entities.toInt
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.entities.weather.WeatherException
import ru.lisss79.weatherforecast.ui.helpers.FormatterForUi

@Composable
fun WeatherDetailsItem(universalWeatherState: UniversalWeatherState) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        @Suppress("IMPLICIT_CAST_TO_ANY")
        val weatherState =  when (universalWeatherState) {
            is UniversalWeatherState.Current -> universalWeatherState.current
            is UniversalWeatherState.Hourly -> universalWeatherState.hourly
            is UniversalWeatherState.Daily -> universalWeatherState.daily
        }
        val fields = weatherState.javaClass.declaredFields

        val detailsList = mutableListOf<Pair<Float, String>>()
        fields.forEach { field ->
            field.isAccessible = true
            if (field.name != TIME && field.name != "\$stable") {
                val value = field.get(weatherState)
                if (value != null) {
                    val valueFloat = when (value) {
                        is Float -> value
                        is Int -> value.toFloat()
                        is Boolean -> value.toInt().toFloat()
                        else -> throw (WeatherException("Unknown field type: ${field.name}"))
                    }
                    detailsList.add(valueFloat to field.name)
                }
            }
        }
        val lines = FormatterForUi.detailsToText(detailsList)
        AddWeatherDetails(lines = lines)
    }
}

@Composable
fun AddWeatherDetails(lines: List<String>) {
    lines.forEach { line ->
        Text(
            text = line,
            style = MaterialTheme.typography.bodySmall
        )
    }
}
