package com.runesuite.client.game.api

import com.runesuite.client.game.raw.access.XWorld
import com.runesuite.general.worlds.World

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