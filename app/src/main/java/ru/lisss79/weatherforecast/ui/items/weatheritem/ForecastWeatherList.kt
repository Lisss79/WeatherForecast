package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.entities.WeatherQuery
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

@Composable
fun UniversalForecastWeatherList(
    modifier: Modifier = Modifier,
    universalWeatherState: List<UniversalWeatherState?>?,
    weatherQuery: WeatherQuery,
    columnState: LazyListState,
    placeStates: Set<Site>? = null,
    showPlace: Boolean = false
) {
    if (universalWeatherState == null) return

    Box(
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.align(Alignment.TopCenter),
            state = columnState
        ) {
            items(universalWeatherState) { state ->
                if (showPlace) {
                    val index = universalWeatherState.indexOf(state)
                    placeStates?.let { placeStateNonNull ->
                        if (index < placeStateNonNull.size)  {
                            val placeState = placeStateNonNull.elementAt(index)
                            WeatherItem(
                                modifier = Modifier.padding(vertical = 2.dp),
                                placeState = placeState,
                                universalWeatherState = state,
                                weatherQuery = weatherQuery,
                                showPlace = true,
                                justPlace = true,
                            )
                        }
                    }
                } else {
                    WeatherItem(
                        modifier = Modifier.padding(vertical = 2.dp),
                        universalWeatherState = state,
                        weatherQuery = weatherQuery,
                        showPlace = false
                    )
                }
            }
        }
    }

}

@Preview
@Composable
fun UniversalForecastWeatherListPreview() {
    UniversalForecastWeatherList(
        universalWeatherState = List(20) { MockData.usualDailyWeatherState },
        columnState = rememberLazyListState(),
        weatherQuery = WeatherQuery()
    )
}