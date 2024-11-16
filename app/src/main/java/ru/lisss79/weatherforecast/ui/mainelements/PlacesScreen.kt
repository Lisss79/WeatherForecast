package ru.lisss79.weatherforecast.ui.mainelements

import android.location.Location
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.R
import ru.lisss79.weatherforecast.data.datastore.DataStoreHelper
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.entities.PlacesSortingMode
import ru.lisss79.weatherforecast.entities.Values
import ru.lisss79.weatherforecast.ui.items.PlaceInputDialog
import ru.lisss79.weatherforecast.ui.items.placvesitems.CurrentPlaceItem
import ru.lisss79.weatherforecast.ui.items.placvesitems.PlacesList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesScreen(
    modifier: Modifier = Modifier,
    viewModel: WeatherViewModel,
    dataStoreHelper: DataStoreHelper,
    onPlacesChanged: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var showPlaceInputDialog by rememberSaveable { mutableStateOf(false) }
    val placesList = dataStoreHelper
        .placesListFlow.collectAsStateWithLifecycle(setOf())
    val selectedPlace = dataStoreHelper
        .selectedPlaceFlow.collectAsStateWithLifecycle(Values.selectedPlaceDefault)
    val geocoderRepository = dataStoreHelper
        .geocoderRepositoryFlow.collectAsStateWithLifecycle(GeocoderRepositoryVariant.default)
    val sortingMode = dataStoreHelper
        .placesSortingModeFlow.collectAsStateWithLifecycle(PlacesSortingMode.default)

    val sortedPlacesSet = remember {
        derivedStateOf {
            getSortedList(placesList.value, sortingMode.value, viewModel)
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select default place",
                    modifier = Modifier
                        .padding(all = 4.dp)
                        .weight(1f),
                    style = MaterialTheme.typography.titleMedium
                )
                val icon = when (sortingMode.value) {
                    PlacesSortingMode.DISTANCE -> R.drawable.ic_sort_distance
                    PlacesSortingMode.UNSORTED -> R.drawable.ic_sort_unsorted
                    PlacesSortingMode.NAME -> R.drawable.ic_sort_name
                }
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(
                        spacingBetweenTooltipAndAnchor = 2.dp
                    ),
                    tooltip = {
                        PlainTooltip {
                            Text(sortingMode.value.subscription)
                        }
                    },
                    state = rememberTooltipState(),
                    modifier = Modifier
                ) {
                    Icon(
                        modifier = Modifier.clickable {
                            val newSortingMode = when (sortingMode.value) {
                                PlacesSortingMode.UNSORTED -> PlacesSortingMode.DISTANCE
                                PlacesSortingMode.DISTANCE -> PlacesSortingMode.NAME
                                PlacesSortingMode.NAME -> PlacesSortingMode.UNSORTED
                            }
                            dataStoreHelper.setPlacesSortingMode(newSortingMode)
                        },
                        painter = painterResource(icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }
            CurrentPlaceItem(
                onClick = {
                    dataStoreHelper.setSelectedPlace(Values.selectedPlaceDefault)
                    onPlacesChanged()
                },
                selected = selectedPlace.value == Values.selectedPlaceDefault
            )

            PlacesList(
                placesSet = sortedPlacesSet,
                selectedPlace = selectedPlace,
                onClick = { selectedPlace ->
                    dataStoreHelper.setSelectedPlace(selectedPlace)
                    onPlacesChanged()
                },
                onRemove = { selected ->
                    val newPlaces = LinkedHashSet(placesList.value)
                    newPlaces.remove(placesList.value.elementAt(selected))
                    dataStoreHelper.setPlacesList(newPlaces)
                    if (selectedPlace.value == selected)
                        dataStoreHelper.setSelectedPlace(Values.selectedPlaceDefault)
                    onPlacesChanged()
                }
            )
        }
        FloatingActionButton(
            onClick = {
                showPlaceInputDialog = true
            },
            modifier = Modifier
                .padding(8.dp)
                .align(Alignment.BottomEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null
            )
        }
        if (showPlaceInputDialog) {
            PlaceInputDialog(
                geocoderRepository = geocoderRepository.value,
                onConfirm = { site ->
                    val newPlaces = LinkedHashSet(placesList.value)
                    newPlaces.add(site)
                    dataStoreHelper.setPlacesList(newPlaces)
                    showPlaceInputDialog = false
                    onPlacesChanged()
                },
                onDismiss = {
                    showPlaceInputDialog = false
                },
                scope = scope
            )
        }
    }
}

fun getSortedList(
    placesSet: Set<Site>,
    mode: PlacesSortingMode,
    viewModel: WeatherViewModel
): Set<IndexedValue<Pair<Site, Float>>> {
    val currentCoords = viewModel.coords.value
    val distance = floatArrayOf(0f)
    val indexedSetWithDistance = placesSet.map { place ->
        if (currentCoords != null) {
            Location.distanceBetween(
                currentCoords.latitude, currentCoords.longitude,
                place.location.lat, place.location.lng, distance
            )
        }
        Pair(place, distance[0])
    }.withIndex()
    val newList = when (mode) {
        PlacesSortingMode.UNSORTED -> indexedSetWithDistance
        PlacesSortingMode.DISTANCE -> {
            indexedSetWithDistance.sortedBy { it.value.second }
        }
        PlacesSortingMode.NAME -> {
            indexedSetWithDistance.sortedBy { it.value.first.formatAddress }
        }
    }
    return newList.toSet()
}

@Preview
@Composable
fun PlacesScreenPreview() {
    //PlacesScreen()
}
