package org.runestar.client.web.hiscore

import java.net.URI

enum class HiscoreEndpoint(private val m: String) {

    NORMAL("hiscore_oldschool"),
    IRONMAN("hiscore_oldschool_ironman"),
    HARDCORE_IRONMAN("hiscore_oldschool_hardcore_ironman"),
    ULTIMATE_IRONMAN("hiscore_oldschool_ultimate"),
    DEADMAN("hiscore_oldschool_deadman"),
    SEASONAL("hiscore_oldschool_seasonal"),
    TOURNAMENT("hiscore_oldschool_tournament"),
    ;

    internal fun query(player: String) = URI("https://services.runescape.com/m=$m/index_lite.ws?player=$player")
}