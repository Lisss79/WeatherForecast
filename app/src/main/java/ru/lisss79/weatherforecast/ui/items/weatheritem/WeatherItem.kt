package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.LoadingState
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState

@Composable
fun WeatherItem(
    modifier: Modifier = Modifier,
    universalWeatherState: UniversalWeatherState?,
    placeState: Site? = null,
    coordsState: Coords? = null,
    showPlace: Boolean = false,
    justPlace: Boolean = false,
    loadingState: LoadingState = LoadingState.FINISHED
) {
    Surface(
        modifier = modifier.wrapContentHeight(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shape = MaterialTheme.shapes.medium
    ) {
        when {
            loadingState == LoadingState.LOADING_WEATHER &&  universalWeatherState == null ->
                NoDataText("Loading weather...")
            loadingState == LoadingState.LOADING_COORDS &&  universalWeatherState == null ->
                NoDataText("Loading GPS coordinates...")
            loadingState == LoadingState.LOADING_TIMEZONES &&  universalWeatherState == null ->
                NoDataText("Updating timezones...")
            else -> {
                if (universalWeatherState != null) {
                    Row(
                        modifier = Modifier
                            .defaultMinSize(minHeight = 100.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .weight(3f)
                                .padding(vertical = 4.dp)
                        ) {
                            WeatherTimeCoordsItem(
                                universalWeatherState = universalWeatherState,
                                placeState = placeState,
                                coordsState = coordsState,
                                showPlace = showPlace,
                                justPlace = justPlace,
                                state = loadingState
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .weight(5f)
                                .padding(vertical = 4.dp)
                        ) {
                            WeatherDetailsItem(universalWeatherState = universalWeatherState)
                        }
                    }
                } else {
                    NoDataText("Weather data is not loaded :(")
                }
            }
        }
    }
}

@Composable
fun NoDataText(text: String) {
    Box(
        modifier = Modifier
            .defaultMinSize(minHeight = 100.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Preview(name = "With data")
@Composable
fun CurrentWeatherItemPreview() {
    val weatherState = MockData.usualCurrentWeatherState
    val coords = MockData.usualCoords
    WeatherItem(universalWeatherState = weatherState, coordsState = coords, showPlace = true)
}

@Preview(name = "Without data")
@Composable
fun CurrentWeatherItemEmptyPreview() {
    WeatherItem(universalWeatherState = null)
}