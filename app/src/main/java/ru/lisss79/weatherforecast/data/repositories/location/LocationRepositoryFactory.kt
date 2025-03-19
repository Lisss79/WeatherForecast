package ru.lisss79.weatherforecast.data.repositories.location

import org.kodein.di.DI
import org.kodein.di.instance
import ru.lisss79.weatherforecast.di.GOOGLE
import ru.lisss79.weatherforecast.di.HUAWEI

class LocationRepositoryFactory(val di: DI) {
    fun getRepository(locationRepository: LocationRepositoryVariant): LocationRepository {
        val repoHuawei by di.instance<LocationRepository>(tag = HUAWEI)
        val repoGoogle by di.instance<LocationRepository>(tag = GOOGLE)
        return when (locationRepository) {
            LocationRepositoryVariant.HUAWEI -> repoHuawei
            LocationRepositoryVariant.GOOGLE -> repoGoogle
        }
    }
}

enum class LocationRepositoryVariant(val menuName: String) {
    HUAWEI("Huawei Location Service"),
    GOOGLE("Google LocationService");

    companion object {
        val default = GOOGLE
    }
}