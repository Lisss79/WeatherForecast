package ru.lisss79.weatherforecast.ui.items.settings

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alorma.compose.settings.ui.SettingsCheckbox
import com.alorma.compose.settings.ui.SettingsGroup
import ru.lisss79.weatherforecast.data.datastore.DataStoreHelper
import ru.lisss79.weatherforecast.entities.CurrentWeatherDetails
import ru.lisss79.weatherforecast.entities.DailyWeatherDetails
import ru.lisss79.weatherforecast.entities.HourlyWeatherDetails

@Composable
fun WeatherDetailsSettingsScreen(
    modifier: Modifier = Modifier,
    dataStoreHelper: DataStoreHelper,
    onDetailsSettingsChanged: () -> Unit,
) {
    val scope = rememberCoroutineScope()
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val selectedCurrentDetails = dataStoreHelper
            .currentDetailsFlow.collectAsStateWithLifecycle(CurrentWeatherDetails.default)
        var currentIsOpen by rememberSaveable { mutableStateOf(false) }
        val selectedHourlyDetails = dataStoreHelper
            .hourlyDetailsFlow.collectAsStateWithLifecycle(HourlyWeatherDetails.default)
        var hourlyIsOpen by rememberSaveable { mutableStateOf(false) }
        val selectedDailyDetails = dataStoreHelper
            .dailyDetailsFlow.collectAsStateWithLifecycle(DailyWeatherDetails.default)
        var dailyIsOpen by rememberSaveable { mutableStateOf(false) }
        val iconOpen = rememberUpdatedState(newValue = Icons.Default.KeyboardArrowUp)
        val iconClosed = rememberUpdatedState(newValue = Icons.Default.KeyboardArrowDown)

        SettingsGroup(
            modifier = Modifier.animateContentSize(),
            enabled = true,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        currentIsOpen = !currentIsOpen
                    }
                ) {
                    Text(
                        text = "Current weather details",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Icon(
                        imageVector = if (currentIsOpen) iconOpen.value else iconClosed.value,
                        contentDescription = null
                    )
                }
            }
        ) {
            if (currentIsOpen) {
                for (currentWeatherDetail in CurrentWeatherDetails.entries) {
                    val selected = selectedCurrentDetails.value.contains(currentWeatherDetail)
                    SettingsCheckbox(
                        state = selected,
                        title = { Text(currentWeatherDetail.menuName) },
                        onCheckedChange = { value ->
                            val newSet = LinkedHashSet(selectedCurrentDetails.value).apply {
                                if (value) add(currentWeatherDetail)
                                else remove(currentWeatherDetail)
                            }
                            dataStoreHelper.setCurrentDetails(newSet)
                            onDetailsSettingsChanged()
                        },
                    )
                }
            }
        }
        SettingsGroup(
            modifier = Modifier.animateContentSize(),
            enabled = true,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        hourlyIsOpen = !hourlyIsOpen
                    }
                ) {
                    Text(
                        text = "Hourly weather details",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Icon(
                        imageVector = if (hourlyIsOpen) iconOpen.value else iconClosed.value,
                        contentDescription = null
                    )
                }
            }
        ) {
            if (hourlyIsOpen) {
                for (hourlyWeatherDetail in HourlyWeatherDetails.entries) {
                    val selected = selectedHourlyDetails.value.contains(hourlyWeatherDetail)
                    SettingsCheckbox(
                        state = selected,
                        title = { Text(hourlyWeatherDetail.menuName) },
                        onCheckedChange = { value ->
                            val newSet = LinkedHashSet(selectedHourlyDetails.value).apply {
                                if (value) add(hourlyWeatherDetail)
                                else remove(hourlyWeatherDetail)
                            }
                            dataStoreHelper.setHourlyDetails(newSet)
                            onDetailsSettingsChanged()
                        },
                    )
                }
            }
        }
        SettingsGroup(
            modifier = Modifier.animateContentSize(),
            enabled = true,
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable {
                        dailyIsOpen = !dailyIsOpen
                    }
                ) {
                    Text(
                        text = "Daily weather details",
                        modifier = Modifier.weight(1f),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Icon(
                        imageVector = if (dailyIsOpen) iconOpen.value else iconClosed.value,
                        contentDescription = null
                    )
                }
            }
        ) {
            if (dailyIsOpen) {
                for (dailyWeatherDetail in DailyWeatherDetails.entries) {
                    val selected = selectedDailyDetails.value.contains(dailyWeatherDetail)
                    SettingsCheckbox(
                        state = selected,
                        title = { Text(dailyWeatherDetail.menuName) },
                        onCheckedChange = { value ->
                            val newSet = LinkedHashSet(selectedDailyDetails.value).apply {
                                if (value) add(dailyWeatherDetail)
                                else remove(dailyWeatherDetail)
                            }
                            dataStoreHelper.setDailyDetails(newSet)
                            onDetailsSettingsChanged()
                        },
                    )
                }
            }
        }
    }
}