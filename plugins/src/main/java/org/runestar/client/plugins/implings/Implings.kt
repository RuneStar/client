package org.runestar.client.plugins.implings

import com.google.common.collect.ImmutableSet
import org.runestar.cache.generated.NpcId
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Npc
import org.runestar.client.game.api.Region
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color
import java.awt.Graphics2D
import java.awt.RenderingHints

@Suppress("UNUSED_PARAMETER")
class Implings : DisposablePlugin<Implings.Settings>() {

    private companion object {
        val PURO_PURO_REGION = Region(40, 67, 0)
        val DRAW_COLOR = Color(255, 255, 255, 140)
        val TEXT_COLOR = Color.WHITE
    }

    override val defaultSettings = Settings()

    private lateinit var implingIds: Set<Int>

    @Volatile
    private var implings: List<Npc> = emptyList()

    override fun start() {
        implingIds = ctx.settings.ids()
        add(Game.ticks.subscribe(::onTick))
        add(Game.stateChanges.filter { it == GameState.TITLE }.subscribe(::onLogOut))
        add(LiveCanvas.repaints.subscribe(::onRepaint))
        if (ctx.settings.drawMinimapInPuroPuro) {
            add(Game.ticks.subscribe(::onTickMinimap))
        }
    }

    override fun stop() {
        super.stop()
        implings = emptyList()
        if (ctx.settings.drawMinimapInPuroPuro && inPuroPuro() && LiveMinimap.isDrawn) {
            LiveMinimap.isDrawn = false
        }
    }

    private fun onTick(u: Unit) {
        implings = Npcs.filter(::isImpling)
    }

    private fun onTickMinimap(u: Unit) {
        if (!LiveMinimap.isDrawn && inPuroPuro()) {
            LiveMinimap.isDrawn = true
        }
    }

    private fun onLogOut(gameState: GameState) {
        implings = emptyList()
    }

    private fun onRepaint(g: Graphics2D) {
        val npcs = implings
        if (npcs.isEmpty()) return

        g.font = RUNESCAPE_CHAT_FONT
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)

        npcs.forEach { npc ->
            val loc = npc.location
            if (!loc.isLoaded) return@forEach

            g.color = DRAW_COLOR
            val model = npc.model
            if (model != null) {
                model.drawBoundingBox(g)
            } else {
                val tileOutline = loc.outline()
                g.draw(tileOutline)
            }

            g.color = TEXT_COLOR
            val pos = npc.position.copy(height = npc.accessor.defaultHeight / 3)
            if (!pos.isLoaded) return@forEach
            val name = npc.definition?.name?.substringBefore(' ') ?: "Impling"

            val minimapPt = pos.toScreen(Projections.minimap)
            if (minimapPt != null) {
                g.drawStringShadowed(name, minimapPt.x, minimapPt.y)
            }

            val viewportPt = pos.toScreen(Projections.viewport)
            if (viewportPt != null) {
                val nameWidth = g.fontMetrics.stringWidth(name)
                g.drawStringShadowed(name, viewportPt.x - nameWidth / 2, viewportPt.y)
            }
        }
    }

    private fun isImpling(npc: Npc): Boolean {
        val def = npc.definition ?: return false
        return implingIds.contains(def.id)
    }

    private fun inPuroPuro(): Boolean {
        val region = Players.local?.location?.toGlobalTile()?.region ?: return false
        return region == PURO_PURO_REGION
    }

    class Settings(
            val drawMinimapInPuroPuro: Boolean = true,
            val baby: Boolean = false,
            val young: Boolean = false,
            val gourmet: Boolean = false,
            val earth: Boolean = false,
            val essence: Boolean = false,
            val eclectic: Boolean = true,
            val nature: Boolean = true,
            val magpie: Boolean = true,
            val ninja: Boolean = true,
            val dragon: Boolean = true,
            val lucky: Boolean = true
    ) : PluginSettings() {

        fun ids(): Set<Int> {
            val b = ImmutableSet.builder<Int>()
            b.add(1618).add(1633).add(1634) // spawns
            if (baby) b.add(NpcId.BABY_IMPLING_1635).add(NpcId.BABY_IMPLING_1645)
            if (young) b.add(NpcId.YOUNG_IMPLING_1636).add(NpcId.YOUNG_IMPLING_1646)
            if (gourmet) b.add(NpcId.GOURMET_IMPLING_1637).add(NpcId.GOURMET_IMPLING_1647)
            if (earth) b.add(NpcId.EARTH_IMPLING_1638).add(NpcId.EARTH_IMPLING_1648)
            if (essence) b.add(NpcId.ESSENCE_IMPLING_1639).add(NpcId.ESSENCE_IMPLING_1649)
            if (eclectic) b.add(NpcId.ECLECTIC_IMPLING_1640).add(NpcId.ECLECTIC_IMPLING_1650)
            if (nature) b.add(NpcId.NATURE_IMPLING_1641).add(NpcId.NATURE_IMPLING_1651)
            if (magpie) b.add(NpcId.MAGPIE_IMPLING_1642).add(NpcId.MAGPIE_IMPLING_1652)
            if (ninja) b.add(NpcId.NINJA_IMPLING_1643).add(NpcId.NINJA_IMPLING_1653)
            if (dragon) b.add(NpcId.DRAGON_IMPLING_1644).add(NpcId.DRAGON_IMPLING_1654)
            if (lucky) b.add(NpcId.LUCKY_IMPLING_7233).add(NpcId.LUCKY_IMPLING_7302)
            return b.build()
        }
    }
}