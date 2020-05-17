package org.runestar.client.plugins.collapsemenuentries

import com.google.common.collect.Maps
import org.runestar.client.api.plugins.DisposablePlugin
import org.runestar.client.api.game.MiniMenuOption
import org.runestar.client.api.game.live.MiniMenu
import org.runestar.client.raw.CLIENT
import org.runestar.client.raw.access.XClient
import org.runestar.client.api.plugins.PluginSettings

class CollapseMenuEntries : DisposablePlugin<CollapseMenuEntries.Settings>() {

    override val name = "Collapse Menu Entries"

    override val defaultSettings = Settings()

    override fun onStart() {
        XClient.updateInterface.exit
                .filter { it.arguments[1] == -1 && it.arguments[0] == CLIENT.interfaceComponents[CLIENT.rootInterface] }
                .subscribe { collapse() }
                .add()
    }

    private fun collapse() {
        val oldCount = MiniMenu.optionsCount
        if (oldCount <= 2) return
        val counts = Maps.newLinkedHashMapWithExpectedSize<MiniMenuOption, Int>(oldCount)
        for (i in (oldCount - 1).downTo(0)) {
            counts.compute(MiniMenu.getOption(i)) { _, count -> (count ?: 0) + 1 }
        }
        val newCount = counts.size
        if (oldCount == newCount) return
        MiniMenu.optionsCount = newCount
        var i = newCount - 1
        for ((menuOption, count) in counts) {
            MiniMenu.setOption(i, menuOption)
            if (count > 1) {
                CLIENT.menuTargetNames[i] += settings.prefix + count + settings.suffix
            }
            i--
        }
    }

    class Settings(
            val prefix: String = "<col=ffffff> x",
            val suffix: String = ""
    ) : PluginSettings()
}