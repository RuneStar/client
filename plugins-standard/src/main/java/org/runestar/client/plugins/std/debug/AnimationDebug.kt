package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color
import java.awt.Graphics2D

class AnimationDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()
        add(LiveCanvas.repaints.subscribe { g ->
            val p = Players.local?.accessor ?: return@subscribe

            g.color = Color.WHITE
            g.font = RUNESCAPE_CHAT_FONT

            val x = 40
            var y = 40

            val strings = listOf(
                    "movement=${p.movementSequence}, ${p.movementFrame}:${p.movementFrameCycle}",
                    "spotAnimation=${p.spotAnimation}, ${p.spotAnimationFrame}:${p.spotAnimationFrameCycle}",
                    "sequence=${p.sequence}, ${p.sequenceFrame}:${p.sequenceFrameCycle}",
                    "idle=${p.idleSequence}, walk=${p.walkSequence}, run=${p.runSequence}",
                    "turn=${p.turnSequence}, left=${p.turnLeftSequence}, right=${p.turnRightSequence}"
            )

            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 25
            }

            for (a in Npcs) {
                drawActor(g, a)
            }
            for (a in Players) {
                drawActor(g, a)
            }
        })
    }

    private fun drawActor(g: Graphics2D, actor: Actor) {
        val pos = actor.position
        if (!pos.isLoaded) return
        val height = actor.accessor.defaultHeight * 2 / 3
        val pt = pos.copy(height = height).toScreen() ?: return
        val s = "sa=${actor.accessor.spotAnimation}," +
                "sq=${actor.accessor.sequence}"
        g.drawStringShadowed(s, pt.x, pt.y)
    }
}