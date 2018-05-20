package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client.accessor
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_CHAT_FONT
import java.awt.Color

class LoginDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.font = RUNESCAPE_CHAT_FONT
            g.color = Color.WHITE

            val strings = listOf(
                    "username: ${accessor.login_username}",
                    "isUsernameRemembered: ${accessor.login_isUsernameRemembered}",
                    "response0: ${accessor.login_response0}",
                    "response1: ${accessor.login_response1}",
                    "response2: ${accessor.login_response2}",
                    "response3: ${accessor.login_response3}"
            )
            val x = 20
            var y = 40
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += g.font.size + 5
            }
        })
    }
}