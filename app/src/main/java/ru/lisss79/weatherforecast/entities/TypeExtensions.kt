package ru.lisss79.weatherforecast.entities

import com.huawei.hms.site.api.model.Coordinate
import com.huawei.hms.site.api.model.Site

fun Boolean.toInt() = if (this) 1 else 0
fun Int.toBoolean() = this == 1
fun List<Int>.toBooleanArray() = map { it.toBoolean() }

fun Site.toCoords(): Coords {
    val lat = this.location.lat
    val lng = this.location.lng
    return Coords(lat, lng)
}

fun Site.copy(newOffset: Int? = null): Site {
    val newSite = Site().also { new ->
        new.location = this.location
        new.address = this.address
        new.formatAddress = this.formatAddress
        new.utcOffset = newOffset ?: this.utcOffset
        new.name = this.name
    }
    return newSite
}
