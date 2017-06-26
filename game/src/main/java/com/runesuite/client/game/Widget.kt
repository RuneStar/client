package com.runesuite.client.game

import com.runesuite.client.base.Client
import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XWidget
import com.runesuite.client.base.access.XWidgetNode
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle

class Widget(override val accessor: XWidget) : Wrapper() {

    val children: List<Widget> get() = accessor.children?.copyOf()?.map { Widget(it) } ?: emptyList()

    val childrenSize get() = accessor.children?.size ?: 0

    val all: Sequence<Widget> get() {
        return sequenceOf(this) + children.asSequence()
    }

    operator fun get(childId: Int) : Widget? = accessor.children?.get(childId)?.let { Widget(it) }

    val group get() = WidgetGroup(accessor.id shr 16)

    val id get() = accessor.id and 0xFFFF

    val parent: Widget? get() {
        var pId = accessor.parentId
        if (pId == -1) {
            val groupId = group.id
            val table = Client.accessor.widgetNodes
            var node = table.first() as XWidgetNode?
            while (node != null && node.id != groupId) {
                node = table.next() as XWidgetNode?
            }
            node ?: return null
            pId = node.uid.toInt()
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