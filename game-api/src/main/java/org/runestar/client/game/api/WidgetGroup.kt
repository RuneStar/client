package org.runestar.client.game.api

import org.runestar.client.game.api.live.WidgetChain
import org.runestar.client.game.raw.access.XWidget

class WidgetGroup(
        val id: Int,
        val accessor: Array<XWidget>
) : AbstractList<Widget.Parent>(), RandomAccess {

    val parent: Widget.Parent? get() {
        val entry = WidgetChain.entries.firstOrNull { it.value == id } ?: return null
        return entry.key
    }

    val roots: Iterable<Widget.Parent> get() = asSequence().filter { it.predecessor == null }.asIterable()

    override val size get() = accessor.size

    val flat: Iterable<Widget> get() = asSequence().flatMap { it.flat.asSequence() }.asIterable()

    override fun get(index: Int): Widget.Parent = accessor[index].let { Widget.Parent(it) }
}