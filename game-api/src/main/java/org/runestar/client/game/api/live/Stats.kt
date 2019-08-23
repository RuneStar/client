package org.runestar.client.game.api.live

import org.runestar.client.cacheids.StatId
import org.runestar.client.game.raw.CLIENT

object Stats {

    val ids: IntRange get() = 0..StatId.CONSTRUCTION

    fun currentLevel(skill: Int): Int = CLIENT.currentLevels[skill]

    fun level(skill: Int): Int = CLIENT.levels[skill]

    fun experience(skill: Int): Int = CLIENT.experience[skill]

    fun boost(skill: Int): Int = currentLevel(skill) - level(skill)

    val totalExperience: Long get() = ids.fold(0L) { acc, skill -> acc + experience(skill) }

    val totalLevel: Int get() = ids.sumBy { level(it) }
}