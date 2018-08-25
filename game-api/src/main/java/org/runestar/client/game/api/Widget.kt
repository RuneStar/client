package org.runestar.client.game.api

import org.runestar.client.game.api.live.WidgetGroupParents
import org.runestar.client.game.api.live.WidgetGroups
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XWidget
import java.awt.Color
import java.awt.Dimension
import java.awt.Point

sealed class Widget(override val accessor: XWidget) : Wrapper(accessor) {

    val type: Int get() = accessor.type

    val cycle: Int get() = accessor.cycle

    val isActive get() = cycle >= CLIENT.cycle

    val isHidden: Boolean get() = accessor.isHidden

    val id get() = WidgetId(accessor.id)

    val index: Int get() = WidgetId.getIndex(accessor.id)

    val group get() = checkNotNull(WidgetGroups[groupId])

    val groupId: Int get() = WidgetId.getGroup(accessor.id)

    val hasStaticParent: Boolean get() = accessor.parentId != -1

    val staticParent: Widget.Layer? get() = staticParent(accessor)?.let { of(it) as Widget.Layer }

    val parent: Widget.Layer? get() = parent(accessor)?.let { of(it) as Widget.Layer }

    val dynamicChildIndex: Int get() = accessor.childIndex

    val isDynamicChild: Boolean get() = dynamicChildIndex != -1

    open val width: Int get() = accessor.width

    open val height: Int get() = accessor.height

    val dimension: Dimension get() = Dimension(width, height)

    val shape: java.awt.Rectangle? get() = location?.let { java.awt.Rectangle(it.x, it.y, width, height) }

    internal open val flat: Sequence<Widget> get() = sequenceOf(this)

    val location: Point? get() {
        var cur = accessor
        var anc = parent(cur)
        var x = 0
        var y = 0
        while(anc != null) {
            x += cur.x - anc.scrollX
            y += cur.y - anc.scrollY
            cur = anc
            anc = parent(anc)
        }
        if (WidgetId.getGroup(cur.id) != WidgetGroups.rootId) return null
        if (cur.rootIndex == -1) return null
        x += CLIENT.rootWidgetXs[cur.rootIndex]
        y += CLIENT.rootWidgetYs[cur.rootIndex]
        return Point(x, y)
    }

    fun idString(): String {
        return if (isDynamicChild) {
            "$groupId:$index:$dynamicChildIndex"
        } else {
            "$groupId:$index"
        }
    }

    override fun toString(): String {
        return "${javaClass.simpleName}(${idString()})"
    }

    class Layer internal constructor(accessor: XWidget) : Widget(accessor) {

        val scrollX: Int get() = accessor.scrollX

        val scrollY: Int get() = accessor.scrollY

        val scrollWidth: Int get() = accessor.scrollWidth

        val scrollHeight: Int get() = accessor.scrollHeight

        val dynamicChildren: List<Widget?> get() = object : AbstractList<Widget?>(), RandomAccess {

            override val size: Int get() = accessor.children?.size ?: 0

            override fun get(index: Int): Widget? {
                val array = accessor.children ?: return null
                return array[index]?.let { of(it) }
            }
        }

        override val flat: Sequence<Widget> get() = sequenceOf(this).plus(dynamicChildren.asSequence().filterNotNull())

        val staticChildren: Sequence<Widget> get() = group.asSequence().filter { it.accessor.parentId == accessor.id }

        val nestedGroup: WidgetGroup? get() = WidgetGroupParents[id]?.let { WidgetGroups[it] }

        val nestedChildren: Sequence<Widget> get() = nestedGroup?.roots ?: emptySequence()

        val children: Sequence<Widget> get() = sequenceOf(dynamicChildren.asSequence().filterNotNull(), staticChildren, nestedChildren).flatten()
    }

    class Inventory internal constructor(accessor: XWidget) : Widget(accessor) {

        companion object {
            const val ITEM_SLOT_SIZE = 32
        }

        override val width: Int get() = accessor.width * (ITEM_SLOT_SIZE + accessor.paddingX) - accessor.paddingX

        override val height: Int get() = accessor.height * (ITEM_SLOT_SIZE + accessor.paddingY) - accessor.paddingY

        val items: List<WidgetItem> get() = object : AbstractList<WidgetItem>(), RandomAccess {

            private val location = checkNotNull(this@Inventory.location)

            override val size: Int get() = accessor.width * accessor.height

            override fun get(index: Int): WidgetItem {
                val id = accessor.itemIds[index]
                val quantity = accessor.itemQuantities[index]
                val item = Item.of(id, quantity)
                val row = index / accessor.width
                val col = index % accessor.width
                val x = location.x + ((ITEM_SLOT_SIZE + accessor.paddingX) * col)
                val y = location.y + ((ITEM_SLOT_SIZE + accessor.paddingY) * row)
                val rect = java.awt.Rectangle(x, y, ITEM_SLOT_SIZE, ITEM_SLOT_SIZE)
                return WidgetItem(item, rect)
            }
        }
    }

    class Rectangle internal constructor(accessor: XWidget) : Widget(accessor) {

        val color: Color get() = Color(accessor.color)
    }

    class Text internal constructor(accessor: XWidget) : Widget(accessor) {

        val fontId: Int get() = accessor.fontId

        var text: String?
            get() = accessor.text
            set(value) { accessor.text = value }

        val color: Color get() = Color(accessor.color)

        val textXAlignment: Int get() = accessor.textXAlignment

        val textYAlignment: Int get() = accessor.textYAlignment

        val isTextShadowed: Boolean get() = accessor.textShadowed

        val textLineHeight: Int get() = accessor.textLineHeight
    }

    class Sprite internal constructor(accessor: XWidget) : Widget(accessor) {

    }

    class Model internal constructor(accessor: XWidget) : Widget(accessor) {


        val modelType: Int get() = accessor.modelType

        val modelId: Int get() = accessor.modelId
    }

    class Line internal constructor(accessor: XWidget) : Widget(accessor) {

        val color: Color get() = Color(accessor.color)
    }

    companion object {

        fun of(accessor: XWidget): Widget {
            return when (accessor.type) {
                WidgetType.LAYER -> Widget.Layer(accessor)
                WidgetType.INVENTORY -> Widget.Inventory(accessor)
                WidgetType.RECTANGLE -> Widget.Rectangle(accessor)
                WidgetType.TEXT -> Widget.Text(accessor)
                WidgetType.SPRITE -> Widget.Sprite(accessor)
                WidgetType.MODEL -> Widget.Model(accessor)
                WidgetType.LINE -> Widget.Line(accessor)
                else -> error("type: ${accessor.type} id: ${WidgetId.getGroup(accessor.id)}:${WidgetId.getIndex(accessor.id)}:${accessor.childIndex}")
            }
        }

        private fun parent(widget: XWidget): XWidget? {
            val staticParent = staticParent(widget)
            if (staticParent != null) return staticParent
            if (WidgetId.getGroup(widget.id) == WidgetGroups.rootId) return null
            val nestedParentId = WidgetGroupParents.parentId(WidgetId.getGroup(widget.id))
            if (nestedParentId == -1) return null
            return CLIENT.widgets[WidgetId.getGroup(nestedParentId)][WidgetId.getIndex(nestedParentId)]
        }

        private fun staticParent(widget: XWidget): XWidget? {
            val pid = widget.parentId
            if (pid == -1) return null
            return CLIENT.widgets[WidgetId.getGroup(pid)][WidgetId.getIndex(pid)]
        }
    }
}