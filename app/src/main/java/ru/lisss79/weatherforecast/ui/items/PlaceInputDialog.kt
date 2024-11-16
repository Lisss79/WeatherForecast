package ru.lisss79.weatherforecast.ui.items

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.huawei.hms.site.api.model.Site
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.compose.localDI
import org.kodein.di.instance
import ru.lisss79.weatherforecast.data.repositories.geocoders.GeocoderRepositoryVariant
import ru.lisss79.weatherforecast.domain.places.GetPlacesByNameUseCase
import ru.lisss79.weatherforecast.ui.helpers.FormatterForUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PlaceInputDialog(
    geocoderRepository: GeocoderRepositoryVariant,
    onConfirm: (site: Site) -> Unit,
    onDismiss: () -> Unit,
    scope: CoroutineScope
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        var text by remember { mutableStateOf("") }
        var places by remember { mutableStateOf(listOf<Site>()) }
        var noSearchResults by remember { mutableStateOf(false) }
        var searchInProgress by remember { mutableStateOf(false) }

        val infiniteTransition = rememberInfiniteTransition(label = "InfiniteTransition")
        val animatedAlpha by infiniteTransition.animateFloat(
            initialValue = 1f,
            targetValue = 0.2f,
            animationSpec = infiniteRepeatable(
                animation = tween(800, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "alphaAnimation"
        )

        val di = localDI()

        Card(
            modifier = Modifier
                .wrapContentSize()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(text = "Input Name of Place")
                OutlinedTextField(
                    value = text,
                    modifier = Modifier.padding(vertical = 4.dp),
                    onValueChange = {
                        noSearchResults = false
                        places = listOf()
                        text = it
                    }
                )

                if (searchInProgress) {
                    Text(
                        text = "Searching...",
                        modifier = Modifier
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                            .alpha(animatedAlpha)
                    )
                }
                if (noSearchResults) {
                    Text(
                        text = "No results found...",
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                if (places.isNotEmpty()) {
                    for (place in places) {
                        val name = FormatterForUi.placeToText(
                            place = place,
                            justPlace = true,
                            countryAfterLocality = true
                        )
                        Text(
                            text = name,
                            modifier = Modifier
                                .padding(horizontal = 16.dp)
                                .padding(bottom = 2.dp, top = 4.dp)
                                .combinedClickable(
                                    onClick = {
                                        onConfirm(place)
                                    },
                                    onLongClick = { }
                                )
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text("Cancel")
                    }
                    TextButton(
                        onClick = {
                            scope.launch {
                                searchInProgress = true
                                val result = getPlaces(text, geocoderRepository, di)
                                if (result.isNullOrEmpty()) {
                                    noSearchResults = true
                                    searchInProgress = false
                                    places = listOf()
                                } else {
                                    noSearchResults = false
                                    searchInProgress = false
                                    places = result
                                }
                            }
                        },
                        modifier = Modifier.padding(4.dp),
                    ) {
                        Text("Search")
                    }
                }
            }
        }
    }
}

private suspend fun getPlaces(
    text: String,
    geocoderRepository: GeocoderRepositoryVariant,
    di: DI
): List<Site>? {
    val getPlacesByNameUseCase by di.instance<GetPlacesByNameUseCase>()
    val places = getPlacesByNameUseCase(text, geocoderRepository)
    return places.getOrNull()
}
