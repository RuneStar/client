package com.runesuite.client.game

import com.runesuite.client.base.access.XWorld
import com.runesuite.general.World

fun XWorld.toWorld(): World {
    return World(
            id,
            properties,
            checkNotNull(host) { "$id: null host" },
            checkNotNull(activity) { "$id: null activity" },
            location,
            population)
}