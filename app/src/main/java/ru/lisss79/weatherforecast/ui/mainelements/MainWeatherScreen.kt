package ru.lisss79.weatherforecast.ui.mainelements

import android.location.Location
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.BiasAlignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huawei.hms.site.api.model.Site
import kotlinx.coroutines.CoroutineScope
import ru.lisss79.weatherforecast.data.datastore.DataStoreHelper
import ru.lisss79.weatherforecast.entities.Errors
import ru.lisss79.weatherforecast.entities.ForecastMode
import ru.lisss79.weatherforecast.entities.PlacesSortingMode
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.WeatherQuery
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.ui.items.GettingDataScreen
import ru.lisss79.weatherforecast.ui.items.ToastShowing
import ru.lisss79.weatherforecast.ui.items.weatheritem.UniversalForecastWeatherList
import ru.lisss79.weatherforecast.ui.items.weatheritem.UpFAB
import ru.lisss79.weatherforecast.ui.items.weatheritem.WeatherItem

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainWeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel,
    dataStoreHelper: DataStoreHelper,
    scope: CoroutineScope
) {
    val currentWeatherState = viewModel.currentWeather.collectAsStateWithLifecycle()
    val forecastWeatherState = viewModel.forecastWeather.collectAsStateWithLifecycle()
    val coordsState = viewModel.coords.collectAsStateWithLifecycle()
    val placeState = viewModel.place.collectAsStateWithLifecycle()
    val errorsState = viewModel.errors.collectAsStateWithLifecycle()
    val loadingState = viewModel.loading.collectAsStateWithLifecycle()
    val forecastModeSetting = dataStoreHelper
        .forecastModeFlow.collectAsStateWithLifecycle(ForecastMode.default)
    val placesList = dataStoreHelper
        .placesListFlow.collectAsStateWithLifecycle(setOf())
    val sortingMode = dataStoreHelper
        .placesSortingModeFlow.collectAsStateWithLifecycle(PlacesSortingMode.default)
    val selectedPlace = dataStoreHelper
        .selectedPlaceFlow.collectAsStateWithLifecycle(Values.selectedPlaceDefault)
    var fieldsToShow by remember { mutableStateOf(WeatherQuery()) }
    var refreshing by remember { mutableStateOf(false) }
    val pullRefreshState = rememberPullRefreshState(refreshing, {
        refreshing = true
        refreshData(
            viewModel = viewModel,
            dataStoreHelper = dataStoreHelper,
            forceUpdate = false,
            scope = scope
        )
        refreshing = false
    })
    val columnState = rememberLazyListState()
    // TODO: custom saver & rememberSavable
    val isScrolled by remember { derivedStateOf { columnState.firstVisibleItemIndex > 0 } }
    val localScope = rememberCoroutineScope()

    val sortedPairs = if (forecastModeSetting.value == ForecastMode.DIFFERENT_PLACES)
        remember {
            derivedStateOf {
                forecastWeatherState.value?.let { weathers ->
                    val pairs = placesList.value.zip(weathers)
                    getSortedPairsList(pairs, sortingMode.value, viewModel)
                }
            }
        } else null

    LaunchedEffect(key1 = Unit) {
        fieldsToShow = dataStoreHelper.getWeatherQueries()
    }


    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            WeatherItem(
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .animateContentSize(
                        animationSpec = tween(
                            durationMillis = 200,
                            easing = EaseOut
                        )
                    ),
                universalWeatherState = currentWeatherState.value,
                weatherQuery = fieldsToShow,
                placeState = placeState.value,
                coordsState = coordsState.value,
                showPlace = true,
                justPlace = false,
                loadingState = loadingState.value
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 8.dp)
            ) {
                if (!loadingState.value.canShowWeather) {
                    GettingDataScreen(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .align(BiasAlignment(0f, -0.2f))
                    )
                } else {
                    if (forecastModeSetting.value == ForecastMode.DIFFERENT_PLACES) {
                        UniversalForecastWeatherList(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            universalWeatherState = sortedPairs?.value?.map { it.second },
                            weatherQuery = fieldsToShow,
                            columnState = columnState,
                            placeStates = sortedPairs?.value?.map { it.first }?.toSet(),
                            showPlace = true
                        )
                    } else {
                        UniversalForecastWeatherList(
                            modifier = Modifier.padding(horizontal = 8.dp),
                            universalWeatherState = forecastWeatherState.value,
                            weatherQuery = fieldsToShow,
                            columnState = columnState,
                            placeStates = null,
                            showPlace = false
                        )
                    }
                }
                this@Column.AnimatedVisibility(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(16.dp),
                    visible = isScrolled,
                    enter = slideInVertically(
                        animationSpec = tween(durationMillis = 200),
                        initialOffsetY = { it }
                    ),
                    exit = slideOutVertically(
                        animationSpec = tween(durationMillis = 200),
                        targetOffsetY = { it }
                    )
                ) {

                    UpFAB(
                        modifier = Modifier,
                        localScope = localScope,
                        columnState = columnState
                    )

                }
            }
        }
        PullRefreshIndicator(
            refreshing = refreshing,
            state = pullRefreshState,
            modifier = Modifier.align(Alignment.TopCenter)
        )
    }
    if (errorsState.value != Errors.UNSPECIFIED && errorsState.value != Errors.NO_ERRORS)
        ToastShowing(message = errorsState.value.message)
}

private fun getSortedPairsList(
    pairs: List<Pair<Site, UniversalWeatherState?>>,
    mode: PlacesSortingMode,
    viewModel: WeatherViewModel
): List<Pair<Site, UniversalWeatherState?>> {
    val currentCoords = viewModel.coords.value
    val distance = floatArrayOf(0f)
    val pairsWithDistance = pairs.map { pair ->
        if (currentCoords != null) {
            Location.distanceBetween(
                currentCoords.latitude, currentCoords.longitude,
                pair.first.location.lat, pair.first.location.lng, distance
            )
        }
        Pair(pair, distance[0])
    }
    val newList = when (mode) {
        PlacesSortingMode.UNSORTED -> pairsWithDistance
        PlacesSortingMode.DISTANCE -> {
            pairsWithDistance.sortedBy { it.second }
        }

        PlacesSortingMode.NAME -> {
            pairsWithDistance.sortedBy { it.first.first.formatAddress }
        }
    }
    return newList.map { it.first }
}