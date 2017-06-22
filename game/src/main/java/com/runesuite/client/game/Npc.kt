package com.runesuite.client.game

import com.runesuite.client.base.Wrapper
import com.runesuite.client.base.access.XNpc
import com.runesuite.client.base.access.XNpcDefinition

class Npc(override val accessor: XNpc) : Actor(accessor) {

    val definition: Definition get() = Definition(accessor.definition)

    class Definition(override val accessor: XNpcDefinition) : Wrapper() {

        val actions: List<String> get() = accessor.actions.map { it ?: "" }
    }
}