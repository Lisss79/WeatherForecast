package ru.lisss79.weatherforecast.data.repositories.location

import android.annotation.SuppressLint
import android.location.Location
import com.huawei.hms.location.LocationServices
import ru.lisss79.weatherforecast.app.ForecastApp
import ru.lisss79.weatherforecast.entities.weather.WeatherException
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationRepositoryHuawei : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCoords(): Result<Location> {
        val fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(ForecastApp.instance)
        val locationTask = fusedLocationProviderClient.lastLocation
        val executor = Executors.newSingleThreadExecutor()

        val result = suspendCoroutine<Result<Location>> { continuation ->
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

        return result
    }

}