package ru.lisss79.weatherforecast.domain.places

import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryFactory

abstract class PlacesUseCase(val geocoderRepositoryFactory: GeocoderRepositoryFactory) {
}