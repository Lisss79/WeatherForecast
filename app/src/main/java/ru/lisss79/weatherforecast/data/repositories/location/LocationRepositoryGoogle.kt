package ru.lisss79.weatherforecast.data.repositories.location

import android.annotation.SuppressLint
import android.location.Location
import com.google.android.gms.location.Priority
import com.google.android.gms.location.LocationServices as GoogleLocationServices
import ru.lisss79.weatherforecast.app.ForecastApp
import ru.lisss79.weatherforecast.entities.weather.WeatherException
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationRepositoryGoogle : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCoords(): Result<Location> {
        val fusedLocationProviderClient = ForecastApp.instance?.let {
            GoogleLocationServices.getFusedLocationProviderClient(it)
        }
        val locationTask = fusedLocationProviderClient
            ?.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
        val executor = Executors.newSingleThreadExecutor()

        val result = if (locationTask != null) {
            suspendCoroutine<Result<Location>> { continuation ->
                locationTask.addOnSuccessListener(executor) { location ->
                    if (location != null) continuation.resume(Result.success(location))
                    else continuation.resume(
                        Result.failure(WeatherException("Location is available but no coordinates got"))
                    )
                }
                locationTask.addOnFailureListener(executor) { exception ->
                    continuation.resume(Result.failure(exception))
                }
            }
        }
        else Result.failure(WeatherException("Application context is not available"))
        return result
    }

}