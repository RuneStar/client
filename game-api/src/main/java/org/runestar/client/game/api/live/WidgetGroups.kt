package org.runestar.client.game.api.live

import org.runestar.client.game.api.WidgetGroup
import org.runestar.client.game.raw.Client

object WidgetGroups : AbstractList<WidgetGroup?>(), RandomAccess {

    override val size: Int get() = Client.accessor.widgets.size

    override fun get(index: Int): WidgetGroup? {
        return Client.accessor.widgets[index]?.let { WidgetGroup(index, it) }
    }

    val root: WidgetGroup? get() = get(Client.accessor.rootWidgetGroup)
}