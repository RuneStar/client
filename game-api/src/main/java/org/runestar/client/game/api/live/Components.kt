package org.runestar.client.game.api.live

import org.runestar.client.game.api.Component
import org.runestar.client.game.api.Interface
import org.runestar.client.game.api.ComponentId
import org.runestar.client.game.raw.CLIENT

object Components {

    val flat: Sequence<Component> get() = Interfaces.asSequence().filterNotNull().flatMap(Interface::flat)

    operator fun get(id: ComponentId): Component? = get(id.itf, id.component)

    operator fun get(itf: Int, component: Int): Component? = CLIENT.interfaceComponents.getOrNull(itf)?.getOrNull(component)?.let { Component(it) }

    val roots: Sequence<Component> get() = Interfaces.root?.roots ?: emptySequence()

    val dragInventory: Component? get() = CLIENT.dragInventoryComponent?.let { Component(it) }

    fun align(c: Component) = CLIENT.alignComponent(c.accessor)
}