package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.general.Skill

object Skills {

    val levels get() = levels(accessor.levels)

    val currentLevels get() = levels(accessor.currentLevels)

    private fun levels(levels: IntArray): Map<Skill, Int> {
        return levels.copyOf().run {
            asSequence()
                    .withIndex()
                    .plusElement(IndexedValue(Skill.OVERALL.id, sum() - (size - Skill.LOOKUP.size + 1)))
                    .filter { Skill.LOOKUP.containsKey(it.index) }
                    .associate { Skill.LOOKUP[it.index]!! to it.value }
        }
    }

    val experience: Map<Skill, Long>
        get() = accessor.experience.map { it.toLong() }.run { asSequence()
                .withIndex()
                .plusElement(IndexedValue(Skill.OVERALL.id, sum()))
                .filter { Skill.LOOKUP.containsKey(it.index) }
                .associate { Skill.LOOKUP[it.index]!! to it.value }
        }
}
