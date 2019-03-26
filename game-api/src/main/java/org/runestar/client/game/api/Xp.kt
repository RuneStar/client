package org.runestar.client.game.api

import java.util.Collections
import java.util.NavigableMap
import java.util.TreeMap

object Xp {

    const val LEVEL_MIN = 1

    const val LEVEL_MAX = 99

    const val EXPERIENCE_MIN = 0

    const val EXPERIENCE_MAX = 200_000_000

    const val VIRTUAL_LEVEL_MAX = 126

    /**
     * level -> experience
     */
    @JvmField
    val VIRTUAL_EXPERIENCE: NavigableMap<Int, Int> = Collections.unmodifiableNavigableMap(
            TreeMap<Int, Int>().apply {
                var xp = 0
                put(LEVEL_MIN, 0)
                for (i in LEVEL_MIN until VIRTUAL_LEVEL_MAX) {
                    xp += (i + 300 * Math.pow(2.0, i / 7.0)).toInt()
                    put(i + 1, xp / 4)
                }
            }
    )

    /**
     * level -> experience
     */
    @JvmField
    val EXPERIENCE: NavigableMap<Int, Int> = VIRTUAL_EXPERIENCE.headMap(LEVEL_MAX, true)

    /**
     * experience -> level
     */
    @JvmField
    val VIRTUAL_LEVELS: NavigableMap<Int, Int> = Collections.unmodifiableNavigableMap(
            VIRTUAL_EXPERIENCE.entries.associateTo(TreeMap<Int, Int>()) { it.value to it.key }
    )

    /**
     * experience -> level
     */
    @JvmField
    val LEVELS: NavigableMap<Int, Int> = VIRTUAL_LEVELS.headMap(EXPERIENCE[LEVEL_MAX], true)
}