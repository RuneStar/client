package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XPlayer

class Player(override val accessor: XPlayer) : Actor(accessor) {

    val name get() = accessor.name ?: ""

    val actions: List<String> get() = accessor.actions.toList()

    val combatLevel get() = accessor.combatLevel

    val headIconPrayer get(): HeadIconPrayer? = accessor.headIconPrayer.let {
        when (it) {
            -1 -> null
            else -> HeadIconPrayer.LOOKUP.getValue(it)
        }
    }

    val headIconPk get(): HeadIconPk? = accessor.headIconPk.let {
        when (it) {
            -1 -> null
            else -> HeadIconPk.LOOKUP.getValue(it)
        }
    }

    val team get() = accessor.team

    val appearance: PlayerAppearance? get() = accessor.appearance?.let { PlayerAppearance(it) }

    override fun toString(): String {
        return "Player($name)"
    }
}