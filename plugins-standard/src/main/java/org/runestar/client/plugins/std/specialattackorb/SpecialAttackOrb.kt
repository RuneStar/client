package org.runestar.client.plugins.std.specialattackorb

import org.kxtra.swing.graphics.drawImage
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Varbit
import org.runestar.client.game.api.WindowMode
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.colorForPercent
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Color
import java.awt.Point
import java.awt.Rectangle
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class SpecialAttackOrb : DisposablePlugin<SpecialAttackOrb.Settings>() {

    companion object {
        const val ORB_OFFSET_BOTTOM = 6
        const val ORB_HEIGHT = 26
        const val WIDTH = 57
        const val HEIGHT = 36
        const val TEXT_AREA_WIDTH = 22
        const val TEXT_AREA_X_OFFSET = 4
        const val TEXT_AREA_Y_OFFSET = 27
    }

    override val name = "Special Attack Orb"

    override val defaultSettings = Settings()

    val orbBack = ImageIO.read(javaClass.getResourceAsStream("orb-back.png"))
    val orbIconDisabled = ImageIO.read(javaClass.getResourceAsStream("orb-icon-disabled.png"))
    val orbIconEnabled = ImageIO.read(javaClass.getResourceAsStream("orb-icon-enabled.png"))
    val orbOrbDisabled = ImageIO.read(javaClass.getResourceAsStream("orb-orb-disabled.png"))
    val orbOrbEnabled = ImageIO.read(javaClass.getResourceAsStream("orb-orb-enabled.png"))

    val font = RUNESCAPE_SMALL_FONT

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            if (Game.state == GameState.TITLE) return@subscribe
            if (!Client.accessor.isMembersWorld && !settings.showInF2p) return@subscribe
            g.font = font
            val offset = when (Game.windowMode) {
                WindowMode.FIXED -> settings.fixedOffset
                WindowMode.RESIZABLE -> settings.resizableOffset
            }
            val location = Point(Client.accessor.canvas.width - offset.x, offset.y)
            g.drawImage(orbBack, location)

            val spec = Game.varps[Varbit.SPECIAL_ATTACK_PERCENT] / 10
            val specString = spec.toString()
            val specPercent = spec / 100.0
            val orbHeight = (ORB_HEIGHT * specPercent).roundToInt()
            g.clip = Rectangle(location.x, location.y + HEIGHT - ORB_OFFSET_BOTTOM - orbHeight, WIDTH, HEIGHT)
            val isEnabled = Game.varps[Varbit.SPECIAL_ATTACK_ENABLED] != 0
            val orbOrb = if (isEnabled) orbOrbEnabled else orbOrbDisabled
            g.drawImage(orbOrb, location)

            g.clip = null
            val orbIcon = if (isEnabled) orbIconEnabled else orbIconDisabled
            g.drawImage(orbIcon, location)

            val stringWidth = g.fontMetrics.stringWidth(specString)
            val textY = location.y + TEXT_AREA_Y_OFFSET
            val textX = location.x + TEXT_AREA_X_OFFSET + ((TEXT_AREA_WIDTH - stringWidth + 1) / 2)
            g.color = Color.BLACK
            g.drawString(specString, textX + 1, textY + 1)
            g.color = colorForPercent(specPercent)
            g.drawString(specString, textX, textY)
        })
    }

    class Settings : PluginSettings() {

        val fixedOffset = Point(58, 22)
        val resizableOffset = Point(152, 152)
        val showInF2p = false
    }
}