package ru.lisss79.weatherforecast.domain.coords

import ru.lisss79.weatherforecast.data.repositories.location.LocationRepository

abstract class CoordsUseCase(val locationRepository: LocationRepository) {
}