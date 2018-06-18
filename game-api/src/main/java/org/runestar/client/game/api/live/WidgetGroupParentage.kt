package org.runestar.client.game.api.live

import org.runestar.client.game.api.NodeHashTable
import org.runestar.client.game.api.WidgetParentId
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XNode
import org.runestar.client.game.raw.access.XNodeHashTable
import org.runestar.client.game.raw.access.XWidgetGroupParent

object WidgetGroupParentage : NodeHashTable<WidgetParentId, Int>() {

    override val accessor: XNodeHashTable get() = Client.accessor.widgetGroupParents

    override fun wrapKey(node: XNode): WidgetParentId {
        return WidgetParentId(node.key.toInt())
    }

    override fun unwrapKey(k: WidgetParentId): Long {
        return k.packed.toLong()
    }

    override fun wrapValue(node: XNode): Int {
        return (node as XWidgetGroupParent).group
    }
}