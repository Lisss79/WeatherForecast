package ru.lisss79.weatherforecast.domain.places

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepository
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.data.repositories.timezone.TimeZoneRepository
import ru.lisss79.weatherforecast.domain.timezone.TimeOffsetGetter

class GetPlacesByNameUseCase(
    geocoderRepositoryFactory: GeocoderRepositoryFactory,
    override val timeZoneRepository: TimeZoneRepository
) :
    PlacesUseCase(geocoderRepositoryFactory), TimeOffsetGetter {
    private lateinit var repository: GeocoderRepository

    suspend operator fun invoke(
        name: String,
        geocoderRepository: GeocoderRepositoryVariant
    ): Result<List<Site>> {
        repository = geocoderRepositoryFactory.getRepository(geocoderRepository)
        val sites = repository.getPlacesByName(name)
        return if (sites.isSuccess) {
            Result.success(getSiteWithTimeZone(sites.getOrNull()!!))
        } else sites
    }
}