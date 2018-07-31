package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XWidget

class WidgetGroup(
        val id: Int,
        val accessor: Array<XWidget>
) : AbstractList<Widget>(), RandomAccess {

    val roots: Sequence<Widget> get() = asSequence().filter { !it.hasStaticParent }

    override val size get() = accessor.size

    val flat: Sequence<Widget> get() = asSequence().flatMap(Widget::flat)

    override fun get(index: Int): Widget = Widget.of(accessor[index])

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is WidgetGroup && id == other.id

    override fun toString(): String = "WidgetGroup($id)"
}