package org.runestar.client.plugins.dev

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XFont
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings

class DumpFonts : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        val mapper = jacksonObjectMapper().enable(SerializationFeature.INDENT_OUTPUT)
        val component = CLIENT._Component_()
        val fontIds = CLIENT.archive13.groupIds ?: throw Exception()
        for (fontId in fontIds) {
            component.fontId = fontId
            val xfont = component.font ?: error(fontId)
            val font = font(xfont)
            val file = ctx.directory.resolve("$fontId.json")
            mapper.writeValue(file.toFile(), font)
        }
    }

    private fun font(f: XFont): Font {
        val glyphs = ArrayList<Glyph>(256)
        for (i in 0..255) {
            if (f.advances[i] != 0) {
                glyphs.add(Glyph(cp1252ToCodePoint(i), f.topBearings[i], f.heights[i], f.widths[i], f.advances[i], f.leftBearings[i], f.pixels[i].asList()))
            }
        }
        val kerns = ArrayList<Kern>()
        if (f.kerning != null) {
            for (a in 0..255) {
                for (b in 0..255) {
                    val i = (a shl 8) + b
                    val v = f.kerning[i].toInt()
                    if (v != 0) {
                        kerns.add(Kern(cp1252ToCodePoint(a), cp1252ToCodePoint(b), v))
                    }
                }
            }
        }
        return Font(f.ascent, f.maxAscent, f.maxDescent, glyphs, kerns)
    }

    private fun cp1252ToCodePoint(n: Int): Int {
        return String(byteArrayOf(n.toByte()), charset("windows-1252")).codePointAt(0)
    }

    data class Font(
            val ascent: Int,
            val maxAscent: Int,
            val maxDescent: Int,
            val glyphs: List<Glyph>,
            val kernings: List<Kern>
    )

    data class Kern(
            val a: Int,
            val b: Int,
            val size: Int
    )

    data class Glyph(
            val codePoint: Int,
            val topBearing: Int,
            val height: Int,
            val width: Int,
            val advance: Int,
            val leftBearing: Int,
            val pixels: List<Byte>
    )
}