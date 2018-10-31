package org.runestar.client.game.api.live

import org.runestar.client.game.api.Player
import org.runestar.client.game.raw.CLIENT

object Players : AbstractCollection<Player>() {

    val local: Player? get() = CLIENT.localPlayer?.let { Player(it) }

    override fun iterator() = iterator {
        var i = 0
        while (i < size) {
            val player = get(CLIENT.players_indices[i++]) ?: break
            yield(player)
        }
    }

    override val size get() = CLIENT.players_count

    operator fun get(index: Int): Player? = CLIENT.players[index]?.let { Player(it) }
}