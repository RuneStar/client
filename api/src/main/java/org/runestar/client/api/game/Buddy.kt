package org.runestar.client.api.game

import org.runestar.client.api.game.live.Worlds
import org.runestar.client.raw.access.XBuddy

abstract class Buddy(override val accessor: XBuddy) : User(accessor) {

    val worldId get() = accessor.world0

    val world get() = Worlds[worldId]

    val rank: Int get() = accessor.rank
}