package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XBuddyList

abstract class BuddyList(open val accessor: XBuddyList) {

    val size get() = accessor.size0

    val capacity get() = accessor.capacity

    val isFull get() = accessor.isFull

    operator fun contains(username: Username) = accessor.contains(username.accessor)
}