package org.runestar.client.plugins.implings

import com.google.common.collect.ImmutableSet
import org.runestar.client.api.Fonts
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.game.api.GameState
import org.runestar.client.game.api.Npc
import org.runestar.client.game.api.NpcId
import org.runestar.client.game.api.Region
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.awt.Graphics2D

class Implings : DisposablePlugin<Implings.Settings>() {

    private companion object {
        val PURO_PURO_REGION_ID = Region(40, 67).id
        val DRAW_COLOR = Color(255, 255, 255, 140)
        val TEXT_COLOR = Color.WHITE
    }

    override val defaultSettings = Settings()

    private var implings: List<Npc> = emptyList()

    override fun onStart() {
        add(Game.ticks.filter { isLoggedIn() }.subscribe { onTick() })
        add(LiveCanvas.repaints.filter { isLoggedIn() }.subscribe(::onRepaint))
        if (settings.drawMinimapInPuroPuro) {
            add(Game.ticks.filter { shouldDrawMinimap() }.subscribe { LiveMinimap.isDrawn = true })
        }
    }

    override fun onStop() {
        implings = emptyList()
        if (settings.drawMinimapInPuroPuro && inPuroPuro() && LiveMinimap.isDrawn) {
            LiveMinimap.isDrawn = false
        }
    }

    private fun onTick() {
        implings = Npcs.filter(::isImpling)
    }

    private fun shouldDrawMinimap(): Boolean {
        return !LiveMinimap.isDrawn && inPuroPuro()
    }

    private fun onRepaint(g: Graphics2D) {
        val npcs = implings
        if (npcs.isEmpty()) return

        g.font = Fonts.PLAIN_12

        npcs.forEach { npc ->
            val loc = npc.location
            if (!loc.isLoaded) return@forEach

            g.color = DRAW_COLOR
            val model = npc.model
            if (model != null) {
                g.draw(model.boundingBox())
            } else {
                val tileOutline = loc.outline()
                g.draw(tileOutline)
            }

            g.color = TEXT_COLOR
            val pos = npc.modelPosition.copy(height = npc.accessor.defaultHeight / 3)
            if (!pos.isLoaded) return@forEach
            val name = npc.type?.name?.substringBefore(' ') ?: "Impling"

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
        val def = npc.type ?: return false
        return settings.ids.contains(def.id)
    }

    private fun inPuroPuro(): Boolean {
        return Game.state == GameState.LOGGED_IN && LiveScene.regionIds.contains(PURO_PURO_REGION_ID)
    }

    private fun isLoggedIn(): Boolean {
        return Game.state == GameState.LOGGED_IN
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

        @Transient
        val ids: Set<Int> = with(ImmutableSet.builder<Int>()) {
            if (baby) add(NpcId.BABY_IMPLING_1635).add(NpcId.BABY_IMPLING_1645)
            if (young) add(NpcId.YOUNG_IMPLING_1636).add(NpcId.YOUNG_IMPLING_1646)
            if (gourmet) add(NpcId.GOURMET_IMPLING_1637).add(NpcId.GOURMET_IMPLING_1647)
            if (earth) add(NpcId.EARTH_IMPLING_1638).add(NpcId.EARTH_IMPLING_1648)
            if (essence) add(NpcId.ESSENCE_IMPLING_1639).add(NpcId.ESSENCE_IMPLING_1649)
            if (eclectic) add(NpcId.ECLECTIC_IMPLING_1640).add(NpcId.ECLECTIC_IMPLING_1650)
            if (nature) add(NpcId.NATURE_IMPLING_1641).add(NpcId.NATURE_IMPLING_1651)
            if (magpie) add(NpcId.MAGPIE_IMPLING_1642).add(NpcId.MAGPIE_IMPLING_1652)
            if (ninja) add(NpcId.NINJA_IMPLING_1643).add(NpcId.NINJA_IMPLING_1653)
            if (dragon) add(NpcId.DRAGON_IMPLING_1644).add(NpcId.DRAGON_IMPLING_1654)
            if (lucky) add(NpcId.LUCKY_IMPLING_7233).add(NpcId.LUCKY_IMPLING_7302)
            build()
        }
    }
}