package com.runesuite.client.game.live

import com.runesuite.client.base.Client.accessor
import com.runesuite.client.base.access.XGraphicsProvider
import io.reactivex.Observable
import java.awt.Graphics2D
import java.awt.Rectangle

interface Canvas {

    val shape: Rectangle

    object Live : Canvas {

        override val shape get() = Rectangle(accessor.canvas.size)

        val repaints: Observable<Graphics2D> = XGraphicsProvider.drawFull0.ENTER.map {
            val gp = it.instance as XGraphicsProvider
            gp.image.graphics as Graphics2D
        }

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