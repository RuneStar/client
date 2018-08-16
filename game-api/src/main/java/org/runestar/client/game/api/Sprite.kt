package org.runestar.client.game.api

import org.kxtra.lang.intarray.replaceEach
import org.kxtra.swing.bufferedimage.BufferedImage
import org.kxtra.swing.image.draw
import org.kxtra.swing.imagetypespecifier.ImageTypeSpecifier
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XIndexedSprite
import org.runestar.client.game.raw.access.XSprite
import org.runestar.client.game.raw.base.Accessor
import java.awt.image.*

abstract class Sprite(accessor: Accessor) : Wrapper(accessor) {

    /**
     * A copy using [ColorModel.getRGBdefault]
     */
    abstract fun toBufferedImage(): BufferedImage

    abstract val width: Int

    abstract val height: Int

    class Direct(override val accessor: XSprite) : Sprite(accessor) {

        override fun toBufferedImage(): BufferedImage {
            val s = Direct(accessor.copyNormalized())
            s.addAlpha()
            return s.wrap()
        }

        private fun wrap(): BufferedImage {
            val buf = DataBufferInt(accessor.pixels, accessor.pixels.size)
            val cm = ColorModel.getRGBdefault()
            val sm = cm.createCompatibleSampleModel(accessor.width, accessor.height)
            val wr = Raster.createWritableRaster(sm, buf, null)
            return BufferedImage(cm, wr)
        }

        override val width: Int get() = accessor.width

        override val height: Int get() = accessor.height

        private fun addAlpha() {
            accessor.pixels.replaceEach {
                if (it == 0) 0 else it or -16777216
            }
        }

        private fun removeAlpha() {
            accessor.pixels.replaceEach {
                if (it and -16777216 != -16777216) 0 else it and 0xFFFFFF
            }
        }

        companion object {

            fun copy(bufferedImage: BufferedImage): Sprite {
                val s = Direct(CLIENT._Sprite_(bufferedImage.width, bufferedImage.height))
                s.wrap().draw(bufferedImage)
                s.removeAlpha()
                return s
            }
        }
    }

    class Indexed(override val accessor: XIndexedSprite) : Sprite(accessor) {

        val colorModel: IndexColorModel get() = IndexColorModel(8, accessor.palette.size, accessor.palette, 0, false, 0, DataBuffer.TYPE_BYTE)

        override fun toBufferedImage(): BufferedImage {
            return BufferedImage(wrap(), ImageTypeSpecifier(ColorModel.getRGBdefault()))
        }

        fun wrap(): BufferedImage {
            accessor.normalize()
            val buf = DataBufferByte(accessor.pixels, accessor.pixels.size)
            val cm = colorModel
            val sm = cm.createCompatibleSampleModel(accessor.width, accessor.height)
            val wr = Raster.createWritableRaster(sm, buf, null)
            return BufferedImage(cm, wr)
        }

        override val width: Int get() = accessor.width

        override val height: Int get() = accessor.height

        companion object {

            fun copy(bufferedImage: BufferedImage): Indexed {
                val w = bufferedImage.width
                val h = bufferedImage.height
                val x = CLIENT._IndexedSprite_().apply {
                    width = w
                    height = h
                    subWidth = w
                    subHeight = h
                    pixels = ByteArray(w * h)
                    palette = getPalette(bufferedImage)
                }
                val s = Indexed(x)
                s.wrap().draw(bufferedImage)
                return s
            }

            private fun getPalette(bufferedImage: BufferedImage): IntArray {
                return bufferedImage.getRGB(0, 0, bufferedImage.width, bufferedImage.height, null, 0, bufferedImage.width)
                        .toSet()
                        .toIntArray()
            }
        }
    }
}