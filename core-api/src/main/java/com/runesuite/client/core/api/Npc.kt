package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Wrapper
import com.runesuite.client.core.raw.access.XNpc
import com.runesuite.client.core.raw.access.XNpcDefinition

class Npc(override val accessor: XNpc) : Actor(accessor) {

    val definition: Npc.Definition get() = Npc.Definition(accessor.definition)

    class Definition(override val accessor: XNpcDefinition) : Wrapper() {

        val actions: List<String> get() = accessor.actions.map { it ?: "" }
    }
}