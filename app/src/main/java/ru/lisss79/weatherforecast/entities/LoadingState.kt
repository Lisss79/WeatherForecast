package ru.lisss79.weatherforecast.entities

enum class LoadingState(val message: String, val canShowWeather: Boolean) {
    FINISHED("Data got", true),
    LOADING_WEATHER("Getting weather", false),
    LOADING_COORDS("Getting GPR coordinates", false),
    LOADING_PLACE("Getting place", true);
}