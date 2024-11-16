package ru.lisss79.weatherforecast.ui.navigation

object NavigationWeatherScreen : NavigationScreens {
    override val route = "WeatherScreen"
}

object NavigationSettingsScreen : NavigationScreens {
    override val route = "SettingsScreen"
}

object NavigationMainSettingsScreen : NavigationScreens {
    override val route = "MainSettingsScreen"
}

object NavigationWeatherDetailsSettingsScreen : NavigationScreens {
    override val route = "WeatherDetailsSettingsScreen"
}

object NavigationPlacesScreen : NavigationScreens {
    override val route = "PlacesScreen"
}

interface NavigationScreens {
    val route: String
}