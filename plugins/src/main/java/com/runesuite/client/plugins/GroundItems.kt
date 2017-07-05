package com.runesuite.client.plugins

import com.google.common.base.Suppliers
import com.google.common.cache.CacheBuilder
import com.google.common.cache.CacheLoader
import com.runesuite.client.base.Client
import com.runesuite.client.base.access.XItemDefinition
import com.runesuite.client.dev.plugins.DisposablePlugin
import com.runesuite.client.dev.plugins.Plugin
import com.runesuite.client.game.GlobalTile
import com.runesuite.client.game.live.Canvas
import com.runesuite.client.game.live.Game
import com.runesuite.client.game.live.Viewport
import com.runesuite.general.rsbuddy.Summary
import java.awt.Color
import java.awt.Font
import java.awt.Point
import java.util.AbstractMap
import java.util.concurrent.TimeUnit
import java.util.function.Supplier
import kotlin.collections.HashSet
import kotlin.collections.LinkedHashMap
import kotlin.collections.component1
import kotlin.collections.component2
import com.runesuite.client.game.live.GroundItems as LiveGroundItems

class GroundItems : DisposablePlugin<GroundItems.Settings>() {

    override val defaultSettings = Settings()

    private val tiles = HashSet<GlobalTile>()

    private lateinit var osbuddySummary: Supplier<Summary>

    private val definitions = CacheBuilder.newBuilder()
            .build(object : CacheLoader<Int, XItemDefinition>() {
                override fun load(key: Int): XItemDefinition {
                    return Client.accessor.getItemDefinition(key)
                }
            })

    override fun start() {
        super.start()
        tiles.clear()
        osbuddySummary = Suppliers.memoizeWithExpiration({ Summary() },
                settings.osbuddyExchange.refreshTimeMinutes.toLong(), TimeUnit.MINUTES)

        add(LiveGroundItems.pileRemovals.subscribe {
            tiles.remove(it.toGlobalTile())
        })
        add(LiveGroundItems.pileChanges.subscribe {
            tiles.add(it.toGlobalTile())
        })

        val font = Font.decode("${settings.font.name}-${settings.font.style}-${settings.font.size}")
        val shadowColor = Color(settings.shadow.colorRgb)

        add(Canvas.Live.repaints.subscribe { g ->
            g.color = Color.WHITE
            g.font = font
            val frc = g.fontRenderContext

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

                var currY = tilePt.y - settings.initialOffsetY
                val centerX = tilePt.x

                for ((id, quantity) in itemEntries) {
                    val itemSb = StringBuilder()
                    if (settings.names) {
                        val def = definitions[id]
                        itemSb.append(def.name)
                    }
                    if (settings.ids) {
                        itemSb.append(" <").append(id).append('>')
                    }
                    if (settings.quantities) {
                        itemSb.append(" (").append(quantity).append(')')
                    }
                    if (settings.osbuddyExchange.active) {
                        val summaryItem = osbuddySummary.get()[id]
                        if (summaryItem != null) {
                            itemSb.append(" ex: ").append(summaryItem.price)
                        }
                    }

                    val itemString = itemSb.toString()
                    val stringRect = font.getStringBounds(itemString, frc).bounds
                    currY -= stringRect.height + settings.spacingY
                    stringRect.y = currY
                    stringRect.x = centerX - stringRect.width / 2
                    if (stringRect !in viewportShape) {
                        break
                    }
                    if (settings.shadow.active) {
                        val col = g.color
                        g.color = shadowColor
                        g.drawString(itemString, stringRect.x + settings.shadow.offset.x,
                                stringRect.y + stringRect.height + settings.shadow.offset.y)
                        g.color = col
                    }
                    g.drawString(itemString, stringRect.x, stringRect.y + stringRect.height)
                }
            }
        })
    }

    class Settings: Plugin.Settings() {

        val font = FontDecode()
        val shadow = Shadow()
        val initialOffsetY = 15
        val spacingY = 0

        val names = true
        val ids = false
        val quantities = true
        val collapseIdDuplicates = true
        val osbuddyExchange = OSBuddyExchange()

        class FontDecode {
            val name = Font.DIALOG
            val style = "plain"
            val size = 12
        }

        class Shadow {
            val active = true
            val colorRgb = Color.BLACK.rgb
            val offset = Point(-1, 1)
        }

        class OSBuddyExchange {
            val active = true
            val refreshTimeMinutes = 30
        }
    }
}