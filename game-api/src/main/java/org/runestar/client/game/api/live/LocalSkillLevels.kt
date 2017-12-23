package org.runestar.client.game.api.live

import org.runestar.client.game.api.LocalSkillLevel
import org.runestar.client.game.raw.Client.accessor
import org.runestar.general.Skill

object LocalSkillLevels {

    @JvmStatic
    operator fun get(skill: Skill) : LocalSkillLevel {
        return if (skill == Skill.OVERALL) {
            LocalSkillLevel(
                    accessor.currentLevels.sum() - (accessor.currentLevels.size - Skill.LOOKUP.size + 1),
                    accessor.levels.sum() - (accessor.levels.size - Skill.LOOKUP.size + 1),
                    accessor.experience.fold(0L) { acc, v -> acc + v.toLong() }
            )
        } else {
            LocalSkillLevel(
                    accessor.currentLevels[skill.id],
                    accessor.levels[skill.id],
                    accessor.experience[skill.id].toLong()
            )
        }
    }
}