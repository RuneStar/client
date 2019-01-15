package org.runestar.client.api.forms

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.function.Supplier

data class DateTimeFormatterForm(
        val pattern: String,
        val zoneId: String?
) : Supplier<DateTimeFormatter> {

    @Transient
    private val value: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            .withZone(if (zoneId.isNullOrEmpty()) ZoneId.systemDefault() else ZoneId.of(zoneId))

    override fun get() = value
}