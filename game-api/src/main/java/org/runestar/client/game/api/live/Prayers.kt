package org.runestar.client.game.api.live

import org.runestar.client.game.api.Prayer
import org.runestar.client.game.api.SkillLevel
import org.runestar.general.Skill

object Prayers {

    fun isEnabled(prayer: Prayer) = Game.getVarbit(prayer.varbit) == 1

    val level: SkillLevel get() = Stats[Skill.PRAYER]
}