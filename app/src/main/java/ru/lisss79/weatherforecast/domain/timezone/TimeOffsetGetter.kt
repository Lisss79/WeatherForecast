package ru.lisss79.weatherforecast.domain.timezone

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.repositories.timezone.TimeZoneRepository
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.copy

interface TimeOffsetGetter {
    val timeZoneRepository: TimeZoneRepository

     suspend fun getSiteWithTimeZone(site: Site): Site {
        val timeZoneOffset = timeZoneRepository
            .getTimeOffset(Coords(site.location.lat, site.location.lng)).getOrNull()
        return site.copy(timeZoneOffset)
    }

    suspend fun getSiteWithTimeZone(sites: List<Site>): List<Site> {
        val newSitesList = sites.map { site ->
            getSiteWithTimeZone(site)
        }
        return newSitesList
    }
}