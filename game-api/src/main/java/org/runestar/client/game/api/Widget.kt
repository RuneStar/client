package org.runestar.client.game.api

import org.runestar.client.game.api.live.WidgetChain
import org.runestar.client.game.api.live.WidgetGroups
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XWidget
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import java.awt.Rectangle
import java.util.*

sealed class Widget(override val accessor: XWidget) : Wrapper(accessor) {

    companion object {
        const val ITEM_SLOT_SIZE = 32
    }

    val group get() = checkNotNull(WidgetGroups[parentId.group])

    val parentId get() = WidgetParentId(accessor.id)

    abstract val ancestor: Widget.Parent?

    val text: String? get() = accessor.text

    /**
     * Color of [text]. Default is [Color.BLACK]
     */
    val textColor: Color get() = Color(accessor.textColor)

    val width: Int get() {
        return when (accessor.type) {
            2 -> accessor.width * (ITEM_SLOT_SIZE + accessor.paddingX) - accessor.paddingX
            else -> accessor.width
        }
    }

    val height: Int get() {
        return when (accessor.type) {
            2 -> accessor.height * (ITEM_SLOT_SIZE + accessor.paddingY) - accessor.paddingY
            else -> accessor.height
        }
    }

    val isHidden get() = accessor.isHidden

    val isActive get() = cycle >= Client.accessor.cycle

    val cycle get() = accessor.cycle

    val location: Point? get() {
        var cur = this
        var anc = ancestor
        var x = 0
        var y = 0
        while(anc != null) {
            x += cur.accessor.x
            y += cur.accessor.y
            if (accessor.scrollMax == 0) {
                x -= cur.accessor.scrollX
                y -= cur.accessor.scrollY
            }
            cur = anc
            anc = anc.ancestor
        }
        if (cur.group != WidgetGroups.root) return null
        x += Client.accessor.rootWidgetXs[cur.accessor.rootIndex]
        y += Client.accessor.rootWidgetYs[cur.accessor.rootIndex]
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

        val flat: Sequence<Widget> get() {
            return sequenceOf(this).plus(children.asSequence().filterNotNull())
        }

        val children: List<Widget.Child?> get() = object : AbstractList<Widget.Child?>(), RandomAccess {

            override val size: Int get() = accessor.children?.size ?: 0

            override fun get(index: Int): Child? {
                val array = accessor.children ?: return null
                return array[index]?.let { Widget.Child(it) }
            }
        }

        val successors: Sequence<Widget.Parent> get() = group.asSequence().filter { it.predecessor == this }

        val descendantsGroup: WidgetGroup? get() = WidgetChain[this]?.let { WidgetGroups[it] }

        val descendants: Sequence<Widget.Parent> get() = descendantsGroup?.roots ?: emptySequence()

        val predecessor get() = accessor.parentId.takeIf { it != -1 }?.let { Widgets[WidgetParentId(it)] }

        override val ancestor get() = predecessor ?: group.parent

        val inventory: List<WidgetItem> get() = object : AbstractList<WidgetItem>(), RandomAccess {

            init {
                check(accessor.type == 2)
            }

            private val location = checkNotNull(this@Parent.location)

            override val size: Int get() = accessor.width * accessor.height

            override fun get(index: Int): WidgetItem {
                val id = accessor.itemIds[index]
                val quantity = accessor.itemQuantities[index]
                val item = Item.of(id, quantity)
                val row = index / accessor.width
                val col = index % accessor.width
                val x = location.x + ((ITEM_SLOT_SIZE + accessor.paddingX) * col)
                val y = location.y + ((ITEM_SLOT_SIZE + accessor.paddingY) * row)
                val rect = Rectangle(x, y, ITEM_SLOT_SIZE, ITEM_SLOT_SIZE)
                return WidgetItem(item, rect)
            }
        }

        override fun toString(): String {
            return "Widget.Parent(group=${group.id}, parent=${parentId.parent})"
        }

        override fun idString(): String {
            return "${group.id}.${parentId.parent}"
        }
    }
}