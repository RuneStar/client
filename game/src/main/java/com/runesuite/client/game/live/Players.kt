package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Player

object Players {

    val SIZE = accessor.players.size

    val local: Player? get() = accessor.localPlayer?.let { Player(it) }

    val all: List<Player> get() = accessor.players.mapNotNull { it?.let { Player(it) } }

    fun get(): List<Player?> =  accessor.players.map { it?.let { Player(it) } }

    operator fun get(index: Int): Player? = accessor.players[index]?.let { Player(it) }
}