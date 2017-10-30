package com.runesuite.client.plugins.std.debug

import com.hunterwb.kxtra.swing.graphics2d.drawString
import com.runesuite.client.game.api.live.Canvas
import com.runesuite.client.game.api.live.Npcs
import com.runesuite.client.game.api.live.Players
import com.runesuite.client.game.raw.Client
import com.runesuite.client.game.raw.access.XClient
import com.runesuite.client.game.raw.access.XEvictingHashTable
import com.runesuite.client.game.raw.access.XModel
import com.runesuite.client.game.raw.access.XNpcDefinition
import com.runesuite.client.plugins.DisposablePlugin
import com.runesuite.client.plugins.PluginSettings
import java.awt.Color
import java.awt.Font

class TestPlugin : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val font = Font(Font.SANS_SERIF, Font.BOLD, 15)

    override fun start() {
        super.start()
        add(Canvas.Live.repaints.subscribe { g ->
            g.color = Color.WHITE
            g.font = font
            Npcs.all.forEach { n ->
                n.definition.let { def ->
                    val pt = n.position.toScreen()
                    
                }
            }
        })
    }
}