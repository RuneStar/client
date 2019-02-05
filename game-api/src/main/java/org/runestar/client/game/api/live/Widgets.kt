package org.runestar.client.game.api.live

import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetGroup
import org.runestar.client.game.api.WidgetId
import org.runestar.client.game.raw.CLIENT

object Widgets {

    val flat: Sequence<Widget> get() = WidgetGroups.asSequence().filterNotNull().flatMap(WidgetGroup::flat)

    operator fun get(id: WidgetId): Widget? = get(id.group, id.index)

    inline fun <reified T : Widget> getAs(id: WidgetId): T? = Widgets[id] as? T?

    operator fun get(group: Int, index: Int): Widget? = CLIENT.widgets.getOrNull(group)?.getOrNull(index)?.let { Widget.of(it) }

    val roots: Sequence<Widget> get() = WidgetGroups.root?.roots ?: emptySequence()
}