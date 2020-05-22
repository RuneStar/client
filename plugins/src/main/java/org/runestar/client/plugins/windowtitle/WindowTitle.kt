package org.runestar.client.plugins.windowtitle

import org.runestar.client.api.Application
import org.runestar.client.api.TITLE
import org.runestar.client.api.game.live.Game
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.plugins.PluginSettings
import org.runestar.client.raw.CLIENT
import javax.swing.SwingUtilities

class WindowTitle : DisposablePlugin<WindowTitle.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Window Title"

    private var lastName: String? = ""

    override fun onStart() {
        add(Game.ticks.startWithItem(Unit).subscribe {
            val name = CLIENT.localPlayerName
            if (name == lastName) return@subscribe
            lastName = name
            val title = if (name == null) {
                settings.titleNoAccount
            } else {
                settings.title.format(name)
            }
            setTitle(title)
        })
    }

    override fun onStop() {
        setTitle(TITLE)
        lastName = ""
    }

    private fun setTitle(title: String) {
        SwingUtilities.invokeLater {
            Application.frame.title = title
            Application.trayIcon?.toolTip = title
        }
    }

    data class Settings(
            val titleNoAccount: String = TITLE,
            val title: String = "%s - $TITLE"
    ) : PluginSettings()
}