package ru.lisss79.weatherforecast.entities

enum class Errors(val message: String) {
    NO_ERRORS("Data got"),
    NO_COORDS("Can't get current location"),
    NO_WEATHER("Can't get current weather"),
    NO_INTERNET("Internet is not avaivable"),
    UNSPECIFIED(""),
    NO_PLACE("Can't get location name");
}
