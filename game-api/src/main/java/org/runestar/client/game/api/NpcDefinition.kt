package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNpcDefinition

class NpcDefinition(val accessor: XNpcDefinition) {

    val actions: List<String> get() = accessor.actions.map { it ?: "" }

    val name get() = accessor.name ?: ""

    val headIconPrayer get() = HeadIconPrayer.of(accessor.headIconPrayer)
}