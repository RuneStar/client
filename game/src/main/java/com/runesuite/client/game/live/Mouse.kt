package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import java.awt.Point

object Mouse {

    val x get() = accessor.mouseX

    val y get() = accessor.mouseY

    val location get() = Point(x, y)

    val crosshair get() = checkNotNull(Crosshair.LOOKUP[accessor.cursorColor]) { accessor.cursorColor }

    override fun toString(): String {
        return "Mouse(location=$location, crosshair=$crosshair)"
    }

    enum class Crosshair(val id: Int) {

        NONE(0),
        YELLOW(1),
        RED(2);

        companion object {
            val LOOKUP = values().associateBy { it.id }
        }
    }
}