package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.Viewport
import com.runesuite.client.game.raw.Client
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color
import java.awt.Font

class ClientPreferencesDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.SANS_SERIF, Font.BOLD, 15)

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            val p = Client.accessor.clientPreferences ?: return@subscribe
            val strings = listOf(
                    "titleMusicDisabled: ${p.titleMusicDisabled}",
                    "roofsHidden: ${p.roofsHidden}",
                    "hideUsername: ${p.hideUsername}",
                    "windowMode: ${p.windowMode}",
                    "rememberedUsername: ${p.rememberedUsername}",
                    "parameters: ${p.parameters}"
            )
            g.font = font
            g.color = Color.WHITE
            val x = 20
            var y = 40
            strings.forEach { s ->
                g.drawString(s, x, y)
                y += 20
            }
        })
    }
}