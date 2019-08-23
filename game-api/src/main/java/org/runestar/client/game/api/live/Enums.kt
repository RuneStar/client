package org.runestar.client.game.api.live

import org.runestar.client.game.api.EnumType
import org.runestar.client.game.raw.CLIENT

object Enums {

    operator fun get(id: Int): EnumType = EnumType(CLIENT.getEnumType(id))
}