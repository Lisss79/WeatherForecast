package ru.lisss79.weatherforecast.ui.mainelements

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.huawei.hms.site.api.model.Site
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.instance
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.domain.coords.GetCoordsUseCase
import ru.lisss79.weatherforecast.domain.places.GetPlaceByCoordsUseCase
import ru.lisss79.weatherforecast.domain.timezone.GetTimeOffsetUseCase
import ru.lisss79.weatherforecast.domain.weather.GetCurrentAndDailyWeatherUseCase
import ru.lisss79.weatherforecast.domain.weather.GetCurrentAndHourlyWeatherUseCase
import ru.lisss79.weatherforecast.domain.weather.GetCurrentWeatherUseCase
import ru.lisss79.weatherforecast.domain.weather.GetWeatherFromDifferentPlacesUseCase
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.Errors
import ru.lisss79.weatherforecast.entities.ForecastMode
import ru.lisss79.weatherforecast.entities.LoadingState
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.WeatherQuery
import ru.lisss79.weatherforecast.entities.toCoords
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.entities.weather.WeatherException

class WeatherViewModel(override val di: DI) : ViewModel(), DIAware {

    private val getCoordsUseCase by instance<GetCoordsUseCase>()
    private val getPlaceByCoordsUseCase by instance<GetPlaceByCoordsUseCase>()
    private val getCurrentWeatherUseCase by instance<GetCurrentWeatherUseCase>()
    private val getCurrentAndHourlyWeatherUseCase by instance<GetCurrentAndHourlyWeatherUseCase>()
    private val getCurrentAndDailyWeatherUseCase by instance<GetCurrentAndDailyWeatherUseCase>()
    private val getWeatherFromDifferentPlacesUseCase by instance<GetWeatherFromDifferentPlacesUseCase>()
    private val getTimeOffsetUseCase by instance<GetTimeOffsetUseCase>()

    private var _currentWeather = MutableStateFlow<UniversalWeatherState?>(null)
    val currentWeather: StateFlow<UniversalWeatherState?> = _currentWeather.asStateFlow()

    private var _forecastWeather = MutableStateFlow<List<UniversalWeatherState?>?>(null)
    val forecastWeather: StateFlow<List<UniversalWeatherState?>?> = _forecastWeather.asStateFlow()

    private var lastCoords: Coords? = null

    private var _coords = MutableStateFlow<Coords?>(null)
    val coords: StateFlow<Coords?> = _coords.asStateFlow()

    private var _place = MutableStateFlow<Site?>(null)
    val place: StateFlow<Site?> = _place.asStateFlow()

    private var _currentGpsCoords = MutableStateFlow<Coords?>(null)
    val currentGpsCoords: StateFlow<Coords?> = _currentGpsCoords.asStateFlow()

    private var _currentGpsPlace = MutableStateFlow<Site?>(null)
    val currentGpsPlace: StateFlow<Site?> = _currentGpsPlace.asStateFlow()

    private var _errors = MutableStateFlow(Errors.UNSPECIFIED)
    val errors: StateFlow<Errors> = _errors.asStateFlow()

    private var _loading = MutableStateFlow(LoadingState.FINISHED)
    val loading: StateFlow<LoadingState> = _loading.asStateFlow()


    private suspend fun getCurrentWeather(queries: WeatherQuery) {
        _loading.value = LoadingState.LOADING_WEATHER
        val currentRequest = queries.currentQuery

        val lastCoords = coords.value
        val result = if (lastCoords != null) getCurrentWeatherUseCase(
            lastCoords,
            currentRequest
        )
        else Result.failure(
            WeatherException("I don't have coords to get weather")
        )

        if (result.isSuccess) {
            val lastResult = result.getOrNull()
            if (lastResult != null) _errors.value = Errors.NO_ERRORS
            else _errors.value = Errors.NO_WEATHER
            _currentWeather.value = lastResult
        } else {
            _errors.value = Errors.NO_WEATHER
        }
        _loading.value = LoadingState.FINISHED
    }

    suspend fun getCurrentWeatherAndForecast(
        forecastMode: ForecastMode,
        placesList: Set<Site>?,
        selectedPlace: Int = Values.selectedPlaceDefault,
        queries: WeatherQuery = WeatherQuery()
    ) {
        when (forecastMode) {
            ForecastMode.HOURLY -> getCurrentAndHourlyWeather(placesList, selectedPlace, queries)
            ForecastMode.DAILY -> getCurrentAndDailyWeather(placesList, selectedPlace, queries)
            ForecastMode.DIFFERENT_PLACES -> {
                getCurrentWeather(queries)
                if (!placesList.isNullOrEmpty())
                    getWeatherFromDifferentPlaces(placesList, selectedPlace, queries)
            }
        }
    }

    private suspend fun getWeatherFromDifferentPlaces(
        placesList: Set<Site>,
        selectedPlace: Int,
        queries: WeatherQuery
    ) {
        _loading.value = LoadingState.LOADING_WEATHER
        val currentRequest = queries.currentQuery

        val lastCoords = coords.value
        val result = if (lastCoords != null)
            getWeatherFromDifferentPlacesUseCase(placesList, currentRequest, selectedPlace)
        else Result.failure(
            WeatherException("Can't get weather")
        )

        if (result.isSuccess) {
            val lastResult = result.getOrNull()
            if (lastResult != null) _errors.value = Errors.NO_ERRORS
            else _errors.value = Errors.NO_WEATHER
            _forecastWeather.value = lastResult
        } else {
            _errors.value = Errors.NO_WEATHER
        }
        _loading.value = LoadingState.FINISHED
    }

