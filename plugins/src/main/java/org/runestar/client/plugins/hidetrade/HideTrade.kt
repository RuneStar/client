package org.runestar.client.plugins.hidetrade

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.MiniMenuOpcode
import org.runestar.client.game.api.live.MiniMenu
import org.runestar.client.plugins.spi.PluginSettings

class HideTrade : DisposablePlugin<PluginSettings>() {

    override val name = "Hide Trade"

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(MiniMenu.optionAdditions.subscribe { op ->
            if (op.opcode in MiniMenuOpcode.OP_PLAYER1_LOWPRIORITY..MiniMenuOpcode.OP_PLAYER8_LOWPRIORITY && op.action == "Trade with") {
                MiniMenu.optionsCount--
            }
        })
    }
}