package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XUser
import org.runestar.client.game.raw.access.XUserList

abstract class UserList<out T : User>(open val accessor: XUserList) : AbstractList<T?>(), RandomAccess {

    override val size get() = accessor.size0

    val capacity get() = accessor.capacity

    val isFull get() = accessor.isFull

    override fun get(index: Int): T? {
        require(index in 0 until capacity)
        if (index >= size) return null
        val x = accessor.array[index] ?: return null
        return wrap(x)
    }

    operator fun get(username: Username): T? {
        val x = accessor.getByUsername(username.accessor) ?: return null
        return wrap(x)
    }

    operator fun contains(username: Username) = accessor.contains(username.accessor)

    protected abstract fun wrap(user: XUser) : T
}