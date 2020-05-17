package org.runestar.client.api.game.live

import org.runestar.client.cacheids.StatId
import org.runestar.client.api.game.EnumId
import org.runestar.client.raw.CLIENT

object Stats {

    fun name(stat: Int): String = Enums[EnumId.STAT_NAMES].getString(stat)

    val ids: IntRange get() = 0..StatId.CONSTRUCTION

    fun currentLevel(stat: Int): Int = CLIENT.currentLevels[stat]

    fun level(stat: Int): Int = CLIENT.levels[stat]

    fun experience(stat: Int): Int = CLIENT.experience[stat]

    fun boost(stat: Int): Int = currentLevel(stat) - level(stat)

    val totalExperience: Long get() = ids.fold(0L) { acc, stat -> acc + experience(stat) }

    val totalLevel: Int get() = ids.sumBy { level(it) }
}