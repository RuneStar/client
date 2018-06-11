package org.runestar.client.game.api

import org.kxtra.lang.intarray.replaceEach
import org.kxtra.swing.bufferedimage.toCompatibleImage
import org.kxtra.swing.graphics.drawImage
import org.runestar.client.game.raw.Accessor
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XIndexedSprite
import org.runestar.client.game.raw.access.XSprite
import java.awt.image.*

abstract class Sprite(accessor: Accessor) : Wrapper(accessor) {

    abstract fun toBufferedImage(): BufferedImage

    abstract val width: Int

    abstract val height: Int

    class Direct(override val accessor: XSprite) : Sprite(accessor) {

        override fun toBufferedImage(): BufferedImage {
            val copy = accessor.copyNormalized()
            copy.pixels.replaceEach {
                if (it == 0) 0 else it or -16777216
            }
            return wrapSprite(copy)
        }

        override val width: Int get() = accessor.width

        override val height: Int get() = accessor.height

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
                return Direct(xs)
            }
        }
    }

    class Indexed(override val accessor: XIndexedSprite) : Sprite(accessor) {

        override fun toBufferedImage(): BufferedImage {
            accessor.normalize()
            return wrapSprite(accessor).toCompatibleImage()
        }

        override val width: Int get() = accessor.width

        override val height: Int get() = accessor.height

        companion object {

            private fun wrapSprite(accessor: XIndexedSprite): BufferedImage {
                val buf = DataBufferByte(accessor.pixels, accessor.pixels.size)
                val cm = IndexColorModel(8, accessor.palette.size, accessor.palette, 0, false, 0, DataBuffer.TYPE_BYTE)
                val sm = cm.createCompatibleSampleModel(accessor.width, accessor.height)
                val wr = Raster.createWritableRaster(sm, buf, null)
                return BufferedImage(cm, wr, false, null)
            }

            fun copy(bufferedImage: BufferedImage): Indexed {
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
                return Indexed(x)
            }

            private fun getPalette(bufferedImage: BufferedImage): IntArray {
                return bufferedImage.getRGB(0, 0, bufferedImage.width, bufferedImage.height, null, 0, bufferedImage.width)
                        .toSet()
                        .toIntArray()
            }
        }
    }
}