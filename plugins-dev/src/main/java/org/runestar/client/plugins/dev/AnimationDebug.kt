package org.runestar.client.plugins.dev

import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.game.api.Actor
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.raw.access.XActor
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class AnimationDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(LiveCanvas.repaints.subscribe { g ->
            val p = Players.local?.accessor ?: return@subscribe

            g.color = Color.WHITE
            g.font = Fonts.PLAIN_11

            val x = 40
            var y = 40

            val strings = animString(p).split(',')

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
        val pos = actor.modelPosition
        if (!pos.isLoaded) return
        val height = actor.accessor.defaultHeight * 2 / 3
        val pt = pos.copy(height = height).toScreen() ?: return
        val s = animString(actor.accessor)
        g.drawStringShadowed(s, pt.x, pt.y)
    }

    private fun animString(p: XActor): String {
        return "m=${p.movementSequence} ${p.movementFrame.pad()}:${p.movementFrameCycle.pad()}," +
        "sa=${p.spotAnimation} ${p.spotAnimationFrame.pad()}:${p.spotAnimationFrameCycle.pad()}," +
        "sq=${p.sequence} ${p.sequenceFrame.pad()}:${p.sequenceFrameCycle.pad()}," +
        "y=${p.readySequence} w=${p.walkSequence} r=${p.runSequence}," +
        "wb=${p.walkBackSequence} wl=${p.walkLeftSequence} wr=${p.walkRightSequence}," +
        "tl=${p.turnLeftSequence} tr=${p.turnRightSequence}"
    }

    private fun Int.pad(): String {
        return this.toString().padStart(2, '0')
    }
}