package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XComponent

class Interface(
        val id: Int,
        val accessor: Array<XComponent>
) : AbstractList<Component>(), RandomAccess {

    val roots: Sequence<Component> get() = asSequence().filter { !it.hasStaticParent }

    override val size get() = accessor.size

    val flat: Sequence<Component> get() = asSequence().flatMap(Component::flat)

    override fun get(index: Int): Component = Component(accessor[index])

    override fun hashCode(): Int = id.hashCode()

    override fun equals(other: Any?): Boolean = other is Interface && id == other.id

    override fun toString(): String = "Interface($id)"
}