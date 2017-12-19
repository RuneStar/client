package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.LocalSkillLevel
import com.runesuite.client.game.api.Prayer
import com.runesuite.general.Skill

object Prayers {

    fun isEnabled(prayer: Prayer) = Game.varps[prayer.defaultVarbit.varbit] == 1

    val level: LocalSkillLevel = LocalSkillLevels[Skill.PRAYER]
}