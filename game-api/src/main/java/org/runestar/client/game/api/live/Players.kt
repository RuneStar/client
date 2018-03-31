package org.runestar.client.game.api.live

import org.runestar.client.game.api.Player
import org.runestar.client.game.raw.Client.accessor

object Players : AbstractCollection<Player>() {

    val local: Player? get() = accessor.localPlayer?.let { Player(it) }

    override fun iterator() = object : AbstractIterator<Player>() {

        private var i = 0

        override fun computeNext() {
            if (i >= size) return done()
            val x = accessor.players[accessor.players_indices[i++]] ?: return done()
            setNext(Player(x))
        }
    }

    override val size get() = accessor.players_count

    internal operator fun get(index: Int): Player? = accessor.players[index]?.let { Player(it) }
}