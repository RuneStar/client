package org.runestar.client.game.api

inline class WidgetId(val packed: Int) {

    val group: Int get() = getGroup(packed)

    val index: Int get() = getIndex(packed)

    companion object {

        fun of(group: Int, index: Int) = WidgetId((group shl 16) or index)

        fun getGroup(packed: Int) = packed shr 16

        fun getIndex(packed: Int) = packed and 0xFFFF

        val CASTLE_WARS_SARADOMIN_TIME_LEFT = of(WidgetGroupId.CASTLE_WARS_SARADOMIN, 25)

        val CASTLE_WARS_ZAMORAK_TIME_LEFT = of(WidgetGroupId.CASTLE_WARS_ZAMORAK, 25)

        val CHAT_REPORT_TEXT = of(WidgetGroupId.CHAT, 36)

        val INVENTORY_ITEMS = of(WidgetGroupId.INVENTORY, 0)

        val MINIMAP_ORBS_HP_CIRCLE = of(WidgetGroupId.MINIMAP_ORBS, 6)
        val MINIMAP_ORBS_SPEC_CIRCLE = of(WidgetGroupId.MINIMAP_ORBS, 31)
    }
}