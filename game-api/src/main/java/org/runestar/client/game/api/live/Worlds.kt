package org.runestar.client.game.api.live

import org.runestar.client.game.api.World
import org.runestar.client.game.raw.Client.accessor
import org.runestar.general.World

object Worlds : AbstractCollection<World>() {

    private val backup by lazy { World.loadAll().values.toList() }

    override fun iterator(): Iterator<World> {
        return if (accessor.worlds == null) {
            backup.iterator()
        } else {
            object : Iterator<World> {
                private var i = 0
                override fun hasNext() = i < accessor.worldsCount
                override fun next() = World(accessor.worlds[i++])
            }
        }
    }

    override val size get() = if (accessor.worlds == null) backup.size else accessor.worldsCount

    val local get() = get(accessor.worldId)

    operator fun get(id: Int) = first { it.id == id }
}