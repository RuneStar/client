package org.runestar.client.plugins.std.debug

import com.google.common.base.Splitter
import org.kxtra.slf4j.logger.info
import org.runestar.client.game.api.live.LiveCanvas
import org.runestar.client.game.raw.Client
import org.runestar.client.plugins.PluginSettings
import org.runestar.client.utils.DisposablePlugin

class VarpsChangeDebug : DisposablePlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    val varps = Client.accessor.varps

    @Volatile
    var old = varps.copyOf()

    override fun start() {
        super.start()

        add(LiveCanvas.repaints.subscribe { g ->
            val curr = varps.copyOf()
            for (i in old.indices) {
                val o = old[i]
                val c = curr[i]
                if (o != c) {
                    val changedBits = Integer.toBinaryString(o xor c).padStart(Integer.SIZE, '0')
                    val lastBitChanged = Integer.SIZE - changedBits.lastIndexOf('1') - 1
                    val firstBitChanged = Integer.SIZE - changedBits.indexOf('1') - 1

                    logger.info { "$i: $firstBitChanged - $lastBitChanged\n" +
                            "${intToString(o)} ->\n" +
                            "${intToString(c)}" }
                }
            }

            old = curr
        })
    }

    val splitter8 = Splitter.fixedLength(8)

    fun intToString(n: Int): String {
        val b = Integer.toBinaryString(n).padStart(Integer.SIZE, '0')
        return splitter8.splitToList(b).joinToString("'")
    }
}