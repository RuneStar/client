package org.runestar.client.game.api.live

import org.runestar.client.game.api.World
import org.runestar.client.game.raw.CLIENT
import org.runestar.general.World

object Worlds : AbstractCollection<World>() {

    private val backup by lazy { World.loadAll().values.toList() }

    override fun iterator(): Iterator<World> {
        return if (CLIENT.worlds == null) {
            backup.iterator()
        } else {
            object : Iterator<World> {
                private var i = 0
                override fun hasNext() = i < CLIENT.worldsCount
                override fun next() = World(CLIENT.worlds[i++])
            }
        }
    }

    override val size get() = if (CLIENT.worlds == null) backup.size else CLIENT.worldsCount

    val local get() = get(CLIENT.worldId)

    operator fun get(id: Int) = first { it.id == id }
}