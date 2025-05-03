package ru.lisss79.weatherforecast.domain.places

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepository
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant

class GetPlacesByNameUseCase(
    geocoderRepositoryFactory: GeocoderRepositoryFactory
) :
    PlacesUseCase(geocoderRepositoryFactory) {
    private lateinit var repository: GeocoderRepository

    suspend operator fun invoke(
        name: String,
        geocoderRepository: GeocoderRepositoryVariant
    ): Result<List<Site>> {
        repository = geocoderRepositoryFactory.getRepository(geocoderRepository)
        val sites = repository.getPlacesByName(name)
        return sites
    }
}