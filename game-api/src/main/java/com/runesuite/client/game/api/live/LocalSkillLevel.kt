package com.runesuite.client.game.api.live

import com.runesuite.client.game.raw.Client
import com.runesuite.general.Skill

data class LocalSkillLevel(
        val currentLevel: Int,
        val level: Int,
        val experience: Long
) {

    override fun toString(): String {
        return "LocalSkillLevel($currentLevel/$level, xp=$experience)"
    }

    companion object {

        @JvmStatic
        fun get(skill: Skill) : LocalSkillLevel {
            return if (skill == Skill.OVERALL) {
                LocalSkillLevel(
                        Client.accessor.currentLevels.sum() - (Client.accessor.currentLevels.size - Skill.LOOKUP.size + 1),
                        Client.accessor.levels.sum() - (Client.accessor.levels.size - Skill.LOOKUP.size + 1),
                        Client.accessor.experience.fold(0L) { acc, v -> acc + v.toLong() }
                )
            } else {
                LocalSkillLevel(
                        Client.accessor.currentLevels[skill.id],
                        Client.accessor.levels[skill.id],
                        Client.accessor.experience[skill.id].toLong()
                )
            }
        }
    }
}

val Skill.localLevel get() = LocalSkillLevel.get(this)