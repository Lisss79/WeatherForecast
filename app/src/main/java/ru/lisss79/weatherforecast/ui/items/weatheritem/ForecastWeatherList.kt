package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.coerceAtLeast
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
    showPlace: Boolean = false,
    scrollIndex: Int? = null,
) {
    if (universalWeatherState == null) return
    val density = LocalDensity.current

    LaunchedEffect(universalWeatherState) {
        if (universalWeatherState.isNotEmpty()) {
            columnState.scrollToItem(scrollIndex ?: 0)
        }
    }

    Box(
        modifier = modifier
    ) {
        val heightWithPlace = remember { mutableStateOf(0.dp) }
        val heightWithoutPlace = remember { mutableStateOf(0.dp) }
        val flingBehavior = rememberSnapFlingBehavior(
            lazyListState = columnState,
            snapPosition = SnapPosition.Start
        )

        LazyColumn(
            modifier = Modifier.align(Alignment.TopCenter),
            state = columnState,
            flingBehavior = flingBehavior
        ) {
            items(items = universalWeatherState, key = { it.hashCode() }) { state ->
                val index = universalWeatherState.indexOf(state)
                if (showPlace) {
                    placeStates?.let { placeStateNonNull ->
                        if (index < placeStateNonNull.size)  {
                            val placeState = placeStateNonNull.elementAt(index)
                            WeatherItem(
                                modifier = Modifier
                                    .padding(vertical = 2.dp)
                                    .heightIn(min = heightWithPlace.value)
                                    .wrapContentHeight()
                                    .then(if (index == 0 || index == scrollIndex) Modifier.onSizeChanged {
                                        val newHeight = with(density) { it.height.toDp() }
                                        heightWithPlace.value =
                                            newHeight.coerceAtLeast(heightWithPlace.value)
                                    } else Modifier),
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
                        modifier = Modifier
                            .padding(vertical = 2.dp)
                            .heightIn(min = heightWithoutPlace.value)
                            .wrapContentHeight()
                            .then(if (index == 0 || index == scrollIndex) Modifier.onSizeChanged {
                                val newHeight = with(density) { it.height.toDp() }
                                heightWithoutPlace.value =
                                    newHeight.coerceAtLeast(heightWithoutPlace.value)
                            } else Modifier),
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