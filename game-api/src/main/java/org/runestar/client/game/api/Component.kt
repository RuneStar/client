package org.runestar.client.game.api

import org.runestar.client.game.api.live.InterfaceParents
import org.runestar.client.game.api.live.Interfaces
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XComponent
import java.awt.Dimension
import java.awt.Point

class Component(override val accessor: XComponent) : Wrapper(accessor) {

    val type: Int get() = accessor.type

    val cycle: Int get() = accessor.cycle

    val isActive get() = cycle >= CLIENT.cycle

    var isHidden: Boolean
        get() = accessor.isHidden
        set(value) { accessor.isHidden = value }

    val id get() = ComponentId(accessor.id)

    val group get() = checkNotNull(Interfaces[id.itf])

    val hasStaticParent: Boolean get() = accessor.parentId != -1

    val staticParent: Component? get() = staticParent(accessor)?.let { Component(it) }

    val parent: Component? get() = parent(accessor)?.let { Component(it) }

    val dynamicChildIndex: Int get() = accessor.childIndex

    val isDynamicChild: Boolean get() = dynamicChildIndex != -1

    var color: RgbColor
        get() = RgbColor(accessor.color)
        set(value) { accessor.color = value.packed }

    var rawX: Int
        get() = accessor.rawX
        set(value) { accessor.rawX = value }

    var rawY: Int
        get() = accessor.rawY
        set(value) { accessor.rawY = value }

    val width: Int get() = when (type) {
        ComponentType.INVENTORY -> accessor.width * (ITEM_SLOT_SIZE + accessor.paddingX) - accessor.paddingX
        else -> accessor.width
    }

    val height: Int get() = when (type) {
        ComponentType.INVENTORY -> accessor.height * (ITEM_SLOT_SIZE + accessor.paddingY) - accessor.paddingY
        else -> accessor.height
    }

    val dimension: Dimension get() = Dimension(width, height)

    val shape: java.awt.Rectangle? get() = location?.let { java.awt.Rectangle(it.x, it.y, width, height) }

    val flat: Sequence<Component> get() = when(type) {
        ComponentType.LAYER -> sequenceOf(this).plus(dynamicChildren.asSequence().filterNotNull())
        else -> sequenceOf(this)
    }

    val dynamicChildren: List<Component?> get() = object : AbstractList<Component?>(), RandomAccess {

        override val size: Int get() = accessor.children?.size ?: 0

        override fun get(index: Int): Component? {
            val array = accessor.children ?: return null
            return array[index]?.let { Component(it) }
        }
    }

    val staticChildren: Sequence<Component> get() = group.asSequence().filter { it.accessor.parentId == accessor.id }

    val nestedGroup: Interface? get() = InterfaceParents[id]?.let { Interfaces[it] }

    val nestedChildren: Sequence<Component> get() = nestedGroup?.roots ?: emptySequence()

    val children: Sequence<Component> get() = sequenceOf(dynamicChildren.asSequence().filterNotNull(), staticChildren, nestedChildren).flatten()

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
        if (ComponentId.getItf(cur.id) != Interfaces.rootId) return null
        if (cur.rootIndex == -1) return null
        x += CLIENT.rootComponentXs[cur.rootIndex]
        y += CLIENT.rootComponentYs[cur.rootIndex]
        return Point(x, y)
    }

    val scrollX: Int get() = accessor.scrollX

    val scrollY: Int get() = accessor.scrollY

    val scrollWidth: Int get() = accessor.scrollWidth

    val scrollHeight: Int get() = accessor.scrollHeight

    val fontId: Int get() = accessor.fontId

    var text: String?
        get() = accessor.text
        set(value) { accessor.text = value }

    val textXAlignment: Int get() = accessor.textXAlignment

    val textYAlignment: Int get() = accessor.textYAlignment

    val isTextShadowed: Boolean get() = accessor.textShadowed

    val textLineHeight: Int get() = accessor.textLineHeight

    val modelType: Int get() = accessor.modelType

    val modelId: Int get() = accessor.modelId

    fun idString(): String {
        return if (isDynamicChild) {
            "${id.itf}:${id.component}:$dynamicChildIndex"
        } else {
            "${id.itf}:${id.component}"
        }
    }

    override fun toString(): String {
        return "Component(${idString()})"
    }

    val items: List<ComponentItem> get() = object : AbstractList<ComponentItem>(), RandomAccess {

        private val location = checkNotNull(this@Component.location)

        override val size: Int get() = accessor.width * accessor.height

        override fun get(index: Int): ComponentItem {
            val id = accessor.itemIds[index]
            val quantity = accessor.itemQuantities[index]
            val item = Item.of(id, quantity)
            val row = index / accessor.width
            val col = index % accessor.width
            val x = location.x + ((ITEM_SLOT_SIZE + accessor.paddingX) * col)
            val y = location.y + ((ITEM_SLOT_SIZE + accessor.paddingY) * row)
            val rect = java.awt.Rectangle(x, y, ITEM_SLOT_SIZE, ITEM_SLOT_SIZE)
            return ComponentItem(item, rect)
        }
    }

    companion object {

        const val ITEM_SLOT_SIZE = 32

        private fun parent(component: XComponent): XComponent? {
            val staticParent = staticParent(component)
            if (staticParent != null) return staticParent
            if (ComponentId.getItf(component.id) == Interfaces.rootId) return null
            val nestedParentId = InterfaceParents.parentId(ComponentId.getItf(component.id))
            if (nestedParentId == -1) return null
            return CLIENT.interfaceComponents[ComponentId.getItf(nestedParentId)][ComponentId.getComponent(nestedParentId)]
        }

        private fun staticParent(component: XComponent): XComponent? {
            val pid = component.parentId
            if (pid == -1) return null
            return CLIENT.interfaceComponents[ComponentId.getItf(pid)][ComponentId.getComponent(pid)]
        }
    }
}