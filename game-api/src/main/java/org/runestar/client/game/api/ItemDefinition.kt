package org.runestar.client.game.api

import org.runestar.client.game.raw.access.XObjType

inline class ItemDefinition(val accessor: XObjType) {

    val id: Int get() = accessor.id
}