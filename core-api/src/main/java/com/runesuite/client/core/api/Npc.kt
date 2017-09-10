package com.runesuite.client.core.api

import com.runesuite.client.core.raw.Wrapper
import com.runesuite.client.core.raw.access.XNpc
import com.runesuite.client.core.raw.access.XNpcDefinition

class Npc(override val accessor: XNpc) : com.runesuite.client.core.api.Actor(accessor) {

    val definition: com.runesuite.client.core.api.Npc.Definition get() = com.runesuite.client.core.api.Npc.Definition(accessor.definition)

    class Definition(override val accessor: XNpcDefinition) : Wrapper() {

        val actions: List<String> get() = accessor.actions.map { it ?: "" }
    }
}