package ru.lisss79.weatherforecast.domain.places

import android.location.Location
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepository
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.weather.WeatherException

class GetPlaceByCoordsUseCase(
    geocoderRepositoryFactory: GeocoderRepositoryFactory,
    //override val timeZoneRepository: TimeZoneRepository
) :
    PlacesUseCase(geocoderRepositoryFactory) {
    private lateinit var repository: GeocoderRepository

    suspend operator fun invoke(
        geocoderRepository: GeocoderRepositoryVariant,
        currCoords: Coords,
        oldCoords: Coords?,
        oldPlace: Site?,
        forceUpdatePlace: Boolean
    ): Result<Site> {
        repository = geocoderRepositoryFactory.getRepository(geocoderRepository)

        return if (needToChangePlace(currCoords, oldCoords) || forceUpdatePlace) {
            repository.getPlaceByCoords(currCoords)
        } else {
            if (oldPlace != null) Result.success(oldPlace)
            else Result.failure(WeatherException("There is no determinate place"))
        }
    }


    private fun needToChangePlace(newCoords: Coords, oldCoords: Coords?) =
        if (oldCoords != null) {
            val currLocation = Location("currLocationProvider").apply {
                latitude = newCoords.latitude
                longitude = newCoords.longitude
            }
            val oldLocation = Location("oldLocationProvider").apply {
                latitude = oldCoords.latitude
                longitude = oldCoords.longitude
            }
            val distance = currLocation.distanceTo(oldLocation)
            if (distance > Values.locationSensitivityMeters) true else false
        } else true

}