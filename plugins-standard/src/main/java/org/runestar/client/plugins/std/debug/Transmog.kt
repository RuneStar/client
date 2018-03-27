package org.runestar.client.plugins.std.debug

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Players
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XPlayer
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class Transmog : DisposablePlugin<Transmog.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()

        add(Game.ticks.subscribe {
            Players.local?.accessor?.let { transmog(it, settings.npcId) }
        })
    }

    override fun stop() {
        super.stop()

        Players.local?.appearance?.accessor?.npcTransformId = -1
    }

    private fun transmog(player: XPlayer, npcId: Int) {
        val appearance = player.appearance ?: return
        val def = Client.accessor.getNpcDefinition(npcId) ?: return
        appearance.npcTransformId = npcId
        // todo: multipliers are broken for these
//        player.walkSequence = def.walkSequence
//        player.walkSequenceA = def.walkSequenceA
//        player.walkSequenceB = def.walkSequenceB
//        player.idleSequence = def.idleSequence
//        player.turnSequence = def.turnSequence
//        player.turnLeftSequence = def.turnLeftSequence
//        player.turnRightSequence = def.turnRightSequence
        // npcs can't run
    }

    class Settings(
            val npcId: Int = 403
    ) : PluginSettings()
}