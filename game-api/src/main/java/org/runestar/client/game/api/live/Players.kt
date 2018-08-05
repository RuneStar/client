package org.runestar.client.game.api.live

import org.runestar.client.game.api.Player
import org.runestar.client.game.raw.CLIENT

object Players : AbstractCollection<Player>() {

    val local: Player? get() = CLIENT.localPlayer?.let { Player(it) }

    override fun iterator() = object : AbstractIterator<Player>() {

        private var i = 0

        override fun computeNext() {
            if (i >= size) return done()
            val player = get(CLIENT.players_indices[i++]) ?: return done()
            setNext(player)
        }
    }

    override val size get() = CLIENT.players_count

    operator fun get(index: Int): Player? = CLIENT.players[index]?.let { Player(it) }
}