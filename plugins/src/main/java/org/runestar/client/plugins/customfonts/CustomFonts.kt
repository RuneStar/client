package org.runestar.client.plugins.customfonts

import org.kxtra.swing.graphics.use
import org.kxtra.swing.image.BufferedImage
import org.kxtra.swing.image.DataBufferByte
import org.kxtra.swing.image.createCompatibleWritableRaster
import org.runestar.client.api.forms.FontForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.FontId
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XArchive
import org.runestar.client.game.raw.access.XFont
import org.runestar.client.game.raw.access.XFontName
import org.runestar.client.game.raw.access.XComponent
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Font
import java.awt.FontMetrics
import java.awt.image.BufferedImage
import java.nio.ByteBuffer
import javax.imageio.ImageTypeSpecifier

class CustomFonts : DisposablePlugin<CustomFonts.Settings>() {

    private val CHARS = charset("cp1252").decode(ByteBuffer.wrap(ByteArray(256) { it.toByte() }))

    override val defaultSettings = Settings()

    override val name = "Custom Fonts"

    private lateinit var oldFontMap: HashMap<*, *>

    override fun onStart() {
        oldFontMap = HashMap(CLIENT.fontsMap)

        val fonts = HashMap<Int, XFont>()
        val metrics = HashMap<Int, ByteArray>()
        for (fr in settings.fonts) {
            val f = fr.font.value
            val fm = CLIENT.canvas.getFontMetrics(f)
            val bm = binaryMetrics(fm)
            metrics[fr.id] = bm
            fonts[fr.id] = rasterize(f, fm, bm)
        }

        for (e in fonts) {
            fontName(e.key)?.let { CLIENT.fontsMap[it] = e.value }
        }
        reloadPrimaryFonts()

        add(XComponent.getFont.enter.subscribe {
            it.returned = fonts[it.instance.fontId] ?: return@subscribe
            it.skipBody = true
        })

        add(XArchive.takeFile.exit.subscribe {
            if (it.instance != CLIENT.archive13) return@subscribe
            it.returned = metrics[it.arguments[0] as Int] ?: return@subscribe
        })
    }

    override fun onStop() {
        CLIENT.fontsMap = oldFontMap
        reloadPrimaryFonts()
    }

    private fun fontName(fontId: Int): XFontName? {
        return when (fontId) {
            FontId.P11_FULL -> CLIENT.fontName_plain11
            FontId.P12_FULL -> CLIENT.fontName_plain12
            FontId.B12_FULL -> CLIENT.fontName_bold12
            FontId.VERDANA_11PT_REGULAR -> CLIENT.fontName_verdana11
            FontId.VERDANA_13PT_REGULAR -> CLIENT.fontName_verdana13
            FontId.VERDANA_15PT_REGULAR -> CLIENT.fontName_verdana15
            else -> null
        }
    }

    private fun reloadPrimaryFonts() {
        CLIENT.fontPlain11 = CLIENT.fontsMap[CLIENT.fontName_plain11] as XFont
        CLIENT.fontPlain12 = CLIENT.fontsMap[CLIENT.fontName_plain12] as XFont
        CLIENT.fontBold12 = CLIENT.fontsMap[CLIENT.fontName_bold12] as XFont
    }

    private fun rasterize(f: Font, fm: FontMetrics, bm: ByteArray): XFont {
        val maxAscent = fm.maxAscent
        val maxDescent = fm.maxDescent

        val bearings = IntArray(CHARS.length)

        val widths = IntArray(CHARS.length)
        val width = fm.maxAdvance
        widths.fill(width)

        val heights = IntArray(CHARS.length)
        val height = maxAscent + maxDescent
        heights.fill(height)

        val data = ByteArray(width * height)
        val cm = ImageTypeSpecifier.createFromBufferedImageType(BufferedImage.TYPE_BYTE_GRAY).colorModel
        val wr = cm.createCompatibleWritableRaster(width, height, DataBufferByte(data))
        val img = BufferedImage(cm, wr)

        val pixels = arrayOfNulls<ByteArray>(CHARS.length)

        CHARS.forEachIndexed { i, c ->
            if (excludeChar(c)) return@forEachIndexed
            img.createGraphics().use { g ->
                g.font = f
                g.drawString(c.toString(), 0, maxAscent)
            }
            pixels[i] = data.clone()
            data.fill(0)
        }

        return CLIENT._Font_(
                bm,
                bearings,
                bearings,
                widths,
                heights,
                null,
                pixels
        )
    }

    private fun excludeChar(c: Char): Boolean = c.isISOControl() || c == 160.toChar()

    private fun binaryMetrics(fm: FontMetrics): ByteArray {
        val bm = ByteArray(CHARS.length + 1)
        CHARS.forEachIndexed { i, c ->
            if (excludeChar(c)) return@forEachIndexed
            bm[i] = fm.charWidth(c).toByte()
        }
        bm[CHARS.length] = fm.maxAscent.toByte()
        return bm
    }

    class Settings(
            val fonts: List<FontReplace> = listOf(FontReplace(FontId.P12_FULL, FontForm(Font.SANS_SERIF, FontForm.Style.PLAIN, 12f)))
    ) : PluginSettings() {

        class FontReplace(val id: Int, val font: FontForm)
    }
}