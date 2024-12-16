package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import ru.lisss79.weatherforecast.R
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import ru.lisss79.weatherforecast.ui.helpers.WeatherCode
import kotlin.math.sqrt

@Composable
fun WeatherIcon(
    modifier: Modifier = Modifier,
    universalWeatherState: UniversalWeatherState?
) {
    return when (universalWeatherState?.weatherCode) {
        0, 1, 2, 3 -> Canvas(modifier = modifier.weatherIconModifier(universalWeatherState)) { }
        else -> Icon(
            painter = painterResource(id = WeatherCode.getWeatherCode(universalWeatherState).icon),
            contentDescription = "weather_icon",
            modifier = Modifier.size(50.dp),
            tint = Color.Unspecified
        )
    }
}

@Composable
fun Modifier.weatherIconModifier(universalWeatherState: UniversalWeatherState?): Modifier {
    val code = universalWeatherState?.weatherCode
    return when (code) {
        0, 1, 2, 3 -> cloudAndSun(universalWeatherState)
        else -> TODO()
    }
}


@Composable
fun Modifier.cloudAndSun(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud_)
    val sun = ImageBitmap.imageResource(R.drawable.sun_)
    val moon = ImageBitmap.imageResource(R.drawable.moon_)
    return this then Modifier.drawWithContent {
        val isDay = state.getDay() ?: true
        val star = if (isDay) sun else moon
        val cloudiness = state.getCloudCover()?.toInt() ?: 0
        val factorCloud = size.width / cloud.width * 1.0
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()

        val newStarWidth = (newCloudHeight * 0.87).toInt()
        val newStarHeight = (newCloudHeight * 0.87).toInt()

        val newCloudWidth1 = (newCloudWidth * sqrt(cloudiness / 100.0)).toInt()
        val newCloudHeight1 = (newCloudHeight * sqrt(cloudiness / 100.0)).toInt()

        drawImage(
            image = star,
            dstSize = IntSize(newStarWidth, newStarHeight),
            dstOffset = IntOffset(
                (size.width - newStarWidth * 1.3).toInt(),
                center.y.toInt() - newStarHeight / 2
            )
        )
        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth1, newCloudHeight1),
            dstOffset = IntOffset(0, center.y.toInt() + newCloudHeight / 2 - newCloudHeight1)
        )
    }
}

@Preview(
    widthDp = 50,
    heightDp = 50
)
@Composable
fun ShowWeatherIcon() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(100)
        .weatherCode(0)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}