package org.runestar.client.api.game

import org.runestar.client.raw.access.XObjType

inline class ItemDefinition(val accessor: XObjType) {

    val id: Int get() = accessor.id
}