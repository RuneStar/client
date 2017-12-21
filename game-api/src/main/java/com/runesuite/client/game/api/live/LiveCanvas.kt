package com.runesuite.client.game.api.live

import com.runesuite.client.game.api.Canvas
import com.runesuite.client.game.raw.Client.accessor
import com.runesuite.client.game.raw.access.XGameShell
import com.runesuite.client.game.raw.access.XRasterProvider
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import org.kxtra.swing.graphics2d.create2D
import org.kxtra.swing.toolkit.fontDesktopHints
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.Toolkit
import java.awt.event.ComponentEvent
import java.awt.event.FocusEvent

object LiveCanvas : Canvas {

    init {
        accessor.gameDrawingMode = 2
    }

    private val desktopHints = Toolkit.getDefaultToolkit().fontDesktopHints()

    override val shape get() = Rectangle(accessor.canvas.size)

    val repaints: Observable<Graphics2D> = XRasterProvider.drawFull0.enter.map {
        val g = it.instance.image.graphics as Graphics2D
        desktopHints?.let { g.addRenderingHints(it) }
        g
    }.publish().refCount().map { it.create2D() }

    val canvasReplacements: Observable<java.awt.Canvas> get() = XGameShell.replaceCanvas.exit.map { accessor.canvas }
            .startWith(accessor.canvas)

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