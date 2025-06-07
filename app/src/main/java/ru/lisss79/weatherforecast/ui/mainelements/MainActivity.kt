package ru.lisss79.weatherforecast.ui.mainelements

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.huawei.hms.api.HuaweiApiAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.kodein.di.DIAware
import org.kodein.di.android.closestDI
import ru.lisss79.weatherforecast.R
import ru.lisss79.weatherforecast.data.datastore.DataStoreHelper
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.entities.WeatherDetail
import ru.lisss79.weatherforecast.ui.navigation.BaseNavigationElement
import ru.lisss79.weatherforecast.ui.theme.WeatherForecastTheme
import java.net.URLEncoder

class MainActivity : ComponentActivity(), DIAware {
    override val di by closestDI()
    private val viewModel: WeatherViewModel by viewModels() {
        WeatherViewModel.WeatherViewModelFactory(di)
    }
    private val scope = lifecycleScope
    private lateinit var dataStoreHelper: DataStoreHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataStoreHelper = DataStoreHelper(this, scope)
        enableEdgeToEdge(
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        setContent {
            WeatherForecastTheme {
                CheckPermission(
                    viewModel = viewModel,
                    dataStoreHelper = dataStoreHelper,
                    scope = scope,
                    currentActivity = this
                )
            }
        }
    }
}

@Composable
fun CheckPermission(
    viewModel: WeatherViewModel,
    dataStoreHelper: DataStoreHelper,
    scope: CoroutineScope,
    currentActivity: MainActivity
) {
    val context = LocalContext.current
    var weatherScreen by rememberSaveable {
        mutableStateOf(false)
    }
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) weatherScreen = true
        else Toast.makeText(
            currentActivity,
            "I have no location permissions", Toast.LENGTH_LONG
        ).show()
    }

    if (weatherScreen) {
        BaseNavigationElement(
            viewModel = viewModel,
            dataStoreHelper = dataStoreHelper,
            scope = scope
        )
        LaunchedEffect(Unit) {
            initValues(context)
            refreshData(
                viewModel = viewModel,
                dataStoreHelper = dataStoreHelper,
                forceUpdate = true,
                updateTimeOffset = true,
                scope = scope
            )
        }
    } else {
        val permissionCheckResult = ContextCompat
            .checkSelfPermission(currentActivity, android.Manifest.permission.ACCESS_FINE_LOCATION)
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            SideEffect {
                weatherScreen = true
            }
        } else {
            SideEffect {
                permissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
            }
        }
    }
}

fun refreshData(
    viewModel: WeatherViewModel,
    dataStoreHelper: DataStoreHelper,
    forceUpdate: Boolean,
    updateTimeOffset: Boolean = false,
    scope: CoroutineScope
) {
    viewModel.resetError()
    scope.launch(SupervisorJob()) {
        val geocoderRepository = dataStoreHelper.geocoderRepositoryFlow.first()
        val locationRepository = dataStoreHelper.locationRepositoryFlow.first()
        val selectedPlace = dataStoreHelper.selectedPlaceFlow.first()
        val placesList = dataStoreHelper.placesListFlow.first()
        val forecastMode = dataStoreHelper.forecastModeFlow.first()
        val queries = WeatherDetail.getAllWeatherQueries()
        if (updateTimeOffset) {
            dataStoreHelper.setPlacesList(placesList)
        }
        viewModel.getCoords(
            locationRepository = locationRepository,
            selectedPlace = selectedPlace,
            placesList = placesList
        )
        viewModel.getCurrentWeatherAndForecast(
            forecastMode = forecastMode,
            placesList = placesList,
            selectedPlace = selectedPlace,
            queries = queries
        )
        viewModel.getPlace(geocoderRepository = geocoderRepository, forceUpdatePlace = forceUpdate)
    }
}

private fun initValues(context: Context) {
    val huaweiApiAvailability = HuaweiApiAvailability.getInstance()
    val status = huaweiApiAvailability.isHuaweiMobileNoticeAvailable(context)
    Values.hmsIsAvailable = (status == 0)
    Values.HUAWEI_API_KEY = URLEncoder
        .encode(context.resources.getString(R.string.HUAWEI_API_KEY), "UTF-8")
    Values.YANDEX_API_KEY = URLEncoder
        .encode(context.resources.getString(R.string.YANDEX_API_KEY), "UTF-8")
    Values.MAPSCO_API_KEY = URLEncoder
        .encode(context.resources.getString(R.string.MAPSCO_API_KEY), "UTF-8")
}
