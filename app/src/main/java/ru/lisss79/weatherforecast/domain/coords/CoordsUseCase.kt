package ru.lisss79.weatherforecast.domain.coords

import ru.lisss79.weatherforecast.data.repositories.location.LocationRepository
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryFactory

abstract class CoordsUseCase(val locationRepositoryFactory: LocationRepositoryFactory) {
}