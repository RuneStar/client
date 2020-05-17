package org.runestar.client.api.game.live

import org.runestar.client.api.game.EnumType
import org.runestar.client.raw.CLIENT

object Enums {

    operator fun get(id: Int): EnumType = EnumType(CLIENT.getEnumType(id))
}