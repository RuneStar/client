package org.runestar.client.plugins.mousetooltips

import org.runestar.client.api.Fonts
import org.runestar.client.api.forms.RgbaForm
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Menu
import org.runestar.client.game.api.live.Mouse
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Rectangle
import kotlin.math.min

class MouseTooltips : DisposablePlugin<MouseTooltips.Settings>() {

    override val defaultSettings = Settings()

    companion object {
        val TAG_REGEX = "<.*?>".toRegex()

        const val PADDING_X = 2
        const val PADDING_TOP = 2
        const val PADDING_BOTTOM = 1
    }

    override val name = "Mouse Tooltips"

    // todo : color tags

    override fun start() {
        val outlineColor = settings.outlineColor.get()
        val fillColor = settings.fillColor.get()
        val fontColor = settings.fontColor.get()
        add(LiveCanvas.repaints.subscribe { g ->
            if (Menu.optionsCount <= 0 || Menu.isOpen) return@subscribe
            val option = Menu.getOption(0)
            val action = option.action
            val target = option.targetName
            if (action in settings.ignoredActions) return@subscribe
            val canvas = LiveCanvas.shape
            val mousePt = Mouse.location
            if (mousePt !in canvas) return@subscribe
            val rawText = if (target.isEmpty()) {
                action
            } else {
                "$action $target"
            }
            val text = rawText.replace(TAG_REGEX, "")
            g.font = Fonts.PLAIN_11
            val textHeight = g.fontMetrics.height
            val textWidth = g.fontMetrics.stringWidth(text)
            val boxWidth = textWidth + 2 * PADDING_X
            val boxHeight = textHeight + PADDING_TOP + PADDING_BOTTOM

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

            val textX = boxX + PADDING_X
            val textY = boxY + PADDING_TOP + g.fontMetrics.ascent
            g.color = fontColor
            g.drawStringShadowed(text, textX, textY)
        })
    }

    data class Settings(
            val ignoredActions: Set<String> = setOf("Cancel", "Walk here"),
            val offsetX: Int = 3,
            val offsetY: Int = 3,
            val offsetYFlipped: Int = 22,
            val outlineColor: RgbaForm = RgbaForm(14, 13, 15),
            val fillColor: RgbaForm = RgbaForm(70, 61, 50, 156),
            val fontColor: RgbaForm = RgbaForm(255, 255, 255)
    ) : PluginSettings()
}