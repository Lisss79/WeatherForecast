package ru.lisss79.weatherforecast.data.repositories.geocoders

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.retrofit.GeocoderMapsCoApi
import ru.lisss79.weatherforecast.data.retrofit.GeocoderYandexApi
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.DataMappers
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.weather.WeatherException

class GeocoderRepositoryMapsCo(
    private val geocoderMapsCoApi: GeocoderMapsCoApi
) :  GeocoderRepository {

    override suspend fun getPlaceByCoords(coords: Coords): Result<Site> {
        val response = kotlin.runCatching {
            geocoderMapsCoApi.getAddress(Values.MAPSCO_API_KEY, coords.latitude, coords.longitude)
        }
        val result = response.fold(
            onSuccess = {
                val site = DataMappers.mapscoApiResponseToSite(it.body())
                if (site != null) Result.success(site)
                else Result.failure(WeatherException("No info about place"))
            },
            onFailure = { Result.failure(it) }
        )

        return result
    }

    override suspend fun getPlacesByName(name: String): Result<List<Site>> {
        val response = kotlin.runCatching {
            geocoderMapsCoApi.getPlace(Values.MAPSCO_API_KEY, name)
        }
        val result = response.fold(
            onSuccess = {
                val sites = DataMappers.mapscoApiResponseToSites(it.body())?.take(5)
                if (sites != null) Result.success(sites)
                else Result.failure(WeatherException("No info about place"))
            },
            onFailure = { Result.failure(it) }
        )

        return result
    }

}