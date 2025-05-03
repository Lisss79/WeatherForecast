package ru.lisss79.weatherforecast.ui.mainelements

import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.SettingsGroup
import com.alorma.compose.settings.ui.SettingsMenuLink
import com.alorma.compose.settings.ui.SettingsRadioButton
import kotlinx.coroutines.launch
import ru.lisss79.weatherforecast.data.datastore.DataStoreHelper
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryVariant
import ru.lisss79.weatherforecast.entities.ForecastMode

@Composable
fun MainSettingsScreen(
    modifier: Modifier = Modifier,
    dataStoreHelper: DataStoreHelper,
    onSettingsChanged: () -> Unit,
    onForecastModeChanged: () -> Unit,
    onDetailsClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(state = rememberScrollState())
    ) {
        val selectedLocationService = dataStoreHelper
            .locationRepositoryFlow.collectAsStateWithLifecycle(LocationRepositoryVariant.default)
        val selectedGeocoder = dataStoreHelper
            .geocoderRepositoryFlow.collectAsStateWithLifecycle(GeocoderRepositoryVariant.default)
        val selectedForecastMode = dataStoreHelper
            .forecastModeFlow.collectAsStateWithLifecycle(ForecastMode.default)

        SettingsGroup(
            modifier = Modifier,
            enabled = true,
            title = {
                Text(
                    text = "Geocoder Selection",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        ) {
            for (geocoderVariant in GeocoderRepositoryVariant.entries) {
                val selected = geocoderVariant == selectedGeocoder.value
                SettingsRadioButton(
                    state = selected,
                    title = { Text(geocoderVariant.menuName) },
                    onClick = {
                        if (!selected) {
                            scope.launch {
                                dataStoreHelper.setGeocoderRepository(geocoderVariant)
                                onSettingsChanged()
                            }
                        }
                    },
                )
            }
        }
        SettingsGroup(
            modifier = Modifier,
            enabled = true,
            title = {
                Text(
                    text = "Location Services Selection",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        ) {
            for (locationVariant in LocationRepositoryVariant.entries) {
                val selected = locationVariant == selectedLocationService.value
                SettingsRadioButton(
                    state = selected,
                    title = { Text(locationVariant.menuName) },
                    onClick = {
                        if (!selected) {
                            scope.launch {
                                dataStoreHelper.setLocationRepository(locationVariant)
                                onSettingsChanged()
                            }
                        }
                    },
                )
            }
        }
        SettingsGroup(
            modifier = Modifier,
            enabled = true,
            title = {
                Text(
                    text = "Forecast Mode",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall
                )

            }
        ) {
            for (forecastModeVariant in ForecastMode.entries) {
                val selected = forecastModeVariant == selectedForecastMode.value
                SettingsRadioButton(
                    state = selected,
                    title = { Text(forecastModeVariant.menuName) },
                    onClick = {
                        if (!selected)
                        scope.launch {
                            dataStoreHelper.setForecastMode(forecastModeVariant)
                            onSettingsChanged()
                            onForecastModeChanged()
                        }
                    },
                )
            }
        }
        SettingsMenuLink(
            title = {
                Text(
                    text = "Weather details settings",
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            modifier = Modifier.padding(vertical = 8.dp),
            onClick = onDetailsClick
        )
    }
}

@Composable
fun BackPressHandler(
    onBackPress: () -> Unit
) {
    val backPressedDispatcher: OnBackPressedDispatcher? =
        LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    val backCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            onBackPress()
        }
    }
    DisposableEffect(key1 = backPressedDispatcher) {
        backPressedDispatcher?.addCallback(backCallback)
        onDispose {
            backCallback.remove()
        }
    }
}

@Preview
@Composable
fun SettingsScreenPreview() {
    //SettingsScreen()
}
