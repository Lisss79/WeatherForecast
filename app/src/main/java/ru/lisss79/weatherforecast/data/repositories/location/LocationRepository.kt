package ru.lisss79.weatherforecast.data.repositories.location

import android.location.Location

interface LocationRepository {
    suspend fun getCoords(): Result<Location>
}