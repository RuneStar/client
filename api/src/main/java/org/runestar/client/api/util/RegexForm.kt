package org.runestar.client.api.util

import java.util.function.Supplier

data class RegexForm(
        val regex: String,
        val options: Set<RegexOption>
) : Supplier<Regex> {

    @Transient
    private val value: Regex = regex.toRegex(options)

    override fun get() = value
}