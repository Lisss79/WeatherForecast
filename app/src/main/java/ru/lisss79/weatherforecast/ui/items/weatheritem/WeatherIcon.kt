package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
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
    return Canvas(modifier = modifier.weatherIconModifier(universalWeatherState)) { }
}

@Composable
fun Modifier.weatherIconModifier(universalWeatherState: UniversalWeatherState?): Modifier {
    val code = universalWeatherState?.weatherCode
    return when (code) {
        0, 1, 2, 3 -> cloudAndSun(state = universalWeatherState)
        45, 48 -> fog(state = universalWeatherState)
        51, 53, 55, 61, 63, 65, 80, 81, 82 -> rain(state = universalWeatherState)
        56, 57, 66, 67 -> freezingRain(state = universalWeatherState)
        71, 73, 75 -> snowFall(state = universalWeatherState)
        77 -> snowGrains(state = universalWeatherState)
        85, 86 -> snowShower(state = universalWeatherState)
        95, 96, 99 -> thunder(state = universalWeatherState)
        else -> unknown()
    }
}


// Коды 0, 1, 2, 3
@Composable
fun Modifier.cloudAndSun(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val sun = ImageBitmap.imageResource(R.drawable.sun)
    val moon = ImageBitmap.imageResource(R.drawable.moon)
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

// Коды 71, 73, 75
@Composable
fun Modifier.snowFall(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val snowFlake = ImageBitmap.imageResource(R.drawable.snowflake)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val snowFlakeSize = (size.width * 0.12).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = center.y.toInt() + newCloudHeight / 2 - snowFlakeSize

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, center.y.toInt() - newCloudHeight / 2 - snowFlakeSize)
        )
        drawImage(
            image = snowFlake,
            dstSize = IntSize(snowFlakeSize, snowFlakeSize),
            dstOffset = IntOffset((center.x - snowFlakeSize / 2).toInt(),
                cloudBottom + snowFlakeSize / 2)
        )
        if (code == 73 || code == 75) {
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.15 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2)
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.85 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2)
            )
        }
        if (code == 75) {
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.33 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2 + snowFlakeSize)
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.67 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2  + snowFlakeSize)
            )
        }
    }
}

// Коды 51, 53, 55, 61, 63, 65, 80, 81, 82
@Composable
fun Modifier.rain(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val drop = ImageBitmap.imageResource(R.drawable.drop)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val dropFactor = when (code) {
            51, 53, 55 -> 0.1
            61, 63, 65 -> 0.15
            else -> 0.18
        }
        val dropSize = (size.width * dropFactor).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2 - dropSize * 1.5).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2 - dropSize * 1.5).toInt())
        )
        drawImage(
            image = drop,
            dstSize = IntSize(dropSize, dropSize),
            dstOffset = IntOffset((center.x - dropSize / 2).toInt(),
                cloudBottom + dropSize / 2)
        )
        if (code == 53 || code == 55 || code == 63 || code == 65 || code == 81 || code == 82) {
            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset((size.width * 0.15 - dropSize / 2).toInt(), cloudBottom + dropSize / 2)
            )
            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset((size.width * 0.85 - dropSize / 2).toInt(),
                    cloudBottom + dropSize / 2)
            )
        }
        if (code == 55 || code == 65 || code == 82) {
            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset((size.width * 0.33 - dropSize / 2).toInt(),
                    cloudBottom + dropSize / 2 + dropSize)
            )
            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset((size.width * 0.67 - dropSize / 2).toInt(),
                    cloudBottom + dropSize / 2  + dropSize)
            )
        }
    }
}

// Коды 85, 86
@Composable
fun Modifier.snowShower(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val snowFlake = ImageBitmap.imageResource(R.drawable.snowflake)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val snowFlakeFactor = 0.18
        val snowFlakeSize = (size.width * snowFlakeFactor).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2 - snowFlakeSize * 1.5).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2 - snowFlakeSize * 1.5).toInt())
        )
        drawImage(
            image = snowFlake,
            dstSize = IntSize(snowFlakeSize, snowFlakeSize),
            dstOffset = IntOffset((center.x - snowFlakeSize / 2).toInt(),
                cloudBottom + snowFlakeSize / 2)
        )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.15 - snowFlakeSize / 2).toInt(), cloudBottom + snowFlakeSize / 2)
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.85 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2)
            )
        if (code == 86) {
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.33 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2 + snowFlakeSize)
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.67 - snowFlakeSize / 2).toInt(),
                    cloudBottom + snowFlakeSize / 2  + snowFlakeSize)
            )
        }
    }
}

