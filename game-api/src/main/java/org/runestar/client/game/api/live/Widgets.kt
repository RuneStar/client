package org.runestar.client.game.api.live

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetParentId
import org.runestar.client.game.raw.Client

object Widgets {

    val flat: List<Widget> get() = WidgetGroups.all.flatMap { it.flat }

    fun get(): List<List<Widget.Parent>> = WidgetGroups.get().map { it?.all ?: emptyList() }

    operator fun get(id: WidgetParentId): Widget.Parent? =
            Client.accessor.widgets.getOrNull(id.group)?.getOrNull(id.parent)?.let { Widget.Parent(it) }

    val roots: List<Widget.Parent> get() = WidgetGroups.root?.roots ?: emptyList()
}