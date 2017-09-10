package com.runesuite.client.core.api

import com.runesuite.client.core.raw.access.XWorld
import com.runesuite.general.worlds.World

fun XWorld.toWorld(): World {
    return World(
            id,
            properties,
            checkNotNull(host) { "$id: null host" },
            checkNotNull(activity) { "$id: null activity" },
            location,
            population)
}