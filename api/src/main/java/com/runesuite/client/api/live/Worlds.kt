package com.runesuite.client.api.live

import com.runesuite.client.api.toWorld
import com.runesuite.client.raw.Client.accessor
import com.runesuite.general.worlds.World
import com.runesuite.general.worlds.worlds

object Worlds {

    private val backup by lazy { worlds() }

    val all: List<World> get() = accessor.worlds?.mapNotNull { it?.toWorld() } ?: backup.values.toList()

    operator fun get(id: Int): World = accessor.worlds?.firstOrNull { it.id == id }?.toWorld() ?:
            checkNotNull(backup[id]) { "Invalid id: $id" }

    val local get() = get(accessor.worldId)
}