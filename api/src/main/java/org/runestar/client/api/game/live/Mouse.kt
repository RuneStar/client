package org.runestar.client.api.game.live

import hu.akarnokd.rxjava3.swing.SwingObservable
import io.reactivex.rxjava3.core.Observable
import org.runestar.client.raw.CLIENT
import java.awt.Point
import java.awt.event.MouseEvent

object Mouse {

    val x get() = CLIENT.mouseHandler_x

    val y get() = CLIENT.mouseHandler_y

    val location get() = Point(x, y)

    val viewportX get() = CLIENT.viewportMouse_x

    val viewportY get() = CLIENT.viewportMouse_y

    val viewportLocation get() = Point(viewportX, viewportY)

    val isInViewport get() = CLIENT.viewportMouse_isInViewport

    val entityCount get() = CLIENT.viewportMouse_entityCount

    val tags: LongArray get() = CLIENT.viewportMouse_entityTags

    val crossColor: Int get() = CLIENT.mouseCrossColor

    val crossState: Int get() = CLIENT.mouseCrossState

    /**
     * @see[java.awt.event.MouseListener]
     * @see[java.awt.event.MouseMotionListener]
     * @see[java.awt.event.MouseWheelListener]
     */
    val events: Observable<MouseEvent> get() = Canvas.canvasReplacements.flatMap { SwingObservable.mouse(it) }

    override fun toString(): String {
        return "Mouse($location)"
    }
}