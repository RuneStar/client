package org.runestar.client.plugins.std.grounditems

import com.google.common.collect.Lists
import org.runestar.client.game.api.GroundItem
import org.runestar.client.game.api.SceneTile
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XItemDefinition
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin
import org.runestar.client.utils.FontForm
import org.runestar.client.utils.RgbaForm
import org.runestar.client.utils.drawStringShadowed
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT
import org.runestar.client.game.api.live.GroundItems as LiveGroundItems

class GroundItems : DisposablePlugin<GroundItems.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Ground Items"

    val tiles = LinkedHashSet<SceneTile>()

    val blockedIds = HashSet<Int>()
    val unblockedIds = HashSet<Int>()
    val blockRegexes = ArrayList<Regex>()

    override fun start() {
        super.start()
        settings.blockedNames.mapTo(blockRegexes) { it.toRegex() }
        LiveGroundItems.onPlane(Game.plane).forEach { gi ->
            tiles.add(gi.location)
        }
        add(LiveGroundItems.pileChanges.subscribe { st ->
            tiles.add(st)
        })
        add(LiveGroundItems.pileRemovals.subscribe { st ->
            tiles.remove(st)
        })

        val defaultColor = settings.color.get()
        val font = settings.font.get()

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = font
            g.clip(LiveViewport.shape)
            val height = g.fontMetrics.height

            val itr = tiles.iterator()
            while (itr.hasNext()) {
                val tile = itr.next()
                if (tile.plane != Game.plane) continue
                val pt = tile.center.toScreen()
                if (pt == null || pt !in g.clip) continue
                val gis = LiveGroundItems.at(tile).toList().asReversed()
                if (gis.isEmpty()) {
                    itr.remove()
                    continue
                }
                val items = LinkedHashMap<XItemDefinition, Int>()
                for (gi in gis) {
                    val def = Client.accessor.getItemDefinition(gi.id)
                    if (def != null) {
                        items.merge(def, gi.quantity) { old, new -> old + new }
                    }
                }
                val x = pt.x
                var y = pt.y - settings.initialOffset
                for ((def, count) in items) {
                    if (isBlocked(def, count)) {
                        continue
                    }
                    val string = itemToString(def, count)
                    val width = g.fontMetrics.stringWidth(string)
                    val leftX = x - (width / 2)

                    g.color = defaultColor
                    g.drawStringShadowed(string, leftX, y)

                    y -= height + settings.spacing

                }
            }
        })
    }

    fun itemToString(def: XItemDefinition, count: Int): String {
        val name = def.name
        return when {
            count == 1 -> name
            count >= GroundItem.MAX_QUANTITY -> "$name x Lots!"
            else -> "$name x $count"
        }
    }

    fun isBlocked(def: XItemDefinition, count: Int): Boolean {
        val id = def.id
        val name = def.name
        return if (id in blockedIds) {
            true
        } else if (id in unblockedIds) {
            false
        } else {
            val blocked = blockRegexes.any { it.matches(name) }
            if (blocked) {
                blockedIds.add(id)
                true
            } else {
                unblockedIds.add(id)
                false
            }
        }
    }

    override fun stop() {
        super.stop()
        tiles.clear()
        blockedIds.clear()
        unblockedIds.clear()
        blockRegexes.clear()
    }

    data class Settings(
            val color: RgbaForm = RgbaForm(255, 255, 255),
            val font: FontForm = FontForm(RUNESCAPE_SMALL_FONT),
            val spacing: Int = -2,
            val initialOffset: Int = 9,
            val blockedNames: List<String> = emptyList()
    ) : PluginSettings()
}