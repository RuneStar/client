package org.runestar.client.api.forms

import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class DateTimeFormatterForm(
        val pattern: String,
        val zoneId: String?
) {

    @Transient val value: DateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            .withZone(if (zoneId.isNullOrEmpty()) ZoneId.systemDefault() else ZoneId.of(zoneId))
}