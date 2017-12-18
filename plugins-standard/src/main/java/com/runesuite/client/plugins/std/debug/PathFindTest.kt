package com.runesuite.client.plugins.std.debug

import com.runesuite.client.game.api.live.*
import com.runesuite.client.plugins.PluginSettings
import com.runesuite.client.plugins.utils.DisposablePlugin
import java.awt.Color

class PathFindTest : DisposablePlugin<PathFindTest.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val pathfinder = when (settings.type) {
            Type.WALK -> PathFinding.walking
            Type.PROJECTILE -> PathFinding.projectile
        }

        add(LiveCanvas.repaints.subscribe { g ->

            g.color = Color.BLUE

            val mouseTile = Projections.viewport.toGame(Mouse.location)?.sceneTile ?: return@subscribe
            val selfTile = Players.local?.location ?: return@subscribe

            val path = pathfinder.findPath(selfTile, mouseTile) ?: return@subscribe

            path.forEach { st ->
                g.draw(st.outline())
            }
        })
    }

    enum class Type {
        WALK, PROJECTILE
    }

    class Settings : PluginSettings() {
        val type = Type.WALK
    }
}