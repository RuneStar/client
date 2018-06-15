package org.runestar.client.api.forms

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.function.Supplier

data class DurationForm(
        val amount: Long,
        val chronoUnit: ChronoUnit
) : Supplier<Duration> {

    @Transient
    private val value: Duration = Duration.of(amount, chronoUnit)

    override fun get() = value
}