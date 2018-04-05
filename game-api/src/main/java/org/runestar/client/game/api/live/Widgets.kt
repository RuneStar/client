package org.runestar.client.game.api.live

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetParentId
import org.runestar.client.game.raw.Client

object Widgets {

    val flat: Iterable<Widget> get() {
        return WidgetGroups.asSequence().filterNotNull().flatMap { it.flat.asSequence() }.asIterable()
    }

    operator fun get(id: WidgetParentId): Widget.Parent? =
            Client.accessor.widgets.getOrNull(id.group)?.getOrNull(id.parent)?.let { Widget.Parent(it) }

    val roots: Iterable<Widget.Parent> get() = WidgetGroups.root?.roots ?: emptyList()
}