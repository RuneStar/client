package org.runestar.client.game.api.live

import org.runestar.client.game.api.NodeHashTable
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetParentId
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeHashTable
import org.runestar.client.game.raw.access.XWidgetNode

object WidgetChain : NodeHashTable<Widget.Parent, Int>() {

    override val accessor: XNodeHashTable get() = Client.accessor.widgetNodes

    override fun wrapKey(node: XNode): Widget.Parent {
        return checkNotNull(Widgets[WidgetParentId(node.key.toInt())])
    }

    override fun unwrapKey(k: Widget.Parent): Long {
        return k.parentId.packed.toLong()
    }

    override fun wrapValue(node: XNode): Int {
        return (node as XWidgetNode).id
    }
}