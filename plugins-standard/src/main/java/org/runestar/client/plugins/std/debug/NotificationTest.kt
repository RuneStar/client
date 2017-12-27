package org.runestar.client.plugins.std.debug

import org.runestar.client.api.trayIcon
import org.runestar.client.game.api.live.Menu
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import java.awt.TrayIcon

class NotificationTest : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        super.start()

        add(Menu.actions.subscribe { a ->
            trayIcon.displayMessage(
                    null,
                    a.toString(),
                    TrayIcon.MessageType.INFO
            )
        })
    }
}