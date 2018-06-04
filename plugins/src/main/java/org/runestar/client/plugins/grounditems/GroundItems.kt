package org.runestar.client.plugins.grounditems

import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.api.util.FontForm
import org.runestar.client.api.util.RgbaForm
import org.runestar.client.api.util.drawStringShadowed
import org.runestar.client.game.api.GroundItem
import org.runestar.client.game.api.SceneElement
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.api.live.LiveViewport
import org.runestar.client.game.api.live.SceneElements
import org.runestar.client.game.raw.Client
import org.runestar.client.game.raw.access.XItemDefinition
import org.runestar.client.plugins.spi.PluginSettings
import org.runestar.general.fonts.RUNESCAPE_SMALL_FONT

class GroundItems : DisposablePlugin<GroundItems.Settings>() {

    override val defaultSettings = Settings()

    override val name = "Ground Items"

    val piles = LinkedHashSet<SceneElement.ItemPile>()

    val blockedIds = HashSet<Int>()
    val unblockedIds = HashSet<Int>()
    val blockRegexes = ArrayList<Regex>()

    override fun start() {
        ctx.settings.blockedNames.mapTo(blockRegexes) { it.toRegex() }

        piles.addAll(SceneElements.ItemPile.all())
        SceneElements.ItemPile.additions.subscribe { piles.add(it) }
        SceneElements.ItemPile.removals.subscribe { piles.remove(it) }
        SceneElements.clears.subscribe { piles.clear() }

        val defaultColor = ctx.settings.color.get()
        val font = ctx.settings.font.get()

        add(LiveCanvas.repaints.subscribe { g ->
            g.font = font
            g.clip(LiveViewport.shape)
            val height = g.fontMetrics.height

            val itr = piles.iterator()
            while (itr.hasNext()) {
                val pile = itr.next()
                if (pile.plane != Game.plane) continue
                val pt = pile.modelPosition.toScreen()
                if (pt == null || pt !in g.clip) continue
                val gis = pile.toList().asReversed()
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
                var y = pt.y - ctx.settings.initialOffset
                for ((def, count) in items) {
                    if (isBlocked(def, count)) {
                        continue
                    }
                    val string = itemToString(def, count)
                    val width = g.fontMetrics.stringWidth(string)
                    val leftX = x - (width / 2)

                    g.color = defaultColor
                    g.drawStringShadowed(string, leftX, y)

                    y -= height + ctx.settings.spacing

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
        piles.clear()
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