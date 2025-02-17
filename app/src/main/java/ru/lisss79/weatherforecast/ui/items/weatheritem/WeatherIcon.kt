package ru.lisss79.weatherforecast.ui.items.weatheritem

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.EaseInElastic
import androidx.compose.animation.core.EaseInOutBounce
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import ru.lisss79.weatherforecast.R
import ru.lisss79.weatherforecast.entities.weather.UniversalWeatherState
import kotlin.math.sqrt
import kotlin.random.Random

@Composable
fun WeatherIcon(
    modifier: Modifier = Modifier,
    universalWeatherState: UniversalWeatherState?
) {
    Canvas(
        modifier = modifier.weatherIconModifier(universalWeatherState)
    ) { }
}

@Composable
fun Modifier.weatherIconModifier(
    universalWeatherState: UniversalWeatherState?
): Modifier {
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

    val randomValue = (-50..50).random()
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Yellow,
        targetValue = if (state.getDay() == false) Color(0xFFF8F8B8) else Color(0xFFFFD700),
        animationSpec = infiniteRepeatable(
            animation = tween(800 + randomValue, easing = EaseInOutBounce),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorAnimation"
    )

    return this then Modifier.drawWithCache {
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

        onDrawBehind {
            drawImage(
                image = star,
                dstSize = IntSize(newStarWidth, newStarHeight),
                dstOffset = IntOffset(
                    (size.width - newStarWidth * 1.3).toInt(),
                    center.y.toInt() - newStarHeight / 2
                ),
                colorFilter = ColorFilter.tint(color)
            )
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth1, newCloudHeight1),
                dstOffset = IntOffset(0, center.y.toInt() + newCloudHeight / 2 - newCloudHeight1)
            )
        }
    }
}

