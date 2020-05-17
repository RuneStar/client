package org.runestar.client.plugins.dev

import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.Fonts
import org.runestar.client.api.game.live.Canvas
import org.runestar.client.raw.CLIENT
import org.runestar.client.api.plugins.PluginSettings
import java.awt.Color

class LoginDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun onStart() {
        add(Canvas.repaints.subscribe { g ->
            g.font = Fonts.PLAIN_12
            g.color = Color.WHITE

            val strings = listOf(
                    "username: ${CLIENT.login_username}",
//                    "isUsernameRemembered: ${CLIENT.login_isUsernameRemembered}",
                    "response0: ${CLIENT.login_response0}",
                    "response1: ${CLIENT.login_response1}",
                    "response2: ${CLIENT.login_response2}",
                    "response3: ${CLIENT.login_response3}"
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