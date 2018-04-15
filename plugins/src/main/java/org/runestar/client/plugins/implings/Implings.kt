package org.runestar.client.plugins.implings

import com.google.common.collect.ImmutableSet
import org.runestar.cache.generated.NpcId
import org.runestar.client.game.api.Npc
import org.runestar.client.game.api.Region
import org.runestar.client.game.api.live.*
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class Implings : DisposablePlugin<Implings.Settings>() {

    private companion object {
        val PURO_PURO_REGION = Region(40, 67, 0)
    }

    override val defaultSettings = Settings()

    @Volatile
    private lateinit var implingIds: Set<Int>

    @Volatile
    private var implings: List<Npc> = emptyList()

    override fun start() {
        implingIds = ctx.settings.ids()
        if (implingIds.isNotEmpty()) {
            add(Game.ticks.subscribe {
                implings = Npcs.filter { it.isImpling() }
            })
            add(LiveCanvas.repaints.subscribe { g ->
                val npcs = implings
                if (npcs.isEmpty()) return@subscribe
                g.color = Color.WHITE
                g.font = RUNESCAPE_CHAT_FONT
                npcs.forEach { npc ->
                    val pos = npc.position
                    if (!pos.isLoaded) return@forEach
                    val name: String = npc.accessor.definition?.name ?: return@forEach

                    val mmPt = pos.toScreen(Projections.minimap)
                    if (mmPt != null) {
                        g.drawStringShadowed(name, mmPt.x, mmPt.y)
                    }

                    val pt = pos.toScreen(Projections.viewport)
                    if (pt != null) {
                        g.drawStringShadowed(name, pt.x, pt.y)
                    }
                }
            })
        }
        if (ctx.settings.drawMinimapInPuroPuro) {
            add(Game.ticks.subscribe {
                if (inPuroPuro() && !LiveMinimap.isDrawn) {
                    LiveMinimap.isDrawn = true
                }
            })
        }
    }

    override fun stop() {
        super.stop()
        if (ctx.settings.drawMinimapInPuroPuro && inPuroPuro() && LiveMinimap.isDrawn) {
            LiveMinimap.isDrawn = false
        }
    }

    private fun Npc.isImpling(): Boolean {
        val def = definition ?: return false
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
            val eclectic: Boolean = false,
            val nature: Boolean = false,
            val magpie: Boolean = false,
            val ninja: Boolean = false,
            val dragon: Boolean = true,
            val lucky: Boolean = true
    ) : PluginSettings() {

        fun ids(): Set<Int> {
            val b = ImmutableSet.builder<Int>()
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