// Коды 71, 73, 75
@Composable
fun Modifier.snowFall(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val snowFlake = ImageBitmap.imageResource(R.drawable.snowflake)
    val code = state.weatherCode
    val number = when (code) {
        71 -> 1
        73 -> 3
        else -> 5
    }
    val animPosition = getAnimation(number)

    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val snowFlakeSize = (size.width * 0.12).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = size.height / 2 + newCloudHeight / 2 - snowFlakeSize
        val positionFactor = (size.height - cloudBottom + 2 * snowFlakeSize) / 100

        onDrawBehind {
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset(
                    (center.x - snowFlakeSize / 2).toInt(),
                    //cloudBottom + snowFlakeSize / 2
                    (cloudBottom - snowFlakeSize + animPosition[0] * positionFactor).toInt()
                )
            )
            if (code == 73 || code == 75) {
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.15 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + animPosition[1] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.85 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + animPosition[2] * positionFactor).toInt()
                    )
                )
            }
            if (code == 75) {
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.33 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + animPosition[3] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.67 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + animPosition[4] * positionFactor).toInt()
                    )
                )
            }
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(0, center.y.toInt() - newCloudHeight / 2 - snowFlakeSize)
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
    val number = when (code) {
        51, 61, 80 -> 1
        53, 63, 81 -> 3
        else -> 5
    }
    val animPosition = getAnimation(number)
    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val dropFactor = when (code) {
            51, 53, 55 -> 0.1
            61, 63, 65 -> 0.15
            else -> 0.18
        }
        val dropSize = (size.width * dropFactor).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (size.height / 2 + newCloudHeight / 2 - dropSize * 1.5).toInt()
        val positionFactor = (size.height - cloudBottom + 2 * dropSize) / 100

        onDrawBehind {
            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset(
                    (center.x - dropSize / 2).toInt(),
                    (cloudBottom - dropSize + animPosition[0] * positionFactor).toInt()
                )
            )
            if (code == 53 || code == 55 || code == 63 || code == 65 || code == 81 || code == 82) {
                drawImage(
                    image = drop,
                    dstSize = IntSize(dropSize, dropSize),
                    dstOffset = IntOffset(
                        (size.width * 0.15 - dropSize / 2).toInt(),
                        (cloudBottom - dropSize + animPosition[1] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = drop,
                    dstSize = IntSize(dropSize, dropSize),
                    dstOffset = IntOffset(
                        (size.width * 0.85 - dropSize / 2).toInt(),
                        (cloudBottom - dropSize + animPosition[2] * positionFactor).toInt()
                    )
                )
            }
            if (code == 55 || code == 65 || code == 82) {
                drawImage(
                    image = drop,
                    dstSize = IntSize(dropSize, dropSize),
                    dstOffset = IntOffset(
                        (size.width * 0.33 - dropSize / 2).toInt(),
                        (cloudBottom - dropSize + animPosition[3] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = drop,
                    dstSize = IntSize(dropSize, dropSize),
                    dstOffset = IntOffset(
                        (size.width * 0.67 - dropSize / 2).toInt(),
                        (cloudBottom - dropSize + animPosition[4] * positionFactor).toInt()
                    )
                )
            }
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(
                    0,
                    (center.y.toInt() - newCloudHeight / 2 - dropSize * 1.5).toInt()
                )
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
    val number = when (code) {
        85 -> 3
        else -> 5
    }
    val animPosition = getAnimation(number)

    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val snowFlakeFactor = 0.18
        val snowFlakeSize = (size.width * snowFlakeFactor).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom =
            ((size.width / 2).toInt() + newCloudHeight / 2 - snowFlakeSize * 1.5).toInt()
        val positionFactor = (size.height - cloudBottom + 2 * snowFlakeSize) / 100

        onDrawBehind {
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset(
                    (center.x - snowFlakeSize / 2).toInt(),
                    (cloudBottom - snowFlakeSize + animPosition[0] * positionFactor).toInt()
                )
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset(
                    (size.width * 0.15 - snowFlakeSize / 2).toInt(),
                    (cloudBottom - snowFlakeSize + animPosition[1] * positionFactor).toInt()
                )
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                dstOffset = IntOffset(
                    (size.width * 0.85 - snowFlakeSize / 2).toInt(),
                    (cloudBottom - snowFlakeSize + animPosition[2] * positionFactor).toInt()
                )
            )
            if (code == 86) {
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.33 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + animPosition[3] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.67 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + animPosition[4] * positionFactor).toInt()
                    )
                )
            }
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(
                    0,
                    (center.y.toInt() - newCloudHeight / 2 - snowFlakeSize * 1.5).toInt()
                )
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
    val number = when (code) {
        56, 66 -> 2
        else -> 4
    }
    val animPosition = getAnimation(number)

    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val dropFactor = when (code) {
            56, 57 -> 0.1
            else -> 0.15
        }
        val dropSize = (size.width * dropFactor).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (size.width / 2).toInt() + newCloudHeight / 2 - dropSize * 1.5
        val positionFactor = (size.height - cloudBottom + 2 * dropSize) / 100
        val (c1, c2) = if (code == 56 || code == 66) Pair(0.3, 0.7) else Pair(0.4, 0.6)

        onDrawBehind {

            drawImage(
                image = drop,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset(
                    (size.width * c1 - dropSize / 2).toInt(),
                    (cloudBottom - dropSize + animPosition[0] * positionFactor).toInt()
                )
            )
            drawImage(
                image = snowFlake,
                dstSize = IntSize(dropSize, dropSize),
                dstOffset = IntOffset(
                    (size.width * c2 - dropSize / 2).toInt(),
                    (cloudBottom - dropSize + animPosition[1] * positionFactor).toInt()
                )
            )
            if (code == 57 || code == 67) {
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(dropSize, dropSize),
                    dstOffset = IntOffset(
                        (size.width * 0.2 - dropSize / 2).toInt(),
                        (cloudBottom - dropSize + animPosition[2] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = drop,
                    dstSize = IntSize(dropSize, dropSize),
                    dstOffset = IntOffset(
                        (size.width * 0.8 - dropSize / 2).toInt(),
                        (cloudBottom - dropSize + animPosition[3] * positionFactor).toInt()
                    )
                )
            }
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(
                    0,
                    (center.y.toInt() - newCloudHeight / 2 - dropSize * 1.5).toInt()
                )
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

    val randomValue = (-50..50).random()
    val infiniteTransition = rememberInfiniteTransition()
    val color by infiniteTransition.animateColor(
        initialValue = Color.Yellow,
        targetValue = Color.Red,
        animationSpec = infiniteRepeatable(
            animation = tween(1500 + randomValue, easing = EaseInElastic),
            repeatMode = RepeatMode.Reverse
        ),
        label = "colorAnimation"
    )
    val number = when (code) {
        95 -> 2
        else -> 4
    }
    val animPosition = getAnimation(number)

    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val hailFactor = 0.12
        val hailRadius = (size.width * hailFactor / 2).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = ((size.width / 2).toInt() + newCloudHeight / 2 - hailRadius * 1.5).toInt()
        val thunderFactor = newCloudHeight.toDouble() / thunder.height * 1.0
        val newThunderHeight = (thunder.height * thunderFactor).toInt()
        val newThunderWidth = (thunder.width * thunderFactor).toInt()
        val positionFactor = (size.height - cloudBottom + 4 * hailRadius) / 100

        onDrawBehind {
            val (c1, c2) = if (code == 96) Pair(0.3, 0.7) else Pair(0.4, 0.6)
            if (code == 96 || code == 99) {
                drawCircle(
                    color = hailColor,
                    radius = hailRadius.toFloat(),
                    center = Offset(
                        (size.width * c1 - hailRadius / 2).toFloat(),
                        cloudBottom - hailRadius * 4 + animPosition[0] * positionFactor
                    )
                )
                drawCircle(
                    color = hailColor,
                    radius = hailRadius.toFloat(),
                    center = Offset(
                        (size.width * c2 - hailRadius / 2).toFloat(),
                        cloudBottom - hailRadius * 4 + animPosition[1] * positionFactor
                    )
                )
            }
            if (code == 99) {
                drawCircle(
                    color = hailColor,
                    radius = hailRadius.toFloat(),
                    center = Offset(
                        (size.width * 0.2 - hailRadius / 2).toFloat(),
                        cloudBottom - hailRadius * 4 + animPosition[2] * positionFactor
                    )
                )
                drawCircle(
                    color = hailColor,
                    radius = hailRadius.toFloat(),
                    center = Offset(
                        (size.width * 0.8 - hailRadius / 2).toFloat(),
                        cloudBottom - hailRadius * 4 + animPosition[3] * positionFactor
                    )
                )
            }
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(
                    0,
                    (center.y.toInt() - newCloudHeight / 2 - hailRadius * 3)
                )
            )
            drawImage(
                image = thunder,
                dstSize = IntSize(newThunderWidth, newThunderHeight),
                dstOffset = IntOffset(
                    center.x.toInt() - newThunderWidth / 2,
                    center.y.toInt() - (newThunderHeight / 2.5).toInt()
                ),
                colorFilter = ColorFilter.tint(color)
            )
        }
    }
}

