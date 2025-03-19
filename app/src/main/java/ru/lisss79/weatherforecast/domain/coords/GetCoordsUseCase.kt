package ru.lisss79.weatherforecast.domain.coords

import android.location.Location
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepository
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepository
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryVariant

class GetCoordsUseCase(locationRepositoryFactory: LocationRepositoryFactory)
    : CoordsUseCase(locationRepositoryFactory) {
    private lateinit var repository: LocationRepository

    suspend operator fun invoke(locationRepository: LocationRepositoryVariant): Result<Location> {
        repository = locationRepositoryFactory.getRepository(locationRepository)
        val coords = repository.getCoords()
        return coords
    }
}