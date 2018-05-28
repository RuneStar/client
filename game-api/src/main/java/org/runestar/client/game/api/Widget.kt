package org.runestar.client.game.api

import org.runestar.client.game.api.live.WidgetChain
import org.runestar.client.game.api.live.WidgetGroups
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XWidget
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle
import java.util.*

sealed class Widget(override val accessor: XWidget) : Wrapper(accessor) {

    val group get() = checkNotNull(WidgetGroups[parentId.group])

    val parentId get() = WidgetParentId(accessor.id)

    abstract val ancestor: Widget.Parent?

    val text: String? get() = accessor.text

    val textColor get() = accessor.textColor

    val width get() = accessor.width

    val height get() = accessor.height

    val isHidden get() = accessor.isHidden

    val isVisible get() = !isHidden || cycle >= Client.accessor.cycle

    val cycle get() = accessor.cycle

    val location: Point? get() {
        if (!isVisible) return null
        var cur = this
        var anc = ancestor
        var x = 0
        var y = 0
        while (anc != null) {
            x += cur.accessor.x - cur.accessor.scrollX
            y += cur.accessor.y - cur.accessor.scrollY
            cur = anc
            anc = anc.ancestor
        }
        x += Client.accessor.widgetXs.getOrElse(cur.accessor.index, { i -> 0 })
        y += Client.accessor.widgetYs.getOrElse(cur.accessor.index, { i -> 0 })
        return Point(x, y)
    }

    val dimension: Dimension get() = Dimension(width, height)

    val shape: Rectangle? get() = location?.let { Rectangle(it, dimension) }

    abstract fun idString(): String

    class Child(override val accessor: XWidget) : Widget(accessor) {

        init {
            require(accessor.childIndex != -1)
        }

        val childId get() = accessor.childIndex

        val parent get() = Widgets[parentId]

        override val ancestor get() = parent

        override fun toString(): String {
            return "Widget.Child(group=${group.id}, parent=${parentId.parent}, child=$childId)"
        }

        override fun idString(): String {
            return "${group.id}.${parentId.parent}:$childId"
        }
    }

    class Parent(override val accessor: XWidget) : Widget(accessor) {

        init {
            require(accessor.childIndex == -1)
        }

        val flat: Iterable<Widget> get() {
            return sequenceOf(this).plus(children.asSequence().filterNotNull()).asIterable()
        }

        val children: List<Widget.Child?> get() = object : AbstractList<Widget.Child?>(), RandomAccess {

            override val size: Int get() = accessor.children?.size ?: 0

            override fun get(index: Int): Child? {
                val array = accessor.children ?: return null
                return array[index]?.let { Widget.Child(it) }
            }
        }

        val successors: Iterable<Widget.Parent> get() = group.asSequence().filter { it.predecessor == this }.asIterable()

        val descendantsGroup: WidgetGroup? get() = WidgetChain[this]?.let { WidgetGroups[it] }

        val descendants: Iterable<Widget.Parent> get() = descendantsGroup?.roots ?: emptyList()

        val predecessor get() = accessor.parentId.takeIf { it != -1 }?.let { Widgets[WidgetParentId(it)] }

        override val ancestor get() = predecessor ?: group.parent

        fun getItem(slot: Int): WidgetItem? {
            return getItem(slot, location)
        }

        private fun getItem(slot: Int, location: Point?): WidgetItem? {
            if (location == null) return null
            if (accessor.type != 2) return null
            val id = accessor.itemIds.getOrNull(slot) ?: return null
            val quantity = accessor.itemQuantities.getOrNull(slot) ?: return null
            val item = Item.of(id, quantity)
            val row = slot / width
            val col = slot % width
            val x = location.x + ((ITEM_SLOT_SIZE + accessor.paddingX) * col)
            val y = location.y + ((ITEM_SLOT_SIZE + accessor.paddingY) * row)
            val rect = Rectangle(x, y, ITEM_SLOT_SIZE, ITEM_SLOT_SIZE)
            return WidgetItem(item, rect)
        }

        val items: List<WidgetItem?>? get() {
            if (accessor.type != 2) return null
            val loc = location
            return List(accessor.itemIds.size) { getItem(it, loc) }
        }

        override fun toString(): String {
            return "Widget.Parent(group=${group.id}, parent=${parentId.parent})"
        }

        override fun idString(): String {
            return "${group.id}.${parentId.parent}"
        }

        private companion object {
            const val ITEM_SLOT_SIZE = 32
        }
    }
}
