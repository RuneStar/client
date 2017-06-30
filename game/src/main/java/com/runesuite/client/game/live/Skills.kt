package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.general.Skill

object Skills {

    val levels: Map<Skill, Int>
        get() = accessor.levels.copyOf().run { asSequence()
                .withIndex()
                .plusElement(IndexedValue(Skill.OVERALL.id, sum() - (size - Skill.values().size + 1)))
                .filter { Skill.LOOKUP.containsKey(it.index) }
                .associate { Skill.LOOKUP[it.index]!! to it.value }
        }

    val currentLevels: Map<Skill, Int>
        get() = accessor.currentLevels.copyOf().run { asSequence()
                .withIndex()
                .plusElement(IndexedValue(Skill.OVERALL.id, sum() - (size - Skill.values().size + 1)))
                .filter { Skill.LOOKUP.containsKey(it.index) }
                .associate { Skill.LOOKUP[it.index]!! to it.value }
        }

    val experience: Map<Skill, Long>
        get() = accessor.experience.map { it.toLong() }.run { asSequence()
                .withIndex()
                .plusElement(IndexedValue(Skill.OVERALL.id, sum()))
                .filter { Skill.LOOKUP.containsKey(it.index) }
                .associate { Skill.LOOKUP[it.index]!! to it.value }
        }
}
