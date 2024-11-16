package ru.lisss79.weatherforecast.data.repositories.geocoders

import android.location.Address
import android.location.Geocoder
import android.os.Build
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.app.ForecastApp
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.DataMappers
import ru.lisss79.weatherforecast.entities.weather.WeatherException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GeocoderRepositoryAndroid : GeocoderRepository {

    override suspend fun getPlaceByCoords(coords: Coords): Result<Site> {
        val geocoder = getGeocoder()
            ?: return Result.failure(WeatherException("Can't get android geocoder"))

        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCoroutine { continuation ->
                geocoder.getFromLocation(
                    coords.latitude,
                    coords.longitude,
                    1,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isNotEmpty())
                                continuation.resume(
                                    Result.success(DataMappers.addressToSite(addresses[0])!!)
                                )
                            else continuation.resume(
                                Result.failure(WeatherException("Can't get address"))
                            )
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resume(
                                Result.failure(WeatherException(errorMessage ?: "Unknown error"))
                            )
                        }
                    }
                )
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(
                coords.latitude,
                coords.longitude,
                1
            )
            if (!addresses.isNullOrEmpty())
                Result.success(DataMappers.addressToSite(addresses[0])!!)
            else
                Result.failure(WeatherException("Can't get address"))
        }

        return result
    }

    override suspend fun getPlacesByName(name: String): Result<List<Site>> {
        val geocoder = getGeocoder()
            ?: return Result.failure(WeatherException("Can't get android geocoder"))

        val result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            suspendCoroutine { continuation ->
                geocoder.getFromLocationName(
                    name,
                    5,
                    object : Geocoder.GeocodeListener {
                        override fun onGeocode(addresses: MutableList<Address>) {
                            if (addresses.isNotEmpty()) {
                                continuation.resume(
                                    Result.success(
                                        addresses.map { DataMappers.addressToSite(it)!! }
                                    )
                                )
                            } else continuation.resume(
                                Result.failure(WeatherException("Can't get address"))
                            )
                        }

                        override fun onError(errorMessage: String?) {
                            continuation.resume(
                                Result.failure(WeatherException(errorMessage ?: "Unknown error"))
                            )
                        }
                    }
                )
            }
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocationName(
                name,
                5
            )
            if (!addresses.isNullOrEmpty())
                Result.success(addresses.filterNotNull().map { DataMappers.addressToSite(it)!! })
            else
                Result.failure(WeatherException("Can't get address"))
        }

        return result

    }

    private fun getGeocoder(): Geocoder? {
        if (!Geocoder.isPresent()) return null
        val geocoder = ForecastApp.instance?.applicationContext?.let { context ->
            Geocoder(context)
        }
        return geocoder
    }

}