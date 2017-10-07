package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XNpc
import com.runesuite.client.game.raw.access.XNpcDefinition

class Npc(override val accessor: XNpc) : com.runesuite.client.game.api.Actor(accessor) {

    val definition: com.runesuite.client.game.api.Npc.Definition get() = com.runesuite.client.game.api.Npc.Definition(accessor.definition)

    class Definition(override val accessor: XNpcDefinition) : Wrapper() {

        val actions: List<String> get() = accessor.actions.map { it ?: "" }
    }
}