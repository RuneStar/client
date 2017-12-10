package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Widget
import com.runesuite.client.game.api.WidgetId
import com.runesuite.client.game.raw.Client

object Widgets {

    val flat: List<Widget> get() {
        return WidgetGroups.all.flatMap { it.flat }
    }

    fun get(): List<List<Widget.Parent>?> {
        return WidgetGroups.get().map { it?.all }
    }

    operator fun get(id: WidgetId): Widget.Parent? {
        return Client.accessor.widgets[id.group]?.get(id.parent)?.let { Widget.Parent(it) }
    }

    val roots: List<Widget.Parent> get() = WidgetGroups.root?.roots ?: emptyList()
}