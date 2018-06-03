package org.runestar.client.game.api.live

import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import org.runestar.client.game.api.EntityTag
import org.runestar.client.game.api.MouseCross
import org.runestar.client.game.api.MouseCrossColor
import org.runestar.client.game.raw.Client.accessor
import java.awt.Point
import java.awt.event.MouseEvent

object Mouse {

    val x get() = accessor.mouseHandler_x

    val y get() = accessor.mouseHandler_y

    val location get() = Point(x, y)

    val viewportX get() = accessor.viewportMouse_x

    val viewportY get() = accessor.viewportMouse_y

    val viewportLocation get() = Point(viewportX, viewportY)

    val isInViewport get() = accessor.viewportMouse_isInViewport

    val entityCount get() = accessor.viewportMouse_entityCount

    val entityTags get() = List(entityCount) { EntityTag(accessor.viewportMouse_entityTags[it], Game.plane) }

    val cross get() = MouseCrossColor.of(accessor.mouseCrossColor)?.let { MouseCross(it, accessor.mouseCrossState) }

    /**
     * @see[java.awt.event.MouseListener]
     * @see[java.awt.event.MouseMotionListener]
     * @see[java.awt.event.MouseWheelListener]
     */
    val events: Observable<MouseEvent> = LiveCanvas.canvasReplacements.flatMap { SwingObservable.mouse(it) }

    override fun toString(): String {
        return "Mouse(location=$location, cross=$cross)"
    }
}