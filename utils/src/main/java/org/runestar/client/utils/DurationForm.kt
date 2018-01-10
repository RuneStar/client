package org.runestar.client.utils

import java.time.Duration
import java.time.temporal.ChronoUnit
import java.util.function.Supplier

data class DurationForm(
        val amount: Long,
        val chronoUnit: ChronoUnit
) : Supplier<Duration> {

    @Transient
    private val value = Duration.of(amount, chronoUnit)

    override fun get() = value
}