    private suspend fun getCurrentAndHourlyWeather(
        placesList: Set<Site>?,
        selectedPlace: Int,
        queries: WeatherQuery
    ) {
        _loading.value = LoadingState.LOADING_WEATHER
        val currentRequest = queries.currentQuery
        val hourlyRequest = queries.hourlyQuery

        val lastCoords = if (selectedPlace == Values.selectedPlaceDefault ||
            placesList == null
        ) coords.value
        else placesList.elementAt(selectedPlace).toCoords()
        val result = if (lastCoords != null) getCurrentAndHourlyWeatherUseCase(
            lastCoords,
            currentRequest,
            hourlyRequest
        )
        else Result.failure(
            WeatherException("I don't have coords to get weather")
        )

        if (result.isSuccess) {
            val lastResult = result.getOrNull()
            if (lastResult != null) _errors.value = Errors.NO_ERRORS
            else _errors.value = Errors.NO_WEATHER
            _currentWeather.value = lastResult?.get(0)
            _forecastWeather.value = lastResult?.subList(1, lastResult.size - 1)
        } else {
            _errors.value = Errors.NO_WEATHER
        }
        _loading.value = LoadingState.FINISHED
    }

    private suspend fun getCurrentAndDailyWeather(
        placesList: Set<Site>?,
        selectedPlace: Int,
        queries: WeatherQuery
    ) {
        _loading.value = LoadingState.LOADING_WEATHER
        val currentRequest = queries.currentQuery
        val dailyRequest = queries.dailyQuery

        val lastCoords = if (selectedPlace == Values.selectedPlaceDefault ||
            placesList == null
        ) coords.value
        else placesList.elementAt(selectedPlace).toCoords()
        val result = if (lastCoords != null) getCurrentAndDailyWeatherUseCase(
            lastCoords,
            currentRequest,
            dailyRequest
        )
        else Result.failure(
            WeatherException("I don't have coords to get weather")
        )

        if (result.isSuccess) {
            val lastResult = result.getOrNull()
            if (lastResult != null) _errors.value = Errors.NO_ERRORS
            else _errors.value = Errors.NO_WEATHER
            _currentWeather.value = lastResult?.get(0)
            _forecastWeather.value = lastResult?.subList(1, lastResult.size - 1)
        } else {
            _errors.value = Errors.NO_WEATHER
        }
        _loading.value = LoadingState.FINISHED
    }

    suspend fun getCoords(
        selectedPlace: Int = Values.selectedPlaceDefault,
        placesList: Set<Site>? = null
    ) {
        fun setCoords(location: Location?) {
            if (location != null) {
                _coords.value = Coords(location.latitude, location.longitude)
            }
        }

        fun setCoords(coords: Coords?) {
            if (coords != null) {
                _coords.value = coords
            }
        }

        _loading.value = LoadingState.LOADING_COORDS
        if (selectedPlace == Values.selectedPlaceDefault) {
            val coordsResult = getCoordsUseCase()
            if (coordsResult.isSuccess) {
                _errors.value = Errors.NO_ERRORS
                setCoords(coordsResult.getOrNull())
            } else _errors.value = Errors.NO_COORDS
        } else {
            val site = placesList?.elementAt(selectedPlace)
            setCoords(site?.toCoords())
        }

        _loading.value = LoadingState.FINISHED
    }

    suspend fun getPlace(
        geocoderRepository: GeocoderRepositoryVariant,
        forceUpdatePlace: Boolean = false
    ) {
        fun setPlace(site: Site?) {
            if (site != null) {
                lastCoords = coords.value
            }
            _place.value = site
        }

        _loading.value = LoadingState.LOADING_PLACE
        val myCoords = coords.value
        if (myCoords != null) {
            val place = getPlaceByCoordsUseCase(
                geocoderRepository,
                myCoords,
                lastCoords,
                place.value,
                forceUpdatePlace
            )
            if (place.isSuccess) {
                _errors.value = Errors.NO_ERRORS
                setPlace(place.getOrNull())
            } else {
                _errors.value = Errors.NO_PLACE
                setPlace(null)
            }
        }
        _loading.value = LoadingState.FINISHED
    }

    suspend fun updateTimeOffset(
        placesList: Set<Site>
    ): Set<Site> {
        _loading.value = LoadingState.LOADING_TIMEZONES
        val newSet = placesList.map { site -> getTimeOffsetUseCase(site) }.toSet()
        _loading.value = LoadingState.FINISHED
        return newSet
    }

    fun removeForecastWeather() {
        _forecastWeather.value = null
    }

    fun resetError() {
        _errors.value = Errors.NO_ERRORS
    }

    class WeatherViewModelFactory(
        private val di: DI,
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return modelClass
                .getConstructor(DI::class.java)
                .newInstance(di)
        }
    }

}