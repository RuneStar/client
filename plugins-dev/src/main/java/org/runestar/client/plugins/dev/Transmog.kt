package org.runestar.client.plugins.dev

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.raw.access.XPlayer
import org.runestar.client.game.raw.CLIENT
import org.runestar.client.plugins.spi.PluginSettings

class Transmog : DisposablePlugin<Transmog.Settings>() {

    override val defaultSettings = Settings()

    override fun onStart() {
        add(Game.ticks.subscribe {
            Players.local?.accessor?.let { transmog(it, settings.npcId) }
        })
    }

    override fun onStop() {
        Players.local?.appearance?.accessor?.npcTransformId = -1
    }

    private fun transmog(player: XPlayer, npcId: Int) {
        val appearance = player.appearance ?: return
        val def = CLIENT.getNPCType(npcId) ?: return
        appearance.npcTransformId = npcId
        player.walkSequence = def.walkanim
        player.walkBackSequence = def.walkbackanim
        player.walkLeftSequence = def.walkleftanim
        player.walkRightSequence = def.walkrightanim
        player.readySequence = def.readyanim
        player.turnLeftSequence = def.turnleftanim
        player.turnRightSequence = def.turnrightanim

        // npcs can't run
        player.runSequence = def.walkanim
    }

    class Settings(
            val npcId: Int = 7530
    ) : PluginSettings()
}