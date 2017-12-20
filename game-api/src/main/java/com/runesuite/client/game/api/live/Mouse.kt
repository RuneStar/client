package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.EntityTag
import com.runesuite.client.game.api.MouseCrossColor
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

    val viewportX get() = accessor.viewportMouse_x

    val viewportY get() = accessor.viewportMouse_y

    val viewportLocation get() = Point(viewportX, viewportY)

    val isInViewport get() = accessor.viewportMouse_isInViewport

    val entityCount get() = accessor.viewportMouse_entityCount

    val entityTags get() = List(entityCount) { EntityTag(accessor.viewportMouse_entityTags[it]) }

    val crossColor get() = MouseCrossColor.LOOKUP.getValue(accessor.mouseCrossColor)

    val crossState get() = accessor.mouseCrossState

    /**
     * @see[java.awt.event.MouseListener]
     * @see[java.awt.event.MouseMotionListener]
     * @see[java.awt.event.MouseWheelListener]
     */
    val events: Observable<MouseEvent> = XGameShell.replaceCanvas.exit.map { accessor.canvas }.startWith(accessor.canvas)
            .flatMap { SwingObservable.mouse(it) }

    override fun toString(): String {
        return "Mouse(location=$location)"
    }
}