package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XWidget
import com.runesuite.client.game.raw.access.XWidgetNode
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

class Widget(override val accessor: XWidget) : Wrapper() {

    val children: List<com.runesuite.client.game.api.Widget> get() = accessor.children?.map { com.runesuite.client.game.api.Widget(it) } ?: emptyList()

    val childrenSize get() = accessor.children?.size ?: 0

    val flat: List<com.runesuite.client.game.api.Widget> get() {
        return listOf(this) + children
    }

    operator fun get(childId: Int) : com.runesuite.client.game.api.Widget? = accessor.children?.get(childId)?.let { com.runesuite.client.game.api.Widget(it) }

    val group get() = com.runesuite.client.game.api.WidgetGroup(accessor.id shr 16)

    val id get() = accessor.id and 0xFFFF

    val parent: Widget? get() {
        var pId = accessor.parentId
        if (pId == -1) {
            // todo
            val groupId = group.id
            val table = Client.accessor.widgetNodes
            var node = table.first() as XWidgetNode?
            while (node != null && node.id != groupId) {
                node = table.next() as XWidgetNode?
            }
            node ?: return null
            pId = node.key.toInt()
        }
        return Widget(Client.accessor.widgets[pId shr 16]!![pId and 0xFFFF])
    }

    val text: String? get() = accessor.text

    val textColor get() = accessor.textColor

    val width get() = accessor.width

    val height get() = accessor.height

    val isHidden get() = accessor.isHidden

    val isVisible get() = cycle >= Client.accessor.cycle

    val cycle get() = accessor.cycle

    val location: Point? get() {
        if (!isVisible) return null
        val p = parent
        return when (p) {
            null -> Point(Client.accessor.widgetXs[accessor.index], Client.accessor.widgetYs[accessor.index])
            else -> p.location!!.let { Point(it.x + accessor.x - accessor.scrollX, it.y + accessor.y - accessor.scrollY) }
        }
    }

    val dimension: Dimension get() = Dimension(width, height)

    val shape: Rectangle? get() {
       return location?.let { Rectangle(it, dimension) }
    }
}