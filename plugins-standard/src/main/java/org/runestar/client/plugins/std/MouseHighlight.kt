package org.runestar.client.plugins.std

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import org.runestar.general.fonts.RUNESCAPE_CHAT_BOLD_FONT
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Font
import java.awt.Rectangle
import kotlin.math.min

class MouseHighlight : DisposablePlugin<MouseHighlight.Settings>() {

    override val defaultSettings = Settings()

    companion object {
        val TAG_REGEX = "<.*?>".toRegex()
    }

    // todo : color tags

    override fun start() {
        super.start()
        val outlineColor = settings.outlineColor.get()
        val fillColor = settings.fillColor.get()
        val font = settings.font.get()
        val fontColor = settings.fontColor.get()
        add(LiveCanvas.repaints.subscribe { g ->
            if (Menu.optionsCount <= 0 || Menu.isOpen) return@subscribe
            val option = Menu.getOption(0)
            val action = option.action
            if (action in settings.ignoredActions) return@subscribe
            val canvas = LiveCanvas.shape
            val mousePt = Mouse.location
            if (mousePt !in canvas) return@subscribe
            val rawText = "${option.action} ${option.targetName}".trim()
            val text = rawText.replace(TAG_REGEX, "")
            g.font = font
            val textHeight = g.fontMetrics.height
            val textWidth = g.fontMetrics.stringWidth(text)
            val boxWidth = textWidth + 2 * settings.paddingX
            val boxHeight = textHeight + 2 * settings.paddingY

            val boxX = min(canvas.width - 1, mousePt.x + boxWidth + settings.offsetX) - boxWidth
            val boxY = if (mousePt.y - boxHeight - settings.offsetY > 0) {
                mousePt.y - settings.offsetY - boxHeight
            } else {
                mousePt.y + settings.offsetYFlipped
            }
            val box = Rectangle(boxX, boxY, boxWidth, boxHeight)

            g.color = fillColor
            g.fill(box)

            g.color = outlineColor
            g.draw(box)

            val textX = boxX + settings.paddingX
            val textY = boxY + settings.paddingY + g.fontMetrics.ascent
            g.color = fontColor
            g.drawString(text, textX, textY)
        })
    }

    class Settings : PluginSettings() {
        val ignoredActions = setOf("Cancel", "Walk here")
        val paddingX = 2
        val paddingY = 2
        val offsetX = 3
        val offsetY = 3
        val offsetYFlipped = 22
        val outlineColor = ColorForm(14, 13, 15)
        val fillColor = ColorForm(70, 61, 50, 156)
        val font = FontForm(RUNESCAPE_SMALL_FONT)
        val fontColor = ColorForm(255, 255, 255)
    }
}