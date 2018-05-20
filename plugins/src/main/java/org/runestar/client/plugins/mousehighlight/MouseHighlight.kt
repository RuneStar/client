package org.runestar.client.plugins.mousehighlight

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.RgbaForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.FontForm
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
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
        val outlineColor = ctx.settings.outlineColor.get()
        val fillColor = ctx.settings.fillColor.get()
        val font = ctx.settings.font.get()
        val fontColor = ctx.settings.fontColor.get()
        add(LiveCanvas.repaints.subscribe { g ->
            if (Menu.optionsCount <= 0 || Menu.isOpen) return@subscribe
            val option = Menu.getOption(0)
            val action = option.action
            val target = option.targetName
            if (action in ctx.settings.ignoredActions) return@subscribe
            val canvas = LiveCanvas.shape
            val mousePt = Mouse.location
            if (mousePt !in canvas) return@subscribe
            val rawText = if (target.isEmpty()) {
                action
            } else {
                "$action $target"
            }
            val text = rawText.replace(TAG_REGEX, "")
            g.font = font
            val textHeight = g.fontMetrics.height
            val textWidth = g.fontMetrics.stringWidth(text)
            val boxWidth = textWidth + 2 * ctx.settings.paddingX
            val boxHeight = textHeight + ctx.settings.paddingBottom + ctx.settings.paddingTop

            val boxX = min(canvas.width - 1, mousePt.x + boxWidth + ctx.settings.offsetX) - boxWidth
            val boxY = if (mousePt.y - boxHeight - ctx.settings.offsetY > 0) {
                mousePt.y - ctx.settings.offsetY - boxHeight
            } else {
                mousePt.y + ctx.settings.offsetYFlipped
            }
            val box = Rectangle(boxX, boxY, boxWidth, boxHeight)

            g.color = fillColor
            g.fill(box)

            g.color = outlineColor
            g.draw(box)

            val textX = boxX + ctx.settings.paddingX
            val textY = boxY + ctx.settings.paddingTop + g.fontMetrics.ascent
            g.color = fontColor
            g.drawStringShadowed(text, textX, textY)
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
            val outlineColor: RgbaForm = RgbaForm(14, 13, 15),
            val fillColor: RgbaForm = RgbaForm(70, 61, 50, 156),
            val font: FontForm = FontForm(RUNESCAPE_SMALL_FONT),
            val fontColor: RgbaForm = RgbaForm(255, 255, 255)
    ) : PluginSettings()
}