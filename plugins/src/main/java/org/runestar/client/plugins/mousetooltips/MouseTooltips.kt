package org.runestar.client.plugins.mousetooltips

import org.runestar.client.api.Fonts
import org.runestar.client.api.forms.FontForm
import org.runestar.client.api.forms.InsetsForm
import org.runestar.client.api.overlay.TextOverlay
import org.runestar.client.api.overlay.withBackground
import org.runestar.client.api.overlay.withBorder
import org.runestar.client.api.overlay.withPadding
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.api.game.live.MiniMenu
import org.runestar.client.api.game.live.Mouse
import org.runestar.client.api.game.removeTags
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color
import java.awt.Dimension
import java.awt.Point
import kotlin.math.min

class MouseTooltips : DisposablePlugin<MouseTooltips.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Mouse Tooltips"

    // todo : color tags

    override fun onStart() {
        val text = TextOverlay("-", settings.font.value, Color.WHITE)
        val overlay = text
                .withPadding(settings.padding)
                .withBackground()
                .withBorder()
        val size = Dimension()
        add(Canvas.repaints.subscribe { g ->
            if (MiniMenu.optionsCount <= 0 || MiniMenu.isOpen) return@subscribe
            val option = MiniMenu.getOption(MiniMenu.optionsCount - 1)
            val action = option.action
            val target = option.targetName
            if (action in settings.ignoredActions) return@subscribe
            val mousePt = Mouse.location
            if (mousePt !in Canvas) return@subscribe
            val rawText = if (target.isEmpty()) {
                action
            } else {
                "$action $target"
            }
            val newString = removeTags(rawText)
            text.string = newString
            overlay.getSize(g, size)

            val x = min(Canvas.width - 1, mousePt.x + size.width + settings.offset.x) - size.width
            val y = if (mousePt.y - size.height - settings.offset.y > 0) {
                mousePt.y - settings.offset.y - size.height
            } else {
                mousePt.y + settings.offsetYFlipped
            }

            g.translate(x, y)
            overlay.draw(g, size)
        })
    }

    data class Settings(
            val ignoredActions: Set<String> = setOf("Cancel", "Walk here"),
            val offset: Point = Point(3, 3),
            val offsetYFlipped: Int = 32,
            val font: FontForm = FontForm(Fonts.PLAIN_11),
            val padding: InsetsForm = InsetsForm(1, 2, 1, 2)
    ) : PluginSettings()
}