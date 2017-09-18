package com.runesuite.client.api.live

import com.runesuite.client.raw.Client.accessor
import com.runesuite.client.raw.access.XGameShell
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.Point
import java.awt.event.MouseEvent

object Mouse {

    val x get() = accessor.mouseX

    val y get() = accessor.mouseY

    val location get() = Point(x, y)

    val crosshair get() = checkNotNull(Crosshair.LOOKUP[accessor.cursorColor]) { accessor.cursorColor }

    /**
     * @see[java.awt.event.MouseListener][java.awt.event.MouseMotionListener][java.awt.event.MouseWheelListener]
     */
    val events: Observable<MouseEvent> = XGameShell.replaceCanvas.exit.map { accessor.canvas }.startWith(accessor.canvas)
            .flatMap { SwingObservable.mouse(it) }

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