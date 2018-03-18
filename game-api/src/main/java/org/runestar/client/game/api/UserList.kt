package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XUserList

abstract class UserList<out T : User>(open val accessor: XUserList) : AbstractList<T>(), RandomAccess {

    override val size get() = accessor.size0

    val capacity get() = accessor.capacity

    val isFull get() = accessor.isFull

    operator fun contains(username: Username) = accessor.contains(username.accessor)
}