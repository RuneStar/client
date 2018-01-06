package org.runestar.client.game.api

enum class HeadIconPk(val id: Int) {

    SKULL(0),
    RED_SKULL(1),
//    BH_RED(2),
//    BH_BLUE(3),
//    BH_GREEN(4),
//    BH_SILVER(5),
//    BH_ALL(6),
//    BH_BRONZE(7),
    DMM_RED(8),
    DMM_BLUE(9),
    DMM_GREEN(10),
    DMM_SILVER(11),
    DMM_BRONZE(12);

    companion object {
        @JvmField val LOOKUP = values().associateBy { it.id }
    }
}