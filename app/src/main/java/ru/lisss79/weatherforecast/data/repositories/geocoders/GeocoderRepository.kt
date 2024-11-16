package ru.lisss79.weatherforecast.data.repositories.geocoders

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.entities.Coords

interface GeocoderRepository {
    suspend fun getPlaceByCoords(coords: Coords): Result<Site>
    suspend fun getPlacesByName(name: String): Result<List<Site>>
}