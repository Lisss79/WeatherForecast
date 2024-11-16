package ru.lisss79.weatherforecast.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.CoroutineScope
import ru.lisss79.weatherforecast.data.datastore.DataStoreHelper
import ru.lisss79.weatherforecast.entities.SettingsScreens
import ru.lisss79.weatherforecast.ui.items.TopPlacesBar
import ru.lisss79.weatherforecast.ui.items.TopSettingsBar
import ru.lisss79.weatherforecast.ui.items.TopWeatherBar
import ru.lisss79.weatherforecast.ui.items.settings.WeatherDetailsSettingsScreen
import ru.lisss79.weatherforecast.ui.mainelements.MainSettingsScreen
import ru.lisss79.weatherforecast.ui.mainelements.MainWeatherScreen
import ru.lisss79.weatherforecast.ui.mainelements.PlacesScreen
import ru.lisss79.weatherforecast.ui.mainelements.WeatherViewModel
import ru.lisss79.weatherforecast.ui.mainelements.refreshData

@Composable
fun BaseNavigationElement(
    scope: CoroutineScope,
    dataStoreHelper: DataStoreHelper,
    viewModel: WeatherViewModel
) {
    val navController = rememberNavController()
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = NavigationWeatherScreen.route
    ) {
        composable(route = NavigationWeatherScreen.route) {
            WeatherScreenLayout(
                scope = scope,
                viewModel = viewModel,
                navigateToSettings = {
                    navController.navigate(NavigationSettingsScreen.route)
                },
                dataStoreHelper = dataStoreHelper,
                navigateToPlaces = {
                    navController.navigate(NavigationPlacesScreen.route)
                }
            )
        }

        navigation(
            startDestination = NavigationMainSettingsScreen.route,
            route = NavigationSettingsScreen.route
        ) {
            composable(route = NavigationMainSettingsScreen.route) {
                viewModel.resetError()
                SettingsScreenLayout(
                    settingsScreen = SettingsScreens.MAIN,
                    dataStoreHelper = dataStoreHelper,
                    onBackPressed = { forceUpdate, forecastModeChanged ->
                        if (forecastModeChanged) viewModel.removeForecastWeather()
                        navController.navigateUp()
                        if (forceUpdate) refreshData(
                            viewModel = viewModel,
                            dataStoreHelper = dataStoreHelper,
                            forceUpdate = true,
                            scope = scope
                        )
                    },
                    onSubmenuClick = {
                        navController.navigate(NavigationWeatherDetailsSettingsScreen.route)
                    }
                )
            }
            composable(route = NavigationWeatherDetailsSettingsScreen.route) {
                SettingsScreenLayout(
                    settingsScreen = SettingsScreens.DETAILS,
                    dataStoreHelper = dataStoreHelper,
                    onBackPressed = { forceUpdate, _ ->
                        navController.navigateUp()
                        if (forceUpdate) refreshData(
                            viewModel = viewModel,
                            dataStoreHelper = dataStoreHelper,
                            forceUpdate = true,
                            scope = scope
                        )
                    }
                )
            }
        }


        composable(route = NavigationPlacesScreen.route) {
            viewModel.resetError()
            PlacesScreenLayout(
                dataStoreHelper = dataStoreHelper,
                viewModel = viewModel,
                onBackPressed = { forceUpdateCoords ->
                    navController.navigateUp()
                    if (forceUpdateCoords) refreshData(
                        viewModel = viewModel,
                        dataStoreHelper = dataStoreHelper,
                        forceUpdate = true,
                        scope = scope
                    )
                }
            )
        }
    }
}

@Composable
fun WeatherScreenLayout(
    scope: CoroutineScope,
    viewModel: WeatherViewModel,
    dataStoreHelper: DataStoreHelper,
    navigateToSettings: () -> Unit,
    navigateToPlaces: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopWeatherBar(
                onSettingsSelection = navigateToSettings,
                onPlacesSelection = navigateToPlaces
            )

        }
    ) { innerPadding ->
        MainWeatherScreen(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 4.dp),
            viewModel = viewModel,
            dataStoreHelper = dataStoreHelper,
            scope = scope,
        )
    }
}

@Composable
fun SettingsScreenLayout(
    settingsScreen: SettingsScreens,
    dataStoreHelper: DataStoreHelper,
    onBackPressed: (Boolean, Boolean) -> Unit,
    onSubmenuClick: () -> Unit = { }
) {
    var forceUpdate by rememberSaveable { mutableStateOf(false) }
    var forecastModeChanged by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopSettingsBar(
                onBackPressed = {
                    onBackPressed(forceUpdate, forecastModeChanged)
                }
            )
        }
    ) { innerPadding ->
        when (settingsScreen) {
            SettingsScreens.MAIN -> {
                MainSettingsScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(vertical = 4.dp),
                    dataStoreHelper = dataStoreHelper,
                    onSettingsChanged = {
                        forceUpdate = true
                    },
                    onForecastModeChanged = {
                        forecastModeChanged = true
                    },
                    onDetailsClick = onSubmenuClick
                )
            }
            SettingsScreens.DETAILS -> {
                WeatherDetailsSettingsScreen(
                    modifier = Modifier
                        .padding(innerPadding)
                        .padding(vertical = 4.dp),
                    dataStoreHelper = dataStoreHelper,
                    onDetailsSettingsChanged = {
                        forceUpdate = true
                    }
                )
            }
        }
    }
}

@Composable
fun PlacesScreenLayout(
    viewModel: WeatherViewModel,
    dataStoreHelper: DataStoreHelper,
    onBackPressed: (Boolean) -> Unit
) {
    var forceUpdateCoords by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopPlacesBar(
                onBackPressed = {
                    onBackPressed(forceUpdateCoords)
                }
            )
        }
    ) { innerPadding ->
        PlacesScreen(
            modifier = Modifier
                .padding(innerPadding)
                .padding(vertical = 4.dp),
            viewModel = viewModel,
            dataStoreHelper = dataStoreHelper,
            onPlacesChanged = {
                forceUpdateCoords = true
            }
        )
    }
}


@Preview
@Composable
fun SettingsScreenLayoutPreview() {
    //SettingsScreen { }
}
