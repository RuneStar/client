package org.runestar.client.game.api.live

import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import org.runestar.client.game.api.Canvas
import org.runestar.client.game.raw.access.XGameShell
import org.runestar.client.game.raw.access.XRasterProvider
import org.runestar.client.game.raw.CLIENT
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.ComponentEvent
import java.awt.event.FocusEvent

object LiveCanvas : Canvas {

    override val shape get() = Rectangle(CLIENT.canvas.size)

    val repaints: Observable<Graphics2D> = XRasterProvider.drawFull0.enter.map {
        it.instance.image.graphics as Graphics2D
    }

    val canvasReplacements: Observable<java.awt.Canvas> = XGameShell.replaceCanvas.exit.map { CLIENT.canvas }
            .startWith(CLIENT.canvas)

    /**
     * @see[java.awt.event.FocusListener]
     */
    val focusEvents: Observable<FocusEvent> = canvasReplacements.flatMap { SwingObservable.focus(it) }

    /**
     * @see[java.awt.event.ComponentListener]
     */
    val componentEvents: Observable<ComponentEvent> = canvasReplacements.flatMap { SwingObservable.component(it) }

    override fun toString(): String {
        return "LiveCanvas(shape=$shape)"
    }
}