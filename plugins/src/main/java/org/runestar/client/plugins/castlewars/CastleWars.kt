package org.runestar.client.plugins.castlewars

import org.runestar.cache.generated.NpcId
import org.runestar.client.game.api.live.Npcs
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XNpcDefinition
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class CastleWars : DisposablePlugin<CastleWars.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Castle Wars"

    override fun start() {
        if (ctx.settings.drawBarricadesOnMinimap) {

            add(XNpcDefinition.init.exit.subscribe {
                val def = it.instance
                when (def.id) {
                    NpcId.BARRICADE, NpcId.BARRICADE_5723, NpcId.BARRICADE_5724, NpcId.BARRICADE_5725 -> {
                        def.drawMapDot = true
                    }
                }
            })

            resetNpcDefinitions()
        }
    }

    override fun stop() {
        super.stop()

        if (ctx.settings.drawBarricadesOnMinimap) {
            resetNpcDefinitions()
        }
    }

    private fun resetNpcDefinitions() {
        Client.accessor.npcDefinition_cached.clear()
        Npcs.forEach {
            it.accessor.definition = Client.accessor.getNpcDefinition(it.accessor.definition.id)
        }
    }

    class Settings(
            val drawBarricadesOnMinimap: Boolean = true
    ) : PluginSettings()
}