package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XNpcDefinition

class NpcDefinition(val accessor: XNpcDefinition) {

    val id get() = accessor.id

    val actions: List<String> get() = accessor.actions.map { it ?: "" }

    val name: String? get() = accessor.name.takeUnless { it == "null" }

    val headIconPrayer get() = HeadIconPrayer.of(accessor.headIconPrayer)
}