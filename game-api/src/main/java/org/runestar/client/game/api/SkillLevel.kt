package org.runestar.client.game.api

data class SkillLevel(
        val currentLevel: Int,
        val level: Int,
        val experience: Long
) {

    override fun toString(): String {
        return "SkillLevel($currentLevel/$level, xp=$experience)"
    }
}