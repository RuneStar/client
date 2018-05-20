package org.runestar.client.plugins.dev

import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import java.awt.Color

class FriendsListDebug : DisposablePlugin<FriendsListDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        add(LiveCanvas.repaints.subscribe { g ->
            g.color = Color.WHITE
            g.font = RUNESCAPE_SMALL_FONT

            val x = 20
            var y = 40
            val lines = ArrayList<String>()

            when (ctx.settings.mode) {
                Mode.CLAN -> {
                    val cc = Game.clanChat ?: return@subscribe
                    lines.add("owner=${cc.owner}")
                    lines.add("name=${cc.name}")
                    lines.add("")
                    cc.mapTo(lines) { it.toString() }
                }
                Mode.FRIEND -> {
                    val fs = Game.friendsSystem.friendsList
                    fs.mapTo(lines) { it.toString() }
                }
                Mode.IGNORE -> {
                    val gs = Game.friendsSystem.ignoreList
                    gs.mapTo(lines) { it.toString() }
                }
            }
            for (line in lines) {
                g.drawString(line, x, y)
                y += g.font.size + 4
            }
        })
    }

    enum class Mode {
        CLAN, FRIEND, IGNORE
    }

    class Settings(
            val mode: Mode = Mode.FRIEND
    ) : PluginSettings()
}