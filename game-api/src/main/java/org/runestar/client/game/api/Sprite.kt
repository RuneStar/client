package org.runestar.client.game.api

import org.kxtra.lang.intarray.replaceEach
import org.kxtra.swing.graphics.drawImage
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XSprite
import java.awt.image.BufferedImage
import java.awt.image.ColorModel
import java.awt.image.DataBufferInt
import java.awt.image.Raster

class Sprite(override val accessor: XSprite) : Wrapper(accessor) {

    fun toBufferedImage(): BufferedImage {
        val copy = accessor.copyNormalized()
        copy.pixels.replaceEach {
            if (it == 0) 0 else it or -16777216
        }
        return wrapSprite(copy)
    }

    companion object {

        private fun wrapSprite(accessor: XSprite): BufferedImage {
            val buf = DataBufferInt(accessor.pixels, accessor.pixels.size)
            val cm = ColorModel.getRGBdefault()
            val sm = cm.createCompatibleSampleModel(accessor.width, accessor.height)
            val wr = Raster.createWritableRaster(sm, buf, null)
            return BufferedImage(cm, wr, false, null)
        }

        fun copy(bufferedImage: BufferedImage): Sprite {
            val xs = Client.accessor._Sprite_(bufferedImage.width, bufferedImage.height)
            wrapSprite(xs).graphics.drawImage(bufferedImage)
            xs.pixels.replaceEach {
                it and 0xFFFFFF
            }
            return Sprite(xs)
        }
    }
}