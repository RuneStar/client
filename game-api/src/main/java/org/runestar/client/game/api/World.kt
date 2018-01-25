@file:JvmName("World")

package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XWorld
import org.runestar.general.World

fun World(xWorld: XWorld): World {
    return xWorld.run {
        World(
                id,
                properties,
                checkNotNull(host) { "$id: null host" },
                checkNotNull(activity) { "$id: null activity" },
                location,
                population
        )
    }
}