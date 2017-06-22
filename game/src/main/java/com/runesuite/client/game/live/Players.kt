package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.Player

object Players {

    val SIZE = accessor.players.size

    val local: Player? get() = accessor.localPlayer?.let { Player(it) }

    val all: Sequence<Player> get() = accessor.players.copyOf().asSequence().filterNotNull().map { Player(it) }

    fun get(): List<Player?> =  accessor.players.copyOf().map { it?.let { Player(it) } }

    operator fun get(index: Int): Player? = accessor.players[index]?.let { Player(it) }
}