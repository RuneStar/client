package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XPlayer

class Player(override val accessor: XPlayer) : Actor(accessor) {

    val name get() = accessor.name ?: ""

    val actions: List<String> get() = accessor.actions.toList()

    val combatLevel get() = accessor.combatLevel

    val prayerIcon get() = accessor.headIconPrayer

    val skullIcon get() = accessor.headIconPk

    val team get() = accessor.team

    val appearance: PlayerAppearance? get() = accessor.appearance?.let { PlayerAppearance(it) }

    override fun toString(): String {
        return "Player($name)"
    }
}