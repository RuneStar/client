package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.World
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.general.worlds.World
import com.runesuite.general.worlds.worlds

object Worlds {

    private val backup by lazy { worlds() }

    val all: List<World> get() = accessor.worlds?.mapNotNull { it?.let { World(it) } } ?: backup.values.toList()

    operator fun get(id: Int): World = accessor.worlds?.firstOrNull { it.id == id }?.let { World(it) } ?:
            checkNotNull(backup[id]) { "Invalid id: $id" }

    val local get() = get(accessor.worldId)
}