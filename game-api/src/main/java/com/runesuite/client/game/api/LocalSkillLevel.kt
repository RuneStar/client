package com.runesuite.client.game.api

data class LocalSkillLevel(
        val currentLevel: Int,
        val level: Int,
        val experience: Long
) {

    override fun toString(): String {
        return "LocalSkillLevel($currentLevel/$level, xp=$experience)"
    }
}