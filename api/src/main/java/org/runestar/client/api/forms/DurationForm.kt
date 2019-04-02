package org.runestar.client.api.forms

import java.time.Duration
import java.time.temporal.ChronoUnit

data class DurationForm(
        val amount: Long,
        val chronoUnit: ChronoUnit
) {

    @Transient val value: Duration = Duration.of(amount, chronoUnit)
}