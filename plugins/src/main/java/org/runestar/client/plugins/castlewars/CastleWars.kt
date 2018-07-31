package org.runestar.client.plugins.castlewars

import com.google.common.base.Stopwatch
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.NpcId
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetGroupId
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.api.live.Widgets
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.game.raw.access.XNpcDefinition
import org.runestar.client.plugins.spi.PluginSettings
import java.time.Duration

class CastleWars : DisposablePlugin<CastleWars.Settings>() {

    private companion object {
        const val ZAM_CADE = NpcId.BARRICADE_5724
        const val ZAM_CADE_BURNING = NpcId.BARRICADE_5725
        const val SARA_CADE = NpcId.BARRICADE_5722
        const val SARA_CADE_BURNING = NpcId.BARRICADE_5723

        const val CADE_TIE_COLOR = 2396.toShort()

        val ONE_MINUTE: Duration = Duration.ofMinutes(1L)
    }

    override val defaultSettings = Settings()

    override val name = "Castle Wars"

    private lateinit var gameTimerMinutes: Duration

    private val gameTimerSeconds: Stopwatch = Stopwatch.createUnstarted()

    override fun start() {
        add(XNpcDefinition.init.exit.subscribe { onNpcDefinitionInit(it.instance) })
        resetNpcDefinitions()

        if (settings.showTimerSeconds) {
            add(XClient.drawWidgetsLoggedIn.enter.subscribe { onDrawWidgets() })
        }
    }

    private fun onNpcDefinitionInit(def: XNpcDefinition) {
        val id = def.id
        if (!isBarricade(id)) return
        if (settings.showBarricadesOnMinimap) {
            def.drawMapDot = true
        }
        if (settings.showBarricadeTeamColors) {
            val newTieColor = barricadeTieColor(id)
            if (def.recolorFrom == null) {
                def.recolorFrom = shortArrayOf(CADE_TIE_COLOR)
                def.recolorTo = shortArrayOf(newTieColor)
            } else {
                val i = def.recolorFrom.indexOf(CADE_TIE_COLOR)
                if (i == -1) {
                    def.recolorFrom = def.recolorFrom.plus(CADE_TIE_COLOR)
                    def.recolorTo = def.recolorTo.plus(newTieColor)
                } else {
                    def.recolorTo[i] = newTieColor
                }
            }
        }
    }

    private fun onDrawWidgets() {
        var w = Widgets[WidgetGroupId.CastleWarsZamorak.time_left]
        if (w == null || w.accessor.cycle == -1) {
            w = Widgets[WidgetGroupId.CastleWarsSaradomin.time_left] ?: return
        }
        w as Widget.Text
        val s = w.text ?: return
        if (s.endsWith(" Min")) {
            gameTimerSeconds.reset()
            val m = s.removeSuffix(" Min").toInt()
            gameTimerMinutes = if (m in 1..20) {
                gameTimerSeconds.start()
                Duration.ofMinutes(m.toLong())
            } else {
                // 0 when game is ending
                // 0 - 25 when game is starting
                Duration.ZERO
            }
        }
        val elapsed = gameTimerSeconds.elapsed().coerceAtMost(ONE_MINUTE)
        val time = gameTimerMinutes.minus(elapsed)
        val minutes = time.toMinutes()
        val seconds = time.seconds % 60
        w.text = "%d:%02d".format(minutes, seconds)
    }

    override fun stop() {
        super.stop()
        resetNpcDefinitions()
    }

    private fun isBarricade(npcId: Int): Boolean {
        return when (npcId) {
            SARA_CADE, SARA_CADE_BURNING, ZAM_CADE, ZAM_CADE_BURNING -> true
            else -> false
        }
    }

    private fun barricadeTieColor(npcId: Int): Short {
        return when (npcId) {
            ZAM_CADE, ZAM_CADE_BURNING -> ((254 shl 8) + 200).toShort()
            SARA_CADE, SARA_CADE_BURNING -> ((150 shl 8) + 200).toShort()
            else -> throw IllegalArgumentException(npcId.toString())
        }
    }

    private fun resetNpcDefinitions() {
        CLIENT.npcDefinition_cachedModels.clear()
        CLIENT.npcDefinition_cached.clear()
        Npcs.forEach {
            it.accessor.definition = CLIENT.getNpcDefinition(it.accessor.definition.id)
        }
    }

    class Settings(
            val showBarricadesOnMinimap: Boolean = true,
            val showBarricadeTeamColors: Boolean = true,
            val showTimerSeconds: Boolean = true
    ) : PluginSettings()
}