// Коды 77
@Composable
fun Modifier.snowGrains(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val grainsColor = Color(0xFF0E6096)
    val code = state.weatherCode
    val animPosition = getAnimation(4)

    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val grainsFactor = 0.07
        val grainsRadius = (size.width * grainsFactor / 2).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (size.width / 2).toInt() + newCloudHeight / 2
        val positionFactor = (size.height - cloudBottom + 4 * grainsRadius) / 100

        onDrawBehind {

            drawCircle(
                color = grainsColor,
                radius = grainsRadius.toFloat(),
                center = Offset(
                    (size.width * 0.25).toFloat(),
                    cloudBottom - grainsRadius * 4 + animPosition[0] * positionFactor
                )
            )
            drawCircle(
                color = grainsColor,
                radius = grainsRadius.toFloat(),
                center = Offset(
                    (size.width * 0.5).toFloat(),
                    cloudBottom - grainsRadius * 4 + animPosition[1] * positionFactor
                )
            )
            drawCircle(
                color = grainsColor,
                radius = grainsRadius.toFloat(),
                center = Offset(
                    (size.width * 0.75).toFloat(),
                    cloudBottom - grainsRadius * 4 + animPosition[2] * positionFactor
                )
            )
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(
                    0,
                    center.y.toInt() - newCloudHeight / 2 - grainsRadius * 3
                )
            )
        }
    }
}

