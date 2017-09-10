package com.runesuite.client.core.api.live

import com.runesuite.client.core.raw.Client.accessor

object Players {

    val SIZE = accessor.players.size

    val local: com.runesuite.client.core.api.Player? get() = accessor.localPlayer?.let { com.runesuite.client.core.api.Player(it) }

    val all: List<com.runesuite.client.core.api.Player> get() = accessor.players.mapNotNull { it?.let { com.runesuite.client.core.api.Player(it) } }

    fun get(): List<com.runesuite.client.core.api.Player?> =  accessor.players.map { it?.let { com.runesuite.client.core.api.Player(it) } }

    operator fun get(index: Int): com.runesuite.client.core.api.Player? = accessor.players[index]?.let { com.runesuite.client.core.api.Player(it) }
}