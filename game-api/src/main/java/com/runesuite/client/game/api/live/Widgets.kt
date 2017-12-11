package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Widget
import com.runesuite.client.game.api.WidgetId
import com.runesuite.client.game.raw.Client

object Widgets {

    val flat: List<Widget> get() = WidgetGroups.all.flatMap { it.flat }

    fun get(): List<List<Widget.Parent>> = WidgetGroups.get().map { it?.all ?: emptyList() }

    operator fun get(id: WidgetId): Widget.Parent? =
            Client.accessor.widgets.getOrNull(id.group)?.getOrNull(id.parent)?.let { Widget.Parent(it) }

    val roots: List<Widget.Parent> get() = WidgetGroups.root?.roots ?: emptyList()
}