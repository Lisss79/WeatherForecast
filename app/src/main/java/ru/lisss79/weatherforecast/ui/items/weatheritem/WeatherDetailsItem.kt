package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.lisss79.weatherforecast.entities.TIME
import ru.lisss79.weatherforecast.entities.WeatherDetail
import ru.lisss79.weatherforecast.entities.WeatherQuery
import ru.lisss79.weatherforecast.entities.toInt
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.entities.weather.WeatherException
import ru.lisss79.weatherforecast.ui.helpers.FormatterForUi

@Composable
fun WeatherDetailsItem(
    universalWeatherState: UniversalWeatherState,
    weatherQuery: WeatherQuery?
) {
    val lines = remember { mutableStateOf<List<String>>(listOf()) }

    LaunchedEffect(key1 = universalWeatherState, key2 = weatherQuery) {
        var fieldsToShow = listOf<String>()
        @Suppress("IMPLICIT_CAST_TO_ANY")
        val weatherState = when (universalWeatherState) {
            is UniversalWeatherState.Current -> {
                fieldsToShow = WeatherDetail
                    .queryNamesToFieldNames(weatherQuery?.currentQuery ?: listOf())
                universalWeatherState.current
            }

            is UniversalWeatherState.Hourly -> {
                fieldsToShow = WeatherDetail
                    .queryNamesToFieldNames(weatherQuery?.hourlyQuery ?: listOf())
                universalWeatherState.hourly
            }

            is UniversalWeatherState.Daily -> {
                fieldsToShow = WeatherDetail
                    .queryNamesToFieldNames(weatherQuery?.dailyQuery ?: listOf())
                universalWeatherState.daily
            }
        }
        val fields = weatherState.javaClass.declaredFields

        val detailsList = mutableListOf<Pair<Float, String>>()
        fields.forEach { field ->
            field.isAccessible = true
            if (field.name != TIME && field.name != "\$stable" && fieldsToShow.contains(field.name)) {
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
        lines.value = FormatterForUi.detailsToText(detailsList)
    }

    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        AddWeatherDetails(lines = lines.value)
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
