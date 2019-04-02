package org.runestar.client.api.forms

data class RegexForm(
        val regex: String,
        val options: Set<RegexOption> = emptySet()
) {

    @Transient val value: Regex = regex.toRegex(options)
}