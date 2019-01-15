package org.runestar.client.plugins.hidetrade

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.game.api.MenuOptionOpcode
import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.spi.PluginSettings

class HideTrade : DisposablePlugin<PluginSettings>() {

    override val name = "Hide Trade"

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Menu.optionAdditions.subscribe { op ->
            if (op.opcode in MenuOptionOpcode.PLAYER_ACTION_0_LOW_PRIORITY..MenuOptionOpcode.PLAYER_ACTION_7_LOW_PRIORITY && op.action == "Trade with") {
                Menu.optionsCount--
            }
        })
    }
}