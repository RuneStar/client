package org.runestar.client.api.util

import java.awt.Transparency
import java.awt.image.ColorModel
import java.awt.image.DataBuffer
import java.awt.image.DirectColorModel
import java.awt.image.Raster
import java.awt.image.SampleModel
import java.awt.image.WritableRaster

/**
 * A 24 bit RGB [ColorModel] which treats all pixel values as opaque except for [transparentFlag] which is fully
 * transparent
 */
data class RgbFlaggedColorModel(val transparentFlag: Int) : ColorModel(
        RGB.pixelSize,
        intArrayOf(8, 8, 8, 0),
        RGB.colorSpace,
        true,
        false,
        Transparency.BITMASK,
        DataBuffer.TYPE_INT
) {

    private companion object {
        @JvmField val RGB = DirectColorModel(24, 0xFF0000, 0xFF00, 0xFF)
    }

    override fun getAlpha(pixel: Int): Int = if (pixel == transparentFlag) 0 else 255

    override fun getRed(pixel: Int): Int = RGB.getRed(pixel)

    override fun getGreen(pixel: Int): Int = RGB.getGreen(pixel)

    override fun getBlue(pixel: Int): Int = RGB.getBlue(pixel)

    override fun createCompatibleSampleModel(w: Int, h: Int) = RGB.createCompatibleSampleModel(w, h)

    override fun createCompatibleWritableRaster(w: Int, h: Int) = RGB.createCompatibleWritableRaster(w, h)

    override fun isCompatibleRaster(raster: Raster?) = RGB.isCompatibleRaster(raster)

    override fun isCompatibleSampleModel(sm: SampleModel?) = RGB.isCompatibleSampleModel(sm)

    override fun getDataElement(components: IntArray?, offset: Int) = RGB.getDataElement(components, offset)

    override fun getDataElements(components: IntArray?, offset: Int, obj: Any?) = RGB.getDataElements(components, offset, obj)

    override fun getDataElements(rgb: Int, pixel: Any?) = RGB.getDataElements(rgb, pixel)

    override fun getComponents(pixel: Int, components: IntArray?, offset: Int) = RGB.getComponents(pixel, components, offset)

    override fun getComponents(pixel: Any?, components: IntArray?, offset: Int) = RGB.getComponents(pixel, components, offset)

    override fun coerceData(raster: WritableRaster?, isAlphaPremultiplied: Boolean) = RGB.coerceData(raster, isAlphaPremultiplied)

    override fun getAlphaRaster(raster: WritableRaster?) = RGB.getAlphaRaster(raster)
}