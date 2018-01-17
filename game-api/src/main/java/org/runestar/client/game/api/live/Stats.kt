package org.runestar.client.game.api.live

import org.runestar.client.game.api.SkillLevel
import org.runestar.client.game.raw.Client.accessor
import org.runestar.general.Skill

object Stats {

    @JvmStatic
    operator fun get(skill: Skill) : SkillLevel {
        return when(skill) {
            Skill.OVERALL ->
                SkillLevel(
                        accessor.currentLevels.sum() - (accessor.currentLevels.size - Skill.VALUES.size + 1),
                        accessor.levels.sum() - (accessor.levels.size - Skill.VALUES.size + 1),
                        accessor.experience.fold(0L) { acc, v -> acc + v.toLong() }
                )
            else ->
                SkillLevel(
                        accessor.currentLevels[skill.id],
                        accessor.levels[skill.id],
                        accessor.experience[skill.id].toLong()
                )
        }
    }
}