package ru.lisss79.weatherforecast.domain.coords

import ru.lisss79.weatherforecast.data.repositories.location.LocationRepository

class GetCoordsUseCase(locationRepository: LocationRepository) : CoordsUseCase(locationRepository) {
    suspend operator fun invoke() = locationRepository.getCoords()

}