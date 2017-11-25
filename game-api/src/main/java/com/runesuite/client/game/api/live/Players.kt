package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Player
import com.runesuite.client.game.raw.Client.accessor

object Players {

    val CAPACITY = accessor.players_players.size

    val local: Player? get() = accessor.localPlayer?.let { Player(it) }

    val all: List<Player> get() = List(count) { Player(accessor.players_players[accessor.players_indices[it]]) }

    val count get() = accessor.players_count

    val indices: List<Int> get() = accessor.players_indices.take(count)

    fun get(): List<Player?> =  accessor.players_players.map { it?.let { Player(it) } }

    operator fun get(index: Int): Player? = accessor.players_players[index]?.let { Player(it) }
}