// Коды 56, 57, 66, 67
@Composable
fun Modifier.freezingRain(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val drop = ImageBitmap.imageResource(R.drawable.drop)
    val snowFlake = ImageBitmap.imageResource(R.drawable.snowflake)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val dropFactor = when (code) {
            56, 57 -> 0.1
            else -> 0.15
        }
        val dropSize = (size.width * dropFactor).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2 - dropSize * 1.5).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2 - dropSize * 1.5).toInt())
        )

        val (c1, c2) = if (code == 56 || code == 66) Pair(0.3, 0.7) else Pair (0.4, 0.6)
        drawImage(
            image = drop,
            dstSize = IntSize(dropSize, dropSize),
            dstOffset = IntOffset((size.width * c1 - dropSize / 2).toInt(),
                cloudBottom + dropSize / 2)
        )
        drawImage(
            image = snowFlake,
            dstSize = IntSize(dropSize, dropSize),
            dstOffset = IntOffset((size.width * c2 - dropSize / 2).toInt(),
                cloudBottom + dropSize / 2)
        )
        if (code == 57 || code == 67) {
            drawImage(
                image = snowFlake,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset((size.width * 0.2 - dropSize / 2).toInt(),
                    cloudBottom + dropSize / 2)
            )
            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset((size.width * 0.8 - dropSize / 2).toInt(),
                    cloudBottom + dropSize / 2)
            )
        }
    }
}

// Коды 95, 96, 99
@Composable
fun Modifier.thunder(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val hailColor = Color(0xFF0E6096)
    val thunder = ImageBitmap.imageResource(R.drawable.thunder)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val hailFactor = 0.12
        val hailRadius = (size.width * hailFactor / 2).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2 - hailRadius * 1.5).toInt()
        val thunderFactor = newCloudHeight.toDouble() / thunder.height * 1.0
        val newThunderHeight = (thunder.height * thunderFactor).toInt()
        val newThunderWidth = (thunder.width * thunderFactor).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2 - hailRadius * 3).toInt())
        )

        val (c1, c2) = if (code == 96) Pair(0.3, 0.7) else Pair (0.4, 0.6)
        if (code == 96 || code == 99) {
            drawCircle(
                color = hailColor,
                radius = hailRadius.toFloat(),
                center = Offset((size.width * c1 - hailRadius / 2).toFloat(),
                    (cloudBottom + hailRadius / 2).toFloat())
            )
            drawCircle(
                color = hailColor,
                radius = hailRadius.toFloat(),
                center = Offset((size.width * c2 - hailRadius / 2).toFloat(),
                    (cloudBottom + hailRadius / 2).toFloat())
            )
        }
        if (code == 99) {
            drawCircle(
                color = hailColor,
                radius = hailRadius.toFloat(),
                center = Offset((size.width * 0.2 - hailRadius / 2).toFloat(),
                    (cloudBottom + hailRadius / 2).toFloat())
            )
            drawCircle(
                color = hailColor,
                radius = hailRadius.toFloat(),
                center = Offset((size.width * 0.8 - hailRadius / 2).toFloat(),
                    (cloudBottom + hailRadius / 2).toFloat())
            )
        }
        drawImage(
            image = thunder,
            dstSize = IntSize(newThunderWidth, newThunderHeight),
            dstOffset = IntOffset(center.x.toInt() - newThunderWidth / 2,
                center.y.toInt() - (newThunderHeight / 2.5).toInt())
        )
    }
}

// Коды 77
@Composable
fun Modifier.snowGrains(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val grainsColor = Color(0xFF0E6096)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val grainsFactor = 0.07
        val grainsRadius = (size.width * grainsFactor / 2).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2 - grainsRadius * 3).toInt())
        )

        drawCircle(
            color = grainsColor,
            radius = grainsRadius.toFloat(),
            center = Offset((size.width * 0.25 - grainsRadius / 2).toFloat(),
                (cloudBottom + grainsRadius / 2).toFloat())
        )
        drawCircle(
            color = grainsColor,
            radius = grainsRadius.toFloat(),
            center = Offset((size.width * 0.5 - grainsRadius / 2).toFloat(),
                (cloudBottom + grainsRadius / 2).toFloat())
        )
        drawCircle(
                color = grainsColor,
        radius = grainsRadius.toFloat(),
        center = Offset((size.width * 0.75 - grainsRadius / 2).toFloat(),
            (cloudBottom + grainsRadius / 2).toFloat())
        )

    }
}

