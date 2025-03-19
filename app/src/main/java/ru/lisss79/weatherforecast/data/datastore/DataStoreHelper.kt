package ru.lisss79.weatherforecast.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.huawei.hms.site.api.model.Site
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryFactory
import ru.lisss79.weatherforecast.data.repositories.location.LocationRepositoryVariant
import ru.lisss79.weatherforecast.entities.CurrentWeatherDetails
import ru.lisss79.weatherforecast.entities.DailyWeatherDetails
import ru.lisss79.weatherforecast.entities.ForecastMode
import ru.lisss79.weatherforecast.entities.HourlyWeatherDetails
import ru.lisss79.weatherforecast.entities.PlacesSortingMode
import ru.lisss79.weatherforecast.entities.SiteSerializer
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.WeatherQuery

class DataStoreHelper(private val context: Context, private val scope: CoroutineScope) {

    private val LOCATION_REPOSITORY_KEY = stringPreferencesKey("locationRepository")
    private val GEOCODER_REPOSITORY_KEY = stringPreferencesKey("geocoderRepository")
    private val FORECAST_MODE_KEY = stringPreferencesKey("forecastMode")
    private val PLACES_LIST_KEY = stringSetPreferencesKey("placesList")
    private val SELECTED_PLACE_KEY = intPreferencesKey("selectedPlace")
    private val CURRENT_DETAILS_KEY = stringSetPreferencesKey("currentDetails")
    private val HOURLY_DETAILS_KEY = stringSetPreferencesKey("hourlyDetails")
    private val DAILY_DETAILS_KEY = stringSetPreferencesKey("dailyDetails")
    private val PLACES_SORTING_MODE_KEY = stringPreferencesKey("placesSorting")

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    val locationRepositoryFlow: Flow<LocationRepositoryVariant> = context.dataStore.data
        .map { preferences ->
            LocationRepositoryVariant.valueOf(
                preferences[LOCATION_REPOSITORY_KEY]
                    ?: LocationRepositoryVariant.default.name
            )
        }
    val geocoderRepositoryFlow: Flow<GeocoderRepositoryVariant> = context.dataStore.data
        .map { preferences ->
            GeocoderRepositoryVariant.valueOf(
                preferences[GEOCODER_REPOSITORY_KEY]
                    ?: GeocoderRepositoryVariant.default.name
            )
        }
    val forecastModeFlow: Flow<ForecastMode> = context.dataStore.data
        .map { preferences ->
            ForecastMode.valueOf(
                preferences[FORECAST_MODE_KEY]
                    ?: ForecastMode.default.name
            )
        }
    val placesListFlow: Flow<Set<Site>> = context.dataStore.data
        .map { preferences ->
            preferences[PLACES_LIST_KEY]
                ?.map { Json.decodeFromString(SiteSerializer, it) }
                ?.toSet()
                ?: setOf()
        }
    val selectedPlaceFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[SELECTED_PLACE_KEY] ?: Values.selectedPlaceDefault
        }
    val currentDetailsFlow: Flow<Set<CurrentWeatherDetails>> = context.dataStore.data
        .map { preferences ->
            preferences[CURRENT_DETAILS_KEY]
                ?.map { CurrentWeatherDetails.valueOf(it) }
                ?.toSet()
                ?: CurrentWeatherDetails.default
        }
    val hourlyDetailsFlow: Flow<Set<HourlyWeatherDetails>> = context.dataStore.data
        .map { preferences ->
            preferences[HOURLY_DETAILS_KEY]
                ?.map { HourlyWeatherDetails.valueOf(it) }
                ?.toSet()
                ?: HourlyWeatherDetails.default
        }
    val dailyDetailsFlow: Flow<Set<DailyWeatherDetails>> = context.dataStore.data
        .map { preferences ->
            preferences[DAILY_DETAILS_KEY]
                ?.map { DailyWeatherDetails.valueOf(it) }
                ?.toSet()
                ?: DailyWeatherDetails.default
        }
    val placesSortingModeFlow: Flow<PlacesSortingMode> = context.dataStore.data
        .map { preferences ->
            PlacesSortingMode.valueOf(
                preferences[PLACES_SORTING_MODE_KEY]
                    ?: PlacesSortingMode.default.name
            )
        }
    suspend fun getWeatherQueries(): WeatherQuery {
        val q1 = currentDetailsFlow.first().map { it.queryName }
        val q2 = hourlyDetailsFlow.first().map { it.queryName }
        val q3 = dailyDetailsFlow.first().map { it.queryName }
        return WeatherQuery(
            currentQuery = q1,
            hourlyQuery = q2,
            dailyQuery = q3
        )
    }

    fun setLocationRepository(selectedLocation: LocationRepositoryVariant) {
        scope.launch {
            context.dataStore.edit { settings ->
                settings[LOCATION_REPOSITORY_KEY] = selectedLocation.name
            }
        }
    }
    fun setGeocoderRepository(selectedGeocoder: GeocoderRepositoryVariant) {
        scope.launch {
            context.dataStore.edit { settings ->
                settings[GEOCODER_REPOSITORY_KEY] = selectedGeocoder.name
            }
        }
    }
    fun setForecastMode(selectedForecastMode: ForecastMode) {
        scope.launch {
            context.dataStore.edit { settings ->
                settings[FORECAST_MODE_KEY] = selectedForecastMode.name
            }
        }
    }
    fun setPlacesList(newPlaces: Set<Site>) {
        scope.launch {
            val newPlacesInString = newPlaces
                .map { Json.encodeToString(SiteSerializer, it) }
                .toSet()
            context.dataStore.edit { settings ->
                settings[PLACES_LIST_KEY] = newPlacesInString
            }
        }
    }
    fun setSelectedPlace(selectedPlace: Int) {
        scope.launch {
            context.dataStore.edit { settings ->
                settings[SELECTED_PLACE_KEY] = selectedPlace
            }
        }
    }
    fun setCurrentDetails(newDetails: Set<CurrentWeatherDetails>) {
        scope.launch {
            val newDetailsInString = newDetails
                .map { it.name }
                .toSet()
            context.dataStore.edit { settings ->
                settings[CURRENT_DETAILS_KEY] = newDetailsInString
            }
        }
    }
    fun setHourlyDetails(newDetails: Set<HourlyWeatherDetails>) {
        scope.launch {
            val newDetailsInString = newDetails
                .map { it.name }
                .toSet()
            context.dataStore.edit { settings ->
                settings[HOURLY_DETAILS_KEY] = newDetailsInString
            }
        }
    }
    fun setDailyDetails(newDetails: Set<DailyWeatherDetails>) {
        scope.launch {
            val newDetailsInString = newDetails
                .map { it.name }
                .toSet()
            context.dataStore.edit { settings ->
                settings[DAILY_DETAILS_KEY] = newDetailsInString
            }
        }
    }
    fun setPlacesSortingMode(placesSortingMode: PlacesSortingMode) {
        scope.launch {
            context.dataStore.edit { settings ->
                settings[PLACES_SORTING_MODE_KEY] = placesSortingMode.name
            }
        }
    }

}
