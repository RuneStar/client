package org.runestar.client.api.web.ge

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.InputStream
import java.util.NavigableMap

data class GeGraphResult(
        val daily: NavigableMap<Long, Int>,
        val average: NavigableMap<Long, Int>
) {

    val latestPrice: Int get() = daily.lastEntry().value

    internal companion object {

        private val mapper = ObjectMapper().findAndRegisterModules()

        fun of(input: InputStream): GeGraphResult = mapper.readValue(input)
    }
}