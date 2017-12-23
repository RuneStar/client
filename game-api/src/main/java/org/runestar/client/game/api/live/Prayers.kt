package org.runestar.client.game.api.live

import org.runestar.client.game.api.LocalSkillLevel
import org.runestar.client.game.api.Prayer
import org.runestar.general.Skill

object Prayers {

    fun isEnabled(prayer: Prayer) = Game.varps[prayer.defaultVarbit.varbit] == 1

    val level: LocalSkillLevel = LocalSkillLevels[Skill.PRAYER]
}