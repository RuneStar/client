package com.runesuite.client.api

import com.runesuite.client.raw.access.XWorld
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