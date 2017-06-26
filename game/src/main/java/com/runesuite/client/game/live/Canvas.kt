package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.base.access.XGameShell
import com.runesuite.client.base.access.XGraphicsProvider
import hu.akarnokd.rxjava2.swing.SwingObservable
import io.reactivex.Observable
import java.awt.Graphics2D
import java.awt.Rectangle
import java.awt.event.ComponentEvent
import java.awt.event.FocusEvent

interface Canvas {

    val shape: Rectangle

    object Live : Canvas {

        override val shape get() = Rectangle(accessor.canvas.size)

        val repaints: Observable<Graphics2D> = XGraphicsProvider.drawFull0.ENTER.map {
            val gp = it.instance as XGraphicsProvider
            gp.image.graphics as Graphics2D
        }

        /**
         * @see[java.awt.event.FocusListener]
         */
        val focusEvents: Observable<FocusEvent> = XGameShell.replaceCanvas.EXIT.map { Unit }.startWith(Unit)
                .flatMap { SwingObservable.focus(accessor.canvas) }

        /**
         * @see[java.awt.event.ComponentListener]
         */
        val componentEvents: Observable<ComponentEvent> = XGameShell.replaceCanvas.EXIT.map { Unit }.startWith(Unit)
                .flatMap { SwingObservable.component(accessor.canvas) }

        override fun toString(): String {
            return "Canvas.Live(shape=$shape)"
        }
    }

    companion object {
        val FIXED = Copy(Rectangle(0, 0, 765, 503))
    }

    fun copyOf(): Copy = Copy(shape)

    data class Copy(override val shape: Rectangle): Canvas
}