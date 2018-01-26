package org.runestar.client.plugins.std.mousehighlight

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.ColorForm
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Color
import java.awt.Rectangle
import kotlin.math.min

class MouseHighlight : DisposablePlugin<MouseHighlight.Settings>() {

    override val defaultSettings = Settings()

    companion object {
        val TAG_REGEX = "<.*?>".toRegex()
    }

    override val name = "Mouse Highlight"

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
            val boxHeight = textHeight + settings.paddingBottom + settings.paddingTop

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
            val textY = boxY + settings.paddingTop + g.fontMetrics.ascent

            g.color = Color.BLACK
            g.drawString(text, textX + 1, textY + 1)

            g.color = fontColor
            g.drawString(text, textX, textY)
        })
    }

    data class Settings(
            val ignoredActions: Set<String> = setOf("Cancel", "Walk here"),
            val paddingX: Int = 2,
            val paddingTop: Int = 4,
            val paddingBottom: Int = 1,
            val offsetX: Int = 3,
            val offsetY: Int = 3,
            val offsetYFlipped: Int = 22,
            val outlineColor: ColorForm = ColorForm(14, 13, 15),
            val fillColor: ColorForm = ColorForm(70, 61, 50, 156),
            val font: FontForm = FontForm(RUNESCAPE_SMALL_FONT),
            val fontColor: ColorForm = ColorForm(255, 255, 255)
    ) : PluginSettings()
}