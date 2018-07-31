package org.runestar.client.game.api.live

import org.runestar.client.game.api.NodeHashTable
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetId
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XNodeHashTable
import org.runestar.client.game.raw.access.XWidgetGroupParent

object WidgetGroupParents : NodeHashTable<WidgetId, Int, XWidgetGroupParent>() {

    override val accessor: XNodeHashTable get() = CLIENT.widgetGroupParents

    override fun wrapKey(node: XWidgetGroupParent): WidgetId {
        return WidgetId(node.key.toInt())
    }

    override fun unwrapKey(k: WidgetId): Long {
        return k.packed.toLong()
    }

    override fun wrapValue(node: XWidgetGroupParent): Int {
        return node.group
    }

    fun parentId(group: Int): Int {
        val node = nodes.firstOrNull { it.group == group } ?: return -1
        return node.key.toInt()
    }

    fun parent(group: Int): Widget.Layer? {
        val pid = parentId(group)
        if (pid == -1) return null
        return Widgets[WidgetId.getGroup(pid), WidgetId.getIndex(pid)] as Widget.Layer?
    }
}