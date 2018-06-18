package org.runestar.client.game.api.live

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetParentId
import org.runestar.client.game.raw.CLIENT

object Widgets {

    val flat: Sequence<Widget> get() {
        return WidgetGroups.asSequence().filterNotNull().flatMap { it.flat }
    }

    operator fun get(id: WidgetParentId): Widget.Parent? =
            CLIENT.widgets.getOrNull(id.group)?.getOrNull(id.parent)?.let { Widget.Parent(it) }

    val roots: Sequence<Widget.Parent> get() = WidgetGroups.root?.roots ?: emptySequence()
}