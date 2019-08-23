package org.runestar.client.game.api.live

import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XGameShell
import org.runestar.client.game.raw.access.XRasterProvider
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle
import java.awt.event.ComponentEvent
import java.awt.event.FocusEvent

object Canvas {

    const val FIXED_WIDTH = 765

    const val FIXED_HEIGHT = 503

    val width: Int get() = CLIENT.canvasWidth

    val height: Int get() = CLIENT.canvasHeight

    val size get() = Dimension(width, height)

    val shape get() = Rectangle(0, 0, width, height)

    fun contains(x: Int, y: Int) = x < width && y < height && x >= 0 && y >= 0

    operator fun contains(point: Point) = contains(point.x, point.y)

    val repaints: Observable<Graphics2D> = XRasterProvider.drawFull0.enter.map {
        it.instance.image.graphics as Graphics2D
    }

    val canvasReplacements: Observable<java.awt.Canvas> get() = XGameShell.addCanvas.exit.map { CLIENT.canvas }
            .startWith(CLIENT.canvas)

    /**
     * @see[java.awt.event.FocusListener]
     */
    val focusEvents: Observable<FocusEvent> get() = canvasReplacements.flatMap { SwingObservable.focus(it) }

    /**
     * @see[java.awt.event.ComponentListener]
     */
    val componentEvents: Observable<ComponentEvent> get() = canvasReplacements.flatMap { SwingObservable.component(it) }

    override fun toString(): String {
        return "Canvas($shape)"
    }
}