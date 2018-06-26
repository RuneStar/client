package org.runestar.client.plugins.castlewars

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.NpcId
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.raw.access.XNpcDefinition
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings

class CastleWars : DisposablePlugin<CastleWars.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Castle Wars"

    override fun start() {
        if (settings.drawBarricadesOnMinimap) {

            add(XNpcDefinition.init.exit.subscribe {
                val def = it.instance
                when (def.id) {
                    NpcId.BARRICADE_5722, NpcId.BARRICADE_5723, NpcId.BARRICADE_5724, NpcId.BARRICADE_5725 -> {
                        def.drawMapDot = true
                    }
                }
            })

            resetNpcDefinitions()
        }
    }

    override fun stop() {
        super.stop()

        if (settings.drawBarricadesOnMinimap) {
            resetNpcDefinitions()
        }
    }

    private fun resetNpcDefinitions() {
        CLIENT.npcDefinition_cached.clear()
        Npcs.forEach {
            it.accessor.definition = CLIENT.getNpcDefinition(it.accessor.definition.id)
        }
    }

    class Settings(
            val drawBarricadesOnMinimap: Boolean = true
    ) : PluginSettings()
}