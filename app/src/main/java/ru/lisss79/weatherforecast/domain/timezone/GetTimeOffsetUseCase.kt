package ru.lisss79.weatherforecast.domain.timezone

import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.data.repositories.timezone.TimeZoneRepository

class GetTimeOffsetUseCase(
    override val timeZoneRepository: TimeZoneRepository
) : TimeOffsetGetter {
    suspend operator fun invoke(site: Site) = getSiteWithTimeZone(site)
}