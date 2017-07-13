package com.runesuite.client.plugins

import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.hunterwb.kxtra.swing.TextLayout
import com.hunterwb.kxtra.swing.drawTextLayout
import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XItemDefinition
import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.GlobalTile
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Game
import com.runesuite.client.game.live.Viewport
import java.awt.geom.Point2D
import java.awt.geom.Rectangle2D
import java.util.AbstractMap
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashMap
import kotlin.collections.component1
import kotlin.collections.component2
import com.runesuite.client.game.live.GroundItems as LiveGroundItems

class GroundItems : DisposablePlugin<GroundItems.Settings>() {

    override val defaultSettings = Settings()

    private val tiles = HashSet<GlobalTile>()

    private val definitions = CacheBuilder.newBuilder()
            .build(object : CacheLoader<Int, XItemDefinition>() {
                override fun load(key: Int): XItemDefinition {
                    return Client.accessor.getItemDefinition(key)
                }
            })

    override fun start() {
        super.start()
        tiles.clear()

        add(LiveGroundItems.pileRemovals.subscribe {
            tiles.remove(it.toGlobalTile())
        })
        add(LiveGroundItems.pileChanges.subscribe {
            tiles.add(it.toGlobalTile())
        })

        val shadowColor = settings.shadow.color.get()
        val font = settings.font.get()
        val color = settings.color.get()

        add(Canvas.Live.repaints.subscribe { g ->
            g.color = color
            g.font = font

            val itr = tiles.iterator()
            var gTile: GlobalTile
            val viewportShape = Viewport.Live.shape
            while (itr.hasNext()) {
                gTile = itr.next()
                if (gTile.plane != Game.plane) {
                    itr.remove()
                    continue
                }
                val sTile = gTile.toSceneTile()
                if (!sTile.isLoaded) {
                    itr.remove()
                    continue
                }
                val tilePt = sTile.center.toScreen()
                if (tilePt !in viewportShape) {
                    continue
                }
                val items = LiveGroundItems.getAt(sTile)
                if (items.isEmpty()) {
                    itr.remove()
                    continue
                }

                val itemEntries = if (settings.collapseIdDuplicates) {
                    val itemsCollapsed = LinkedHashMap<Int, Int>()
                    items.asReversed().forEach {
                        itemsCollapsed.compute(it.id) { _, v -> (v ?: 0) + it.quantity }
                    }
                    itemsCollapsed.entries.asSequence()
                } else {
                    items.asReversed().asSequence().map { AbstractMap.SimpleEntry(it.id, it.quantity)  }
                }

                var currY = tilePt.getY() - settings.initialOffsetY
                val centerX = tilePt.getX()

                for ((id, quantity) in itemEntries) {
                    val def = definitions[id]
                    val itemText = TextLayout("${def.name} x $quantity", g)
                    val stringDim = itemText.bounds
                    currY -= stringDim.height + settings.spacingY
                    val stringRect = Rectangle2D.Double(
                            centerX - stringDim.width / 2,
                            currY,
                            stringDim.width,
                            stringDim.height
                    )
                    if (stringRect !in viewportShape) {
                        break
                    }
                    if (settings.shadow.active) {
                        val col = g.color
                        g.color = shadowColor
                        val drawShadowPt = Point2D.Double(
                                stringRect.x - 1,
                                stringRect.y + stringRect.height + 1
                        )
                        g.drawTextLayout(itemText, drawShadowPt)
                        g.color = col
                    }
                    val drawPt = Point2D.Double(stringRect.x, stringRect.y + stringRect.height)
                    g.drawTextLayout(itemText, drawPt)
                }
            }
        })
    }

    class Settings: Plugin.Settings() {

        val font = AwtFont()
        val color = AwtColor(255, 255, 255)
        val shadow = Shadow()
        val initialOffsetY = 13.0
        val spacingY = 4.0

        val collapseIdDuplicates = true

        class Shadow {
            val active = true
            val color = AwtColor()
        }
    }
}