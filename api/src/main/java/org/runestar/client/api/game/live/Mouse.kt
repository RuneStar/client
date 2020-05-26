package org.runestar.client.api.game.live

import io.reactivex.rxjava3.core.Observable
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XMouseHandler
import org.runestar.client.raw.base.MethodEvent
import java.awt.Point

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

    val methods: Observable<MethodEvent<XMouseHandler, Void>> = Observable.mergeArray(
            XMouseHandler.mouseClicked.enter,
            XMouseHandler.mouseDragged.enter,
            XMouseHandler.mouseEntered.enter,
            XMouseHandler.mouseExited.enter,
            XMouseHandler.mouseMoved.enter,
            XMouseHandler.mousePressed.enter,
            XMouseHandler.mouseReleased.enter
    )

    override fun toString(): String {
        return "Mouse($location)"
    }
}