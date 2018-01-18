package org.runestar.client.plugins.std.specorb

import org.kxtra.swing.graphics.drawImage
import org.kxtra.swing.graphics2d.drawString
import org.kxtra.swing.point.plus
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Varbit
import org.runestar.client.game.api.WindowMode
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.Color
import java.awt.Font
import java.awt.Point
import java.awt.Rectangle
import javax.imageio.ImageIO
import kotlin.math.roundToInt

class SpecOrb : DisposablePlugin<SpecOrb.Settings>() {

    companion object {
        val ORB_OFFSET_BOTTOM = 6
        val ORB_HEIGHT = 26
        val WIDTH = 57
        val HEIGHT = 36
    }

    override val defaultSettings = Settings()

    val orbBack = ImageIO.read(javaClass.getResourceAsStream("orb-back.png"))
    val orbIconDisabled = ImageIO.read(javaClass.getResourceAsStream("orb-icon-disabled.png"))
    val orbIconEnabled = ImageIO.read(javaClass.getResourceAsStream("orb-icon-enabled.png"))
    val orbOrbDisabled = ImageIO.read(javaClass.getResourceAsStream("orb-orb-disabled.png"))
    val orbOrbEnabled = ImageIO.read(javaClass.getResourceAsStream("orb-orb-enabled.png"))

    val font = Font(Font.SANS_SERIF, Font.PLAIN, 11)

    // todo : actual font, font shadow, font spacing, font color

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            if (Game.state == GameState.TITLE) return@subscribe
            if (!Client.accessor.isMembersWorld && !settings.showInF2p) return@subscribe
            g.font = font
            g.color = Color.GREEN
            val offset = when (Game.windowMode) {
                WindowMode.FIXED -> settings.fixedOffset
                WindowMode.RESIZABLE -> settings.resizableOffset
            }
            val location = Point(Client.accessor.canvas.width - offset.x, offset.y)
            g.drawImage(orbBack, location)

            val spec = Game.varps[Varbit.SPECIAL_ATTACK_PERCENT] / 10
            val specString = spec.toString()
            val orbHeight = (ORB_HEIGHT * (spec / 100.0)).roundToInt()
            g.clip = Rectangle(location.x, location.y + HEIGHT - ORB_OFFSET_BOTTOM - orbHeight, WIDTH, HEIGHT)
            val isEnabled = Game.varps[Varbit.SPECIAL_ATTACK_ENABLED] != 0
            val orbOrb = if (isEnabled) orbOrbEnabled else orbOrbDisabled
            g.drawImage(orbOrb, location)

            g.clip = null
            val orbIcon = if (isEnabled) orbIconEnabled else orbIconDisabled
            g.drawImage(orbIcon, location)

            val textLocation = location + Point(5, 25)
//            val textY = location.y + 25
            g.drawString(specString, textLocation)
        })
    }

    class Settings : PluginSettings() {

        val fixedOffset = Point(58, 22)
        val resizableOffset = Point(152, 152)
        val showInF2p = true
    }
}