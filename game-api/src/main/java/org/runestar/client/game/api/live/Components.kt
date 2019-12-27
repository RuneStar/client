package org.runestar.client.game.api.live

import org.runestar.client.cacheids.ScriptId
import org.runestar.client.game.api.Component
import org.runestar.client.game.api.Interface
import org.runestar.client.game.api.ComponentId
import org.runestar.client.game.api.EnumType
import org.runestar.client.game.raw.CLIENT

object Components {

    val flat: Sequence<Component> get() = Interfaces.asSequence().filterNotNull().flatMap(Interface::flat)

    operator fun get(id: ComponentId): Component? = get(id.itf, id.component)

    operator fun get(itf: Int, component: Int): Component? = CLIENT.interfaceComponents.getOrNull(itf)?.getOrNull(component)?.let { Component(it) }

    val roots: Sequence<Component> get() = Interfaces.root?.roots ?: emptySequence()

    val dragInventory: Component? get() = CLIENT.dragInventoryComponent?.let { Component(it) }

    fun align(c: Component) = CLIENT.alignComponent(c.accessor)

    val topLevelComponents: EnumType get() {
        val e = CLIENT._ClientScriptEvent_()
        e.setArgs(arrayOf(ScriptId.PROC_TOPLEVEL_GETCOMPONENTS))
        CLIENT.runClientScript(e)
        return EnumType(CLIENT.getEnumType(CLIENT.interpreter_intStack[0]))
    }

    fun topLevel(topLevelComponentId: ComponentId): Component? = get(ComponentId(topLevelComponents.getInt(topLevelComponentId.packed)))
}