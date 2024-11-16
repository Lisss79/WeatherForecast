package ru.lisss79.weatherforecast.ui.items

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ru.lisss79.weatherforecast.ui.mainelements.BackPressHandler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopSettingsBar(
    modifier: Modifier = Modifier,
    onBackPressed: () -> Unit
) {
    BackPressHandler {
        onBackPressed()
    }
    CenterAlignedTopAppBar(
        title = {
            Text(text = "Weather Settings")
        },
        modifier = modifier,
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                )
            }
        }
    )
}