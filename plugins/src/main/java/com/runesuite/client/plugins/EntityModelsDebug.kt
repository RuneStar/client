package com.runesuite.client.plugins

import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.live.*
import com.runesuite.client.game.live.GroundItems
import java.awt.Color

class EntityModelsDebug : DisposablePlugin<EntityModelsDebug.Settings>() {

    override val defaultSettings = Settings()

    override fun start() {
        super.start()
        val s = settings
        if (s.npcs || s.players || s.projectiles || s.items) {
            add(Canvas.Live.repaints.subscribe { g ->
                if (s.npcs) {
                    g.color = Color.RED
                    Npcs.all.forEach { npc ->
                        npc.model?.let { model ->
                            model.trianglesToScreen().forEach { tri ->
                                tri.takeIf { it.bounds in Canvas.Live.shape }?.let { g.draw(it) }
                            }
                        }
                    }
                }
                if (s.players) {
                    g.color = Color.BLUE
                    Players.all.forEach { players ->
                        players.model?.let { model ->
                            model.trianglesToScreen().forEach { tri ->
                                tri.takeIf { it.bounds in Canvas.Live.shape }?.let { g.draw(it) }
                            }
                        }
                    }
                }
                if (s.projectiles) {
                    g.color = Color.GREEN
                    Projectiles.all.forEach { proj ->
                        proj.model?.let { model ->
                            model.trianglesToScreen().forEach { tri ->
                                tri.takeIf { it.bounds in Canvas.Live.shape }?.let { g.draw(it) }
                            }
                        }
                    }
                }
                if (s.items) {
                    g.color = Color.PINK
                    GroundItems.getOnPlane(Game.plane).flatMap { it.flatMap { it } }.forEach { gi ->
                        gi.model?.let { model ->
                            model.trianglesToScreen().forEach { tri ->
                                tri.takeIf { it.bounds in Canvas.Live.shape }?.let { g.draw(it) }
                            }
                        }
                    }
                }
            })
        }
    }

    class Settings : Plugin.Settings() {
        val npcs = false
        val players = false
        val projectiles = false
        val items = false
    }
}