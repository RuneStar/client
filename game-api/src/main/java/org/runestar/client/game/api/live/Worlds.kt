package org.runestar.client.game.api.live

import org.runestar.client.game.api.World
import org.runestar.client.game.raw.Client.accessor
import org.runestar.general.World

object Worlds {

    private val backup by lazy { World.loadAll() }

    val all: List<World> get() = accessor.worlds?.mapNotNull { it?.let { World(it) } } ?: backup.values.toList()

    operator fun get(id: Int): World = accessor.worlds?.firstOrNull { it.id == id }?.let { World(it) } ?:
            checkNotNull(backup[id]) { "Invalid id: $id" }

    val local get() = get(accessor.worldId)
}