// Коды 45, 48
@Composable
fun Modifier.fog(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val snowFlake = ImageBitmap.imageResource(R.drawable.snowflake)
    val fogColor = Color(0xFF0E6096)
    val code = state.weatherCode
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val fogFactor = 0.07
        val fogHeight = (size.width * fogFactor / 2).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2 - fogHeight * 1).toInt()
        val snowFlakeFactor = 0.15
        val snowFlakeSize = (size.width * snowFlakeFactor).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2 - fogHeight * 2).toInt())
        )

        if (code == 45) {
            drawRoundRect(
                color = fogColor,
                topLeft = Offset((size.width * 0.15 - fogHeight / 2).toFloat(),
                    (cloudBottom + fogHeight / 2).toFloat()),
                size = Size(size.width * 0.7.toFloat(), fogHeight.toFloat()),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = fogColor,
                topLeft = Offset((size.width * 0.2 - fogHeight / 2).toFloat(),
                    (cloudBottom + fogHeight * 2.5).toFloat()),
                size = Size(size.width * 0.7.toFloat(), fogHeight.toFloat()),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = fogColor,
                topLeft = Offset((size.width * 0.15 - fogHeight / 2).toFloat(),
                    (cloudBottom + fogHeight * 4.5).toFloat()),
                size = Size(size.width * 0.7.toFloat(), fogHeight.toFloat()),
                cornerRadius = CornerRadius(8f, 8f)
            )
        }
        else {
            drawRoundRect(
                color = fogColor,
                topLeft = Offset((size.width * 0.3 - fogHeight / 2).toFloat(),
                    (cloudBottom + fogHeight / 2).toFloat()),
                size = Size(size.width * 0.4.toFloat(), fogHeight.toFloat()),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = fogColor,
                topLeft = Offset((size.width * 0.3 - fogHeight / 2).toFloat(),
                    (cloudBottom + fogHeight * 2.5).toFloat()),
                size = Size(size.width * 0.4.toFloat(), fogHeight.toFloat()),
                cornerRadius = CornerRadius(8f, 8f)
            )
            drawRoundRect(
                color = fogColor,
                topLeft = Offset((size.width * 0.3 - fogHeight / 2).toFloat(),
                    (cloudBottom + fogHeight * 4.5).toFloat()),
                size = Size(size.width * 0.4.toFloat(), fogHeight.toFloat()),
                cornerRadius = CornerRadius(8f, 8f)
            )

            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.15 - snowFlakeSize / 2).toInt(),
                    (cloudBottom + snowFlakeSize * 0.1).toInt())
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset((size.width * 0.85 - snowFlakeSize / 2).toInt(),
                    (cloudBottom + snowFlakeSize * 0.1).toInt())
            )
        }
    }
}

// Коды null
@Composable
fun Modifier.unknown(): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val sign = ImageBitmap.imageResource(R.drawable.unknown)
    val grainsColor = Color(0xFF0E6096)
    return this then Modifier.drawWithContent {
        val factorCloud = size.width / cloud.width * 1.0
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (center.y.toInt() + newCloudHeight / 2).toInt()
        val factorSign = newCloudHeight.toDouble() / sign.height.toDouble() * 0.8
        val newSignWidth = (sign.width * factorSign).toInt()
        val newSignHeight = (sign.height * factorSign).toInt()

        drawImage(
            image = cloud,
            dstSize = IntSize(newCloudWidth, newCloudHeight),
            dstOffset = IntOffset(0, (center.y.toInt() - newCloudHeight / 2))
        )
        drawImage(
            image = sign,
            dstSize = IntSize(newSignWidth, newSignHeight),
            dstOffset = IntOffset(center.x.toInt() - newSignWidth / 2, (center.y.toInt() - newSignHeight / 2))
        )
    }
}


@Preview(
    name = "cloud & sun (moon)",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun ShowWeatherIcon() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(0)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "snowfall",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun SnowFallIcon() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(75)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "rain",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun RainIcon() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(82)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "freezing rain",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun FreezingRain() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(67)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "thunder",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun Thunder() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(99)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "snow grains",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun SnowGrains() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(77)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "fog",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun Fog() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(48)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "snow shower",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun SnowShower() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(8)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}

@Preview(
    name = "unknown",
    widthDp = 50,
    heightDp = 50
)
@Composable
fun Unknown() {
    val s = UniversalWeatherState.Builder()
        .cloudiness(50)
        .weatherCode(null)
        .isDay(true)
        .build()
    WeatherIcon(universalWeatherState = s)
}