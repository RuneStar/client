package org.runestar.client.api.game.live

import io.reactivex.rxjava3.core.Observable
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XRasterProvider
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.Point
import java.awt.Rectangle

object Canvas {

    val width: Int get() = CLIENT.canvasWidth

    val height: Int get() = CLIENT.canvasHeight

    val size get() = Dimension(width, height)

    val shape get() = Rectangle(0, 0, width, height)

    fun contains(x: Int, y: Int) = x < width && y < height && x >= 0 && y >= 0

    operator fun contains(point: Point) = contains(point.x, point.y)

    val repaints: Observable<Graphics2D> = XRasterProvider.drawFull.enter.map {
        it.instance.image.graphics as Graphics2D
    }
}