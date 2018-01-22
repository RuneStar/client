package org.runestar.client.game.api.live

import org.runestar.client.game.api.Ignored
import org.runestar.client.game.raw.Client.accessor

object IgnoreList : AbstractList<Ignored>(), RandomAccess {

    override val size get() = accessor.ignoreListCount

    val capacity = accessor.ignoreList.size

    override fun get(index: Int) = Ignored(checkNotNull(accessor.ignoreList[index]))
}