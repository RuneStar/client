package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Player
import com.runesuite.client.game.raw.Client.accessor

object Players {

    val SIZE = accessor.players.size

    val local: com.runesuite.client.game.api.Player? get() = accessor.localPlayer?.let { com.runesuite.client.game.api.Player(it) }

    val all: List<com.runesuite.client.game.api.Player> get() = accessor.players.mapNotNull { it?.let { com.runesuite.client.game.api.Player(it) } }

    fun get(): List<com.runesuite.client.game.api.Player?> =  accessor.players.map { it?.let { com.runesuite.client.game.api.Player(it) } }

    operator fun get(index: Int): com.runesuite.client.game.api.Player? = accessor.players[index]?.let { com.runesuite.client.game.api.Player(it) }
}