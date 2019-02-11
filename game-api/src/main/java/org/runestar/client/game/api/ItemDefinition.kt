package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XItemDefinition

inline class ItemDefinition(val accessor: XItemDefinition) {

    val id: Int get() = accessor.id
}