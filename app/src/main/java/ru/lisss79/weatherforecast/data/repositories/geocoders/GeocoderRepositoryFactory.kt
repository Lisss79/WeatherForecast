package ru.lisss79.weatherforecast.data.repositories.geocoders

import org.kodein.di.DI
import org.kodein.di.instance
import ru.lisss79.weatherforecast.di.ANDROID
import ru.lisss79.weatherforecast.di.MAPSCO
import ru.lisss79.weatherforecast.di.YANDEX

class GeocoderRepositoryFactory(val di: DI) {
    fun getRepository(geocoderRepository: GeocoderRepositoryVariant): GeocoderRepository {
        val repoYandex by di.instance<GeocoderRepository>(tag = YANDEX)
        val repoMapsco by di.instance<GeocoderRepository>(tag = MAPSCO)
        val repoAndroid by di.instance<GeocoderRepository>(tag = ANDROID)
        return when (geocoderRepository) {
            GeocoderRepositoryVariant.YANDEX -> repoYandex
            GeocoderRepositoryVariant.ANDROID -> repoAndroid
            GeocoderRepositoryVariant.MAPSCO -> repoMapsco
        }
    }
}

enum class GeocoderRepositoryVariant(val menuName: String) {
    YANDEX("Yandex Geocoder Api"),
    MAPSCO("Maps.co Geocoder Api"),
    ANDROID("Standard Android");

    companion object {
        val default = ANDROID
    }
}