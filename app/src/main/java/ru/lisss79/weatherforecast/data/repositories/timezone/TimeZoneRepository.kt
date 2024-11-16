package ru.lisss79.weatherforecast.data.repositories.timezone

import ru.lisss79.weatherforecast.entities.Coords

interface TimeZoneRepository {
    // Возвращаем сдвиг в МИНУТАХ!
    suspend fun getTimeOffset(coords: Coords): Result<Int>
}