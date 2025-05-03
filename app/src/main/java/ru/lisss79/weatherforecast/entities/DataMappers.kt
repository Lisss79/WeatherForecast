package ru.lisss79.weatherforecast.entities

import android.location.Address
import com.huawei.hms.site.api.model.AddressDetail
import com.huawei.hms.site.api.model.Coordinate
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.entities.geocoder.maps_co.GeocoderMapsCoApiResponse
import ru.lisss79.weatherforecast.entities.geocoder.yandex.FeatureMember
import ru.lisss79.weatherforecast.entities.geocoder.yandex.GeocoderYandexApiResponse
import ru.lisss79.weatherforecast.entities.weather.CurrentWeatherState
import ru.lisss79.weatherforecast.entities.weather.DailyWeatherState
import ru.lisss79.weatherforecast.entities.weather.HourlyWeatherState
import ru.lisss79.weatherforecast.entities.weather.UniversalTime
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.entities.weather.WeatherData
import java.time.DateTimeException
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeParseException

object DataMappers {

    fun mapscoApiResponseToSite(apiResponse: GeocoderMapsCoApiResponse?): Site? {
        if (apiResponse?.error != null || apiResponse?.address == null) return null
        val coordinate = apiResponse.getCoordinates()
        val formatAdr = apiResponse.displayName
        val addressDetail = apiResponse.address.let { address ->
            AddressDetail().apply {
                countryCode = address?.countryCode
                country = address?.country
                adminArea = address?.state
                locality = address?.city
                subLocality = address?.town ?: address?.village
                subAdminArea = address?.county
                tertiaryAdminArea = address?.region ?: address?.neighbourhood
            }
        }
        val site = addressDetail.let { adr ->
            Site().apply {
                this.address = adr
                this.formatAddress = formatAdr
                this.location = coordinate
            }
        }
        return site
    }

    fun mapscoApiResponseToSites(apiResponses: List<GeocoderMapsCoApiResponse>?): List<Site>? {
        if (apiResponses == null) return null
        val sites = mutableListOf<Site>()
        apiResponses.forEach { response ->
            val coordinate = response.getCoordinates()
            val formatAdr = response.displayName
            val adrDetails = formatAdr?.split(',')
            val addressDetail = response.address.let { address ->
                AddressDetail().apply {
                    country = adrDetails?.lastOrNull()
                    locality = adrDetails?.firstOrNull()
                    subAdminArea =
                        if (adrDetails != null && adrDetails.size > 2) adrDetails[1] else null
                }
            }
            val site = addressDetail.let { adr ->
                Site().apply {
                    this.address = adr
                    this.formatAddress = formatAdr
                    this.location = coordinate
                }
            }

            sites.add(site)
        }
        return sites.ifEmpty { null }
    }

    fun yandexApiResponseToSite(apiResponse: GeocoderYandexApiResponse?): Site? {
        val featureMember = apiResponse?.response?.GeoObjectCollection
            ?.featureMember?.get(0)
        return featureMemberToSite(featureMember)
    }

    fun yandexApiResponseToSites(apiResponse: GeocoderYandexApiResponse?): List<Site>? {
        val featureMembers = apiResponse?.response?.GeoObjectCollection
            ?.featureMember
        val sites = featureMembers?.mapNotNull { featureMemberToSite(it) }
        return sites
    }

    private fun featureMemberToSite(featureMember: FeatureMember?): Site? {
        if (featureMember == null) return null

        val pos = featureMember.GeoObject?.Point?.pos
        val coords = getCoordsFromPos(pos)
        val address = featureMember.GeoObject?.metaDataProperty
            ?.GeocoderMetaData?.Address
        val countryCode = address?.countryCode
        val addressParts = address?.Components
        val formatAdr = address?.formatted

        val addressDetail = addressParts?.let { parts ->
            AddressDetail().apply {
                this.countryCode = countryCode
                country = parts.firstOrNull { it.kind == "country" }?.name
                adminArea = parts.lastOrNull { it.kind == "province" }?.name
                locality = parts.firstOrNull { it.kind == "locality" }?.name
                subAdminArea = parts.firstOrNull { it.kind == "area" }?.name
                tertiaryAdminArea = parts.firstOrNull { it.kind == "airport" }?.name
                    ?: parts.firstOrNull { it.kind == "station" }?.name
                            ?: parts.firstOrNull { it.kind == "hydro" }?.name
                            ?: parts.firstOrNull { it.kind == "other" }?.name
            }
        }

        val site = addressDetail?.let { adr ->
            Site().apply {
                this.address = adr
                this.formatAddress = formatAdr
                this.location = coords
            }
        }

        return site
    }

    private fun getCoordsFromPos(pos: String?): Coordinate? {
        if (pos == null) return null
        val list = pos.split(" ")
        val coords = try {
            Coordinate(list[1].toDouble(), list[0].toDouble())
        } catch(exc: Exception) {
            exc.printStackTrace()
            null
        }
        return coords
    }

    fun addressToSite(androidAddress: Address?): Site? {
        if (androidAddress == null) return null
        val formatAdr = androidAddress.getAddressLine(0)
        val addressDetail = androidAddress.let {
            AddressDetail().apply {
                countryCode = androidAddress.countryCode
                country = androidAddress.countryName
                adminArea = androidAddress.adminArea
                locality = androidAddress.locality
                subAdminArea = androidAddress.subAdminArea
                subLocality = androidAddress.subAdminArea
            }
        }
        val coordinate = Coordinate(
            androidAddress.latitude,
            androidAddress.longitude
        )
        val site = addressDetail.let { adr ->
            Site().apply {
                this.address = adr
                this.location = coordinate
                this.formatAddress = formatAdr
            }
        }
        return site
    }

