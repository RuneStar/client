package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.game.toWorld
import com.runesuite.general.World
import com.runesuite.general.WorldSelection

object Worlds {

    private val backup by lazy { WorldSelection.getWorlds() }

    val all: Sequence<World> get() = accessor.worlds?.copyOf()?.asSequence()?.filterNotNull()?.map { it.toWorld() } ?:
            backup.values.asSequence()

    operator fun get(id: Int): World = accessor.worlds?.copyOf()?.firstOrNull { it.id == id }?.toWorld() ?:
            checkNotNull(backup[id]) { "Invalid id: $id" }

    val local get() = get(accessor.worldId)
}