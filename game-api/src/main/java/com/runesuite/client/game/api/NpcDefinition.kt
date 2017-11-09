package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XNpcDefinition

class NpcDefinition(override val accessor: XNpcDefinition) : Wrapper() {

    val actions: List<String> get() = accessor.actions.map { it ?: "" }
}