// Коды 45, 48
@Composable
fun Modifier.fog(state: UniversalWeatherState): Modifier {
    val cloud = ImageBitmap.imageResource(R.drawable.cloud)
    val snowFlake = ImageBitmap.imageResource(R.drawable.snowflake)
    val fogColor = Color(0xFF0E6096)
    val code = state.weatherCode
    val animPosition = getAnimation(3, false)
    val snowAnimPosition = if (code == 48) getAnimation(number = 2, true) else listOf()

    return this then Modifier.drawWithCache {
        val factorCloud = size.width / cloud.width * 1.0
        val fogFactor = 0.07
        val fogHeight = (size.width * fogFactor / 2).toInt()
        val newCloudWidth = (cloud.width * factorCloud).toInt()
        val newCloudHeight = (cloud.height * factorCloud).toInt()
        val cloudBottom = (size.width / 2).toInt() + newCloudHeight / 2 - fogHeight * 1
        val snowFlakeFactor = 0.15
        val snowFlakeSize = (size.width * snowFlakeFactor).toInt()
        val positionFactor = (size.height - cloudBottom + 4 * snowFlakeSize) / 100

        onDrawBehind {
            if (code == 45) {
                drawRoundRect(
                    color = fogColor,
                    topLeft = Offset(
                        (size.width * 0.15 - fogHeight / 2).toFloat(),
                        (cloudBottom + fogHeight / 2).toFloat()
                    ),
                    size = Size(size.width * 0.7.toFloat(),
                        fogHeight.toFloat() * animPosition[0] / 100),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = fogColor,
                    topLeft = Offset(
                        (size.width * 0.2 - fogHeight / 2).toFloat(),
                        (cloudBottom + fogHeight * 2.5).toFloat()
                    ),
                    size = Size(size.width * 0.7.toFloat(),
                        fogHeight.toFloat() * animPosition[1] / 100),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = fogColor,
                    topLeft = Offset(
                        (size.width * 0.15 - fogHeight / 2).toFloat(),
                        (cloudBottom + fogHeight * 4.5).toFloat()
                    ),
                    size = Size(size.width * 0.7.toFloat(),
                        fogHeight.toFloat() * animPosition[2] / 100),
                    cornerRadius = CornerRadius(8f, 8f)
                )
            } else {
                drawRoundRect(
                    color = fogColor,
                    topLeft = Offset(
                        (size.width * 0.3 - fogHeight / 2).toFloat(),
                        (cloudBottom + fogHeight / 2).toFloat()
                    ),
                    size = Size(size.width * 0.4.toFloat(),
                        fogHeight.toFloat() * animPosition[0] / 100),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = fogColor,
                    topLeft = Offset(
                        (size.width * 0.3 - fogHeight / 2).toFloat(),
                        (cloudBottom + fogHeight * 2.5).toFloat()
                    ),
                    size = Size(size.width * 0.4.toFloat(),
                        fogHeight.toFloat() * animPosition[1] / 100),
                    cornerRadius = CornerRadius(8f, 8f)
                )
                drawRoundRect(
                    color = fogColor,
                    topLeft = Offset(
                        (size.width * 0.3 - fogHeight / 2).toFloat(),
                        (cloudBottom + fogHeight * 4.5).toFloat()
                    ),
                    size = Size(size.width * 0.4.toFloat(),
                        fogHeight.toFloat() * animPosition[2] / 100),
                    cornerRadius = CornerRadius(8f, 8f)
                )

                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.15 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + snowAnimPosition[0] * positionFactor).toInt()
                    )
                )
                drawImage(
                    image = snowFlake,
                    dstSize = IntSize(snowFlakeSize, snowFlakeSize),
                    dstOffset = IntOffset(
                        (size.width * 0.85 - snowFlakeSize / 2).toInt(),
                        (cloudBottom - snowFlakeSize + snowAnimPosition[1] * positionFactor).toInt()
                    )
                )
            }
            drawImage(
                image = cloud,
                dstSize = IntSize(newCloudWidth, newCloudHeight),
                dstOffset = IntOffset(
                    0,
                    (center.y.toInt() - newCloudHeight / 2 - fogHeight * 2)
                )
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
            dstOffset = IntOffset(
                center.x.toInt() - newSignWidth / 2,
                (center.y.toInt() - newSignHeight / 2)
            )
        )
    }
}

@Composable
fun getAnimation(number: Int, restart: Boolean = true): List<Float> {
    val infiniteTransition = rememberInfiniteTransition()

    val animations = List(number) { 0f }.toMutableList()
    for (index in 0 until number) {
        val animPosition by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 100f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000 + Random.nextInt(500), easing = EaseIn),
                repeatMode = if (restart) RepeatMode.Restart else RepeatMode.Reverse,
                initialStartOffset = StartOffset(Random.nextInt(120))
            ),
            label = "positionAnimation1"
        )
        animations[index] = animPosition
    }
    return animations
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
        .weatherCode(86)
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