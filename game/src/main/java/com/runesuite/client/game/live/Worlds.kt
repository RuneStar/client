package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.toWorld
import com.runesuite.general.World
import com.runesuite.general.WorldSelection

object Worlds {

    private val backup by lazy { WorldSelection.getWorlds() }

    val all: List<World> get() = accessor.worlds?.mapNotNull { it?.toWorld() } ?: backup.values.toList()

    operator fun get(id: Int): World = accessor.worlds?.firstOrNull { it.id == id }?.toWorld() ?:
            checkNotNull(backup[id]) { "Invalid id: $id" }

    val local get() = get(accessor.worldId)
}