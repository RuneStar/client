package org.runestar.client.plugins.castlewars

import com.google.common.base.Stopwatch
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.HslColor
import org.runestar.client.game.api.NpcDefinition
import org.runestar.client.game.api.NpcId
import org.runestar.client.game.api.Widget
import org.runestar.client.game.api.WidgetId
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

        val DEFAULT_CADE_TIE_COLOR = HslColor(2396)
        val ZAM_CADE_TIE_COLOR = HslColor.fromRgb(1.0, 0.0, 0.0)
        val SARA_CADE_TIE_COLOR = HslColor.fromRgb(0.1, 0.5, 1.0)

        val ONE_MINUTE: Duration = Duration.ofMinutes(1L)
    }

    override val defaultSettings = Settings()

    override val name = "Castle Wars"

    private var gameTimerMinutes: Duration = Duration.ZERO

    private val gameTimerSeconds: Stopwatch = Stopwatch.createUnstarted()

    override fun onStart() {
        add(XNpcDefinition.init.exit.subscribe { onNpcDefinitionInit(NpcDefinition(it.instance)) })
        resetNpcDefinitions()

        if (settings.showTimerSeconds) {
            add(XClient.drawLoggedIn.enter.subscribe { onDrawWidgets() })
        }
    }

    private fun onNpcDefinitionInit(def: NpcDefinition) {
        if (!isBarricade(def.id)) return
        if (settings.showBarricadesOnMinimap) {
            def.drawMapDot = true
        }
        if (settings.showBarricadeTeamColors) {
            def.recolor(DEFAULT_CADE_TIE_COLOR, barricadeTieColor(def.id))
        }
    }

    private fun onDrawWidgets() {
        var w = Widgets.getAs<Widget.Text>(WidgetId.CASTLE_WARS_ZAMORAK_TIME_LEFT)
        if (w == null || w.accessor.cycle == -1) {
            w = Widgets.getAs(WidgetId.CASTLE_WARS_SARADOMIN_TIME_LEFT) ?: return
        }
        val s = w.text ?: return
        if (s.endsWith(" Min")) {
            val m = s.removeSuffix(" Min").toInt()
            val minutes = Duration.ofMinutes(m.toLong())
            if (gameTimerMinutes != minutes) {
                gameTimerSeconds.reset()
                if (m != 0) {
                    gameTimerSeconds.start()
                }
                gameTimerMinutes = minutes
            }
        }
        val elapsed = gameTimerSeconds.elapsed().coerceAtMost(ONE_MINUTE)
        val time = gameTimerMinutes.minus(elapsed)
        val minutes = time.toMinutes()
        val seconds = time.seconds % 60
        w.text = "%d:%02d".format(minutes, seconds)
    }

    override fun onStop() {
        resetNpcDefinitions()
    }

    private fun isBarricade(npcId: Int): Boolean {
        return when (npcId) {
            SARA_CADE, SARA_CADE_BURNING, ZAM_CADE, ZAM_CADE_BURNING -> true
            else -> false
        }
    }

    private fun barricadeTieColor(npcId: Int): HslColor {
        return when (npcId) {
            ZAM_CADE, ZAM_CADE_BURNING -> ZAM_CADE_TIE_COLOR
            SARA_CADE, SARA_CADE_BURNING -> SARA_CADE_TIE_COLOR
            else -> error(npcId)
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