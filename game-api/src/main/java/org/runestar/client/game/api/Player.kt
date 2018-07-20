package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XPlayer
import kotlin.math.max

class Player(override val accessor: XPlayer) : Actor(accessor) {

    override val plane: Int get() = accessor.plane

    val name: Username? get() = accessor.username?.let { Username(it) }

    val actions: List<String> get() = accessor.actions.toList()

    val combatLevel get() = accessor.combatLevel

    val headIconPrayer get(): HeadIconPrayer? = HeadIconPrayer.of(accessor.headIconPrayer)

    val headIconPk get(): HeadIconPk? = HeadIconPk.of(accessor.headIconPk)

    val team get() = accessor.team

    val appearance: PlayerAppearance? get() = accessor.appearance?.let { PlayerAppearance(it) }

    override fun toString(): String {
        return "Player(name=$name)"
    }

    companion object {

        /**
         * Returns the combat level of a player who has the given stats.
         */
        fun combatLevel(attack: Int, strength: Int, defence: Int, ranged: Int, prayer: Int, magic: Int, hitpoints: Int): Int {
            val base = defence + hitpoints + prayer / 2
            val offenseMelee = attack + strength
            val offenseRanged =  3 * ranged / 2
            val offenseMagic = 3 * magic / 2
            val offense = max(offenseMelee, max(offenseRanged, offenseMagic))
            return (base + 13 * offense / 10) / 4
        }
    }
}