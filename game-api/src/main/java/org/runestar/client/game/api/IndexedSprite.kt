package org.runestar.client.game.api

import org.kxtra.swing.bufferedimage.toCompatibleImage
import org.kxtra.swing.graphics.drawImage
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XIndexedSprite
import java.awt.image.*

class IndexedSprite(override val accessor: XIndexedSprite) : Wrapper(accessor) {

    fun toBufferedImage(): BufferedImage {
        accessor.normalize()
        return wrapSprite(accessor).toCompatibleImage()
    }

    companion object {

        private fun wrapSprite(accessor: XIndexedSprite): BufferedImage {
            val buf = DataBufferByte(accessor.pixels, accessor.pixels.size)
            val cm = IndexColorModel(8, accessor.palette.size, accessor.palette, 0, false, 0, DataBuffer.TYPE_BYTE)
            val sm = cm.createCompatibleSampleModel(accessor.width, accessor.height)
            val wr = Raster.createWritableRaster(sm, buf, null)
            return BufferedImage(cm, wr, false, null)
        }

        fun copy(bufferedImage: BufferedImage): IndexedSprite {
            val w = bufferedImage.width
            val h = bufferedImage.height
            val x = Client.accessor._IndexedSprite_()
            x.width = w
            x.height = h
            x.subWidth = w
            x.subHeight = h
            x.pixels = ByteArray(w * h)
            x.palette = getPalette(bufferedImage)
            wrapSprite(x).graphics.drawImage(bufferedImage)
            return IndexedSprite(x)
        }

        private fun getPalette(bufferedImage: BufferedImage): IntArray {
            return bufferedImage.getRGB(0, 0, bufferedImage.width, bufferedImage.height, null, 0, bufferedImage.width)
                    .toSet()
                    .toIntArray()
        }
    }
}