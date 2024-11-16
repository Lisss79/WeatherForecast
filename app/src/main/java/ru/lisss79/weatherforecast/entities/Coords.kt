package ru.lisss79.weatherforecast.entities

data class Coords(
    val latitude: Double,
    val longitude: Double
) {
    fun toStringForRequest() = "$latitude,$longitude"
}
