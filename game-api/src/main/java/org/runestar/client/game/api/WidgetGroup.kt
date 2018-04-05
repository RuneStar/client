package org.runestar.client.game.api

import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XWidget
import org.runestar.client.game.raw.access.XWidgetNode

class WidgetGroup(
        val id: Int,
        val accessor: Array<XWidget>
) : AbstractList<Widget.Parent>(), RandomAccess {

    val parent: Widget.Parent? get() {
        val table = Client.accessor.widgetNodes // todo
        var node = table.first() as XWidgetNode?
        while (node != null) {
            if (node.id == id) {
                return Widgets[WidgetParentId(node.key.toInt())]
            }
            node = table.next() as XWidgetNode?
        }
        return null
    }

    val roots: Iterable<Widget.Parent> get() = asSequence().filter { it.predecessor == null }.asIterable()

    override val size get() = accessor.size

    val flat: Iterable<Widget> get() = asSequence().flatMap { it.flat.asSequence() }.asIterable()

    override fun get(index: Int): Widget.Parent = accessor[index].let { Widget.Parent(it) }
}