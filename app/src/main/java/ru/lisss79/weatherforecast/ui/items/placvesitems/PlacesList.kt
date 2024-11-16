package ru.lisss79.weatherforecast.ui.items.placvesitems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PlainTooltip
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.ui.helpers.FormatterForUi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlacesList(
    placesSet: State<Set<IndexedValue<Pair<Site, Float>>>>,
    selectedPlace: State<Int>,
    onClick: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    if (placesSet.value.isNotEmpty()) {
        for (place in placesSet.value) {
            val placeName = FormatterForUi.placeToText(
                place = place.value.first,
                justPlace = true,
                countryAfterLocality = true
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedPlace.value == place.index,
                        onClick = {
                            onClick(place.index)
                        },
                        modifier = Modifier.wrapContentSize()
                    )
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(
                            spacingBetweenTooltipAndAnchor = 2.dp
                        ),
                        tooltip = {
                            PlainTooltip {
                                Text("${place.value.first.formatAddress}," +
                                        " ${(place.value.second / 1000).toInt()}km")
                            }
                        },
                        state = rememberTooltipState(),
                        modifier = Modifier
                    ) {
                        Text(
                            text = placeName,
                            modifier = Modifier
                                .clickable {
                                    onClick(place.index)
                                }
                                .padding(vertical = 4.dp),
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        onRemove(place.index)
                    }
                )
            }

        }
    }

}