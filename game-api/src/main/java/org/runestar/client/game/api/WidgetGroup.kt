package org.runestar.client.game.api

import org.runestar.client.game.api.live.WidgetGroupParentage
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.access.XWidget

class WidgetGroup(
        val id: Int,
        val accessor: Array<XWidget>
) : AbstractList<Widget.Parent>(), RandomAccess {

    val parent: Widget.Parent? get() {
        val entry = WidgetGroupParentage.entries.firstOrNull { it.value == id } ?: return null
        return Widgets[entry.key]
    }

    val roots: Sequence<Widget.Parent> get() = asSequence().filter { it.predecessor == null }

    override val size get() = accessor.size

    val flat: Sequence<Widget> get() = asSequence().flatMap { it.flat.asSequence() }

    override fun get(index: Int): Widget.Parent = accessor[index].let { Widget.Parent(it) }
}