package org.runestar.client.game.api.live

import org.runestar.client.game.api.Interface
import org.runestar.client.game.raw.CLIENT

object Interfaces : AbstractList<Interface?>(), RandomAccess {

    override val size: Int get() = CLIENT.interfaceComponents.size

    override fun get(index: Int): Interface? = CLIENT.interfaceComponents.getOrNull(index)?.let { Interface(index, it) }

    val rootId: Int get() = CLIENT.rootInterface

    val root: Interface? get() = get(rootId)
}