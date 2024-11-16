package ru.lisss79.weatherforecast.entities.weather

import java.time.LocalDate
import java.time.ZonedDateTime

sealed class UniversalTime {
    class DateOnly(val date: LocalDate?): UniversalTime()
    class DateTime(val dateTime: ZonedDateTime?) : UniversalTime()
}