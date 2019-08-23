package org.runestar.client.plugins.boosts

import org.runestar.client.api.Fonts
import org.runestar.client.api.forms.FontForm
import org.runestar.client.api.forms.InsetsForm
import org.runestar.client.api.overlay.Anchor
import org.runestar.client.api.overlay.ColoredString
import org.runestar.client.api.overlay.TextTableOverlay
import org.runestar.client.api.overlay.withBackground
import org.runestar.client.api.overlay.withBorder
import org.runestar.client.api.overlay.withPadding
import org.runestar.client.api.util.DisposablePlugin
import org.runestar.client.cacheids.StatId
import org.runestar.client.game.api.live.Game
import org.runestar.client.game.api.live.Stats
import org.runestar.client.plugins.spi.PluginSettings
import java.awt.Color
import java.util.TreeMap
import kotlin.math.absoluteValue

class Boosts : DisposablePlugin<Boosts.Settings>() {

    override val defaultSettings = Settings()

    override fun onStart() {
        val map = TreeMap<String, ColoredString>()
        val table = TextTableOverlay(map, Color.WHITE, settings.font.value, settings.lineSpacing, settings.dividingWidth)
        val overlay = table
                .withPadding(settings.padding)
                .withBackground()
                .withBorder()
        add(settings.anchor.add(overlay))
        val stats = Stats.ids.filter { !ignoreStat(it) }.toIntArray()
        val boosts = IntArray(stats.size)
        add(Game.ticks.subscribe {
            stats.forEachIndexed { i, stat ->
                val boost = Stats.boost(stat)
                if (boosts[i] == boost) return@forEachIndexed
                boosts[i] = boost
                val statName = Stats.name(stat)
                table.modified = true
                if (boost == 0) {
                    map.remove(statName)
                } else {
                    map[statName] = ColoredString(boost.absoluteValue.toString(), if (boost > 0) Color.GREEN else Color.RED)
                }
            }
        })
    }

    private fun ignoreStat(stat: Int): Boolean {
        return when (stat) {
            StatId.HITPOINTS, StatId.PRAYER -> true
            StatId.ATTACK, StatId.STRENGTH, StatId.DEFENCE, StatId.RANGED, StatId.MAGIC -> false
            else -> settings.combatOnly
        }
    }

    class Settings(
            val font: FontForm = FontForm(Fonts.PLAIN_12),
            val lineSpacing: Int = 0,
            val dividingWidth: Int = 5,
            val padding: InsetsForm = InsetsForm(1, 2, 1, 2),
            val combatOnly: Boolean = true,
            val anchor: Anchor = Anchor.TOP_LEFT
    ) : PluginSettings()
}