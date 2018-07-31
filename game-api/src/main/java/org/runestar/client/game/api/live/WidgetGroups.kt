package org.runestar.client.game.api.live

import org.runestar.client.game.api.WidgetGroup
import org.runestar.client.game.raw.CLIENT

object WidgetGroups : AbstractList<WidgetGroup?>(), RandomAccess {

    override val size: Int get() = CLIENT.widgets.size

    override fun get(index: Int): WidgetGroup? = CLIENT.widgets.getOrNull(index)?.let { WidgetGroup(index, it) }

    val rootId: Int get() = CLIENT.rootWidgetGroup

    val root: WidgetGroup? get() = get(rootId)
}