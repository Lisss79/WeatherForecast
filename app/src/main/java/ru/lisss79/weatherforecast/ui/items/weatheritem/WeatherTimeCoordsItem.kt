package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.huawei.hms.site.api.model.Site
import ru.lisss79.weatherforecast.entities.Coords
import ru.lisss79.weatherforecast.entities.LoadingState
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.ui.helpers.FormatterForUi
import ru.lisss79.weatherforecast.ui.helpers.WeatherCode

@Composable
fun WeatherTimeCoordsItem(
    universalWeatherState: UniversalWeatherState,
    placeState: Site? = null,
    coordsState: Coords? = null,
    showPlace: Boolean,
    justPlace: Boolean,
    state: LoadingState = LoadingState.FINISHED
) {

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

    Column(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = FormatterForUi.universalTimeToText(
                universalWeatherState.universalTime, placeState
            ),
            style = MaterialTheme.typography.bodyMedium
        )
        Icon(
            painter = painterResource(
                id = WeatherCode.getWeatherCode(universalWeatherState).icon
            ),
            contentDescription = "weather_icon",
            modifier = Modifier.size(50.dp),
            tint = Color.Unspecified
        )
        if (showPlace) {
            Spacer(modifier = Modifier.height(8.dp))
            if (placeState != null) {
                val textModifier =
                    if (state == LoadingState.LOADING_PLACE) Modifier.alpha(animatedAlpha)
                    else Modifier
                Text(
                    text = FormatterForUi.placeToText(placeState, justPlace),
                    modifier = textModifier,
                    style = MaterialTheme.typography.labelMedium
                )
            } else {
                val textModifier =
                    if (state == LoadingState.LOADING_PLACE) Modifier.alpha(animatedAlpha)
                    else Modifier
                Text(
                    text = FormatterForUi.coordsToText(coordsState),
                    modifier = textModifier,
                    style = MaterialTheme.typography.labelMedium
                )
            }
        }
    }
}