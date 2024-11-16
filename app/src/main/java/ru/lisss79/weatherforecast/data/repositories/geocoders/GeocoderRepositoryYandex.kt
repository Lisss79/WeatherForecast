package ru.lisss79.weatherforecast.data.repositories.geocoders

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.retrofit.GeocoderYandexApi
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.DataMappers
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.weather.WeatherException

class GeocoderRepositoryYandex(
    private val geocoderYandexApi: GeocoderYandexApi
) :  GeocoderRepository {

    override suspend fun getPlaceByCoords(coords: Coords): Result<Site> {
        val response = kotlin.runCatching {
            geocoderYandexApi.getAddress(Values.YANDEX_API_KEY, coords.toStringForRequest())
        }
        val result = response.fold(
            onSuccess = {
                val site = DataMappers.yandexApiResponseToSite(it.body())
                if (site != null) Result.success(site)
                else Result.failure(WeatherException("No info about place"))
            },
            onFailure = { Result.failure(it) }
        )

        return result
    }

    override suspend fun getPlacesByName(name: String): Result<List<Site>> {
        val response = kotlin.runCatching {
            geocoderYandexApi.getPlace(Values.YANDEX_API_KEY, name)
        }
        val result = response.fold(
            onSuccess = {
                val sites = DataMappers.yandexApiResponseToSites(it.body())
                if (sites != null) Result.success(sites)
                else Result.failure(WeatherException("No info about place"))
            },
            onFailure = { Result.failure(it) }
        )

        return result
    }

}