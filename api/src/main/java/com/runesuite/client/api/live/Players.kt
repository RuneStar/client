package com.runesuite.client.api.live

import com.runesuite.client.api.Player
import com.runesuite.client.raw.Client.accessor

object Players {

    val SIZE = accessor.players.size

    val local: Player? get() = accessor.localPlayer?.let { Player(it) }

    val all: List<Player> get() = accessor.players.mapNotNull { it?.let { Player(it) } }

    fun get(): List<Player?> =  accessor.players.map { it?.let { Player(it) } }

    operator fun get(index: Int): Player? = accessor.players[index]?.let { Player(it) }
}