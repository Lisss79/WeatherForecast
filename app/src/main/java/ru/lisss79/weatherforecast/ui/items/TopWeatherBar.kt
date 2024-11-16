package ru.lisss79.weatherforecast.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopWeatherBar(
    modifier: Modifier = Modifier,
    onSettingsSelection: () -> Unit,
    onPlacesSelection: () -> Unit
) {
    var showMenu by rememberSaveable { mutableStateOf(false) }

    CenterAlignedTopAppBar(
        title = {
            Text(text = "Weather Forecast")
        },
        modifier = modifier,
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        actions = {
            IconButton(onClick = { showMenu = true }) {
                Icon(
                    imageVector = Icons.Filled.MoreVert,
                    contentDescription = "ShowMenu",
                )
            }
            WeatherMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                onSettingsSelection = {
                    showMenu = false
                    onSettingsSelection()
                },
                onPlacesSelection = {
                    showMenu = false
                    onPlacesSelection()
                }
            )
        }
    )
}

@Composable
fun WeatherMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onSettingsSelection: () -> Unit,
    onPlacesSelection: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest
    ) {
        DropdownMenuItem(
            text = { Text("Settings") },
            onClick = onSettingsSelection,
            leadingIcon = {
                Icon(Icons.Outlined.Settings, contentDescription = null)
            }
        )
        DropdownMenuItem(
            text = { Text("Places") },
            onClick = onPlacesSelection,
            leadingIcon = {
                Icon(Icons.Outlined.LocationOn, contentDescription = null)
            }
        )
    }
}

