package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.MouseCrosshair
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XGameShell
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.Point
import java.awt.event.MouseEvent

object Mouse {

    val x get() = accessor.mouseX

    val y get() = accessor.mouseY

    val location get() = Point(x, y)

    val crosshair get() = MouseCrosshair.LOOKUP.getValue(accessor.mouseCrosshair)

    /**
     * @see[java.awt.event.MouseListener]
     * @see[java.awt.event.MouseMotionListener]
     * @see[java.awt.event.MouseWheelListener]
     */
    val events: Observable<MouseEvent> = XGameShell.replaceCanvas.exit.map { accessor.canvas }.startWith(accessor.canvas)
            .flatMap { SwingObservable.mouse(it) }

    override fun toString(): String {
        return "Mouse(location=$location, crosshair=$crosshair)"
    }
}