    fun currentAndHourlyDataToState(weatherData: WeatherData?): List<UniversalWeatherState?> {
        val hourlyWeatherStateArray = mutableListOf<UniversalWeatherState?>()
        hourlyWeatherStateArray.add(currentDataToState(weatherData))
        hourlyWeatherStateArray.addAll(hourlyToStateArray(weatherData))
        return hourlyWeatherStateArray
    }

    fun currentAndDailyDataToState(weatherData: WeatherData?): List<UniversalWeatherState?> {
        val dailyWeatherStateArray = mutableListOf<UniversalWeatherState?>()
        dailyWeatherStateArray.add(currentDataToState(weatherData))
        dailyWeatherStateArray.addAll(dailyToStateArray(weatherData))
        return dailyWeatherStateArray
    }

    fun currentDataToState(weatherData: WeatherData?): UniversalWeatherState? {
        if (weatherData == null) return null
        return with (weatherData.current) {
            UniversalWeatherState.Current(
                universalTime = UniversalTime
                    .DateTime(parseZonedDateTime(this?.time, weatherData.timezone)),
                weatherCode =  this?.weatherCode,
                current = CurrentWeatherState(
                    time = parseZonedDateTime(this?.time, weatherData.timezone),
                    temperature = this?.temperature2m,
                    humidity = this?.relativeHumidity2m,
                    windSpeed = this?.windSpeed10m,
                    windDirection = this?.windDirection10m,
                    precipitation = this?.precipitation,
                    rain = this?.rain,
                    showers = this?.showers,
                    snowfall = this?.snowfall,
                    surfacePressure = this?.surfacePressure,
                    cloudCover = this?.cloudCover,
                    weatherCode = this?.weatherCode,
                    isDay = this?.isDay?.toBoolean()
                )
            )
        }
    }

    private fun hourlyToStateArray(weatherData: WeatherData?): List<UniversalWeatherState?> {
        val data = mutableListOf<UniversalWeatherState>()
        if (weatherData == null) return data

        weatherData.hourly?.let {
            val length = it.time.size
            for (index in 0 until length) {
                val item = UniversalWeatherState.Hourly(
                    universalTime = UniversalTime
                        .DateTime(parseZonedDateTime(it.time[index], weatherData.timezone)),
                    weatherCode = getDataOrNull(it.weatherCode, index),
                    hourly = HourlyWeatherState(
                        time = parseZonedDateTime(it.time[index], weatherData.timezone),
                        temperature = getDataOrNull(it.temperature2m, index),
                        humidity = getDataOrNull(it.relativeHumidity2m, index),
                        windSpeed = getDataOrNull(it.windSpeed10m, index),
                        windDirection = getDataOrNull(it.windDirection10m, index),
                        precipitation = getDataOrNull(it.precipitation, index),
                        rain = getDataOrNull(it.rain, index),
                        showers = getDataOrNull(it.showers, index),
                        snowfall = getDataOrNull(it.snowfall, index),
                        surfacePressure = getDataOrNull(it.surfacePressure, index),
                        cloudCover = getDataOrNull(it.cloudCover, index),
                        weatherCode = getDataOrNull(it.weatherCode, index),
                        isDay = getDataOrNull(it.isDay.toBooleanArray(), index),
                    )
                )
                data.add(item)
            }
        }
        return data
    }

    private fun dailyToStateArray(weatherData: WeatherData?): List<UniversalWeatherState?> {
        val data = mutableListOf<UniversalWeatherState>()
        if (weatherData == null) return data

        weatherData.daily?.let {
            val length = it.time.size
            for (index in 0 until length) {
                val item = UniversalWeatherState.Daily(
                    universalTime = UniversalTime.DateOnly(parseLocalDate(it.time[index])),
                    weatherCode = getDataOrNull(it.weatherCode, index),
                    daily = DailyWeatherState(
                        time = parseLocalDate(it.time[index]),
                        temperatureMin = getDataOrNull(it.temperature2mMin, index),
                        temperatureMax = getDataOrNull(it.temperature2mMax, index),
                        windSpeedMax = getDataOrNull(it.windSpeed10mMax, index),
                        windGustsMax = getDataOrNull(it.windGusts10mMax, index),
                        windDirectionDominant = getDataOrNull(it.windDirection10mDominant, index),
                        precipitationSum = getDataOrNull(it.precipitationSum, index),
                        precipitationProbability = getDataOrNull(it.precipitationProbabilityMean, index),
                        rainSum = getDataOrNull(it.rainSum, index),
                        showersSum = getDataOrNull(it.showersSum, index),
                        snowfallSum = getDataOrNull(it.snowfallSum, index),
                        weatherCode = getDataOrNull(it.weatherCode, index),
                        sunrise = parseLocalTime(it.sunrise[index]),
                        sunset = parseLocalTime(it.sunset[index]),
                    )
                )
                data.add(item)
            }
        }
        return data
    }

    private fun <T> getDataOrNull(list: List<T>, index: Int) =
        if (index < list.size) list[index]
        else null

    private fun parseZonedDateTime(time: String?, timeZoneId: String?) =
        try {
            LocalDateTime.parse(time).atZone(ZoneId.of(timeZoneId))
        } catch (e: DateTimeException) {
            e.printStackTrace()
            null
        }

    private fun parseLocalDate(time: String?) =
        try {
            LocalDate.parse(time)
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }

    private fun parseLocalTime(time: String?) =
        try {
            LocalDateTime.parse(time).toLocalTime()
        } catch (e: DateTimeParseException) {
            e.printStackTrace()
            null
        }
}