package org.runestar.client.api.game

import org.runestar.client.api.game.live.Worlds
import org.runestar.client.api.util.BitVec
import org.runestar.client.raw.access.XWorld
import org.runestar.client.api.web.hiscore.HiscoreEndpoint

inline class World(val accessor: XWorld) {

    val id: Int get() = accessor.id

    val properties: BitVec get() = BitVec(accessor.properties)

    val host: String get() = accessor.host

    val activity: String get() = accessor.activity

    val location: Int get() = accessor.location

    val population: Int get() = accessor.population

    val hiscoreEndpoint: HiscoreEndpoint get() = Worlds.hiscoreEndpoint(properties)

    override fun toString(): String = "World($id)"
}