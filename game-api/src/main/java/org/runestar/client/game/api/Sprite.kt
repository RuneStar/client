package org.runestar.client.game.api

import org.kxtra.swing.bufferedimage.BufferedImage
import org.kxtra.swing.image.draw
import org.kxtra.swing.image.height
import org.kxtra.swing.image.width
import org.kxtra.swing.imagetypespecifier.ImageTypeSpecifier
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XIndexedSprite
import org.runestar.client.game.raw.access.XSprite
import org.runestar.client.game.raw.base.Accessor
import java.awt.Image
import java.awt.image.*

abstract class Sprite(accessor: Accessor) : Wrapper(accessor) {

    abstract val colorModel: ColorModel

    /**
     * A copy using [ColorModel.getRGBdefault]
     */
    fun toArgbImage() = BufferedImage(asImage(), ImageTypeSpecifier(ColorModel.getRGBdefault()))

    abstract val dataBuffer: DataBuffer

    /**
     * A view using [colorModel]
     */
    fun asImage(): BufferedImage {
        normalize()
        val cm = colorModel
        val sm = cm.createCompatibleSampleModel(width, height)
        val wr = Raster.createWritableRaster(sm, dataBuffer, null)
        return BufferedImage(cm, wr)
    }

    /**
     * Draws [image] onto this
     */
    fun draw(image: Image) = asImage().draw(image)

    abstract val width: Int

    abstract val height: Int

    abstract val subWidth: Int

    abstract val subHeight: Int

    abstract val xOffset: Int

    abstract val yOffset: Int

    abstract fun normalize()

    class Direct(override val accessor: XSprite) : Sprite(accessor) {

        constructor(width: Int, height: Int) : this(CLIENT._Sprite_(width, height))

        val pixels: IntArray get() = accessor.pixels

        override val colorModel get() = COLOR_MODEL

        override val dataBuffer get() = DataBufferInt(pixels, pixels.size)

        override val width: Int get() = accessor.width

        override val height: Int get() = accessor.height

        override val subWidth: Int get() = accessor.subWidth

        override val subHeight: Int get() = accessor.subHeight

        override val xOffset: Int get() = accessor.xOffset

        override val yOffset: Int get() = accessor.yOffset

        override fun normalize() = accessor.normalize()

        companion object {

             @JvmField val COLOR_MODEL = RgbFlaggedColorModel(0)

            fun copy(image: Image): Direct {
                return Direct(image.width, image.height).apply {
                    draw(image)
                }
            }
        }
    }

    class Indexed(override val accessor: XIndexedSprite) : Sprite(accessor) {

        constructor(width: Int, height: Int, palette: IntArray) : this(
                CLIENT._IndexedSprite_().apply {
                    setWidth(width)
                    setHeight(height)
                    subWidth = width
                    subHeight = height
                    pixels = ByteArray(width * height)
                    setPalette(palette)
                }
        )

        val pixels: ByteArray get() = accessor.pixels

        val palette: IntArray get() = accessor.palette

        override val colorModel get() = IndexColorModel(8, palette.size, palette, 0, false, 0, DataBuffer.TYPE_BYTE)

        override val dataBuffer get() = DataBufferByte(pixels, pixels.size)

        override val width: Int get() = accessor.width

        override val height: Int get() = accessor.height

        override val subWidth: Int get() = accessor.subWidth

        override val subHeight: Int get() = accessor.subHeight

        override val xOffset: Int get() = accessor.xOffset

        override val yOffset: Int get() = accessor.yOffset

        override fun normalize() = accessor.normalize()

        companion object {

            fun copy(image: Image): Indexed {
                return Indexed(image.width, image.height, getPalette(image)).apply {
                    draw(image)
                }
            }

            private fun getRgb(image: Image): IntArray {
                return if (image is BufferedImage) {
                    image.getRGB(0, 0, image.width, image.height, null, 0, image.width)
                } else {
                    val pg = PixelGrabber(image, 0, 0, -1, -1, true)
                    check(pg.grabPixels())
                    pg.pixels as IntArray
                }
            }

            private fun getPalette(image: Image): IntArray = getRgb(image).toCollection(LinkedHashSet(listOf(0))).toIntArray()
        }
    }
}