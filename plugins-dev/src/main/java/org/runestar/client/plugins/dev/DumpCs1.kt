package org.runestar.client.plugins.dev

import org.runestar.client.game.raw.CLIENT
import org.runestar.client.game.raw.access.XArchive
import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings
import java.nio.file.Files

class DumpCs1 : AbstractPlugin<PluginSettings>() {

    override val defaultSettings = PluginSettings()

    override fun start() {
        val file = ctx.directory.resolve("cs1.txt")
        val s = StringBuilder()
        val cache = CLIENT.component_archive as XArchive
        for (a in 0 until cache.groupCount) {
            if (!cache.validGroups[a]) continue
            while (!cache.tryLoadGroup(a)) {
                Thread.sleep(1000)
            }
            for (r in 0 until cache.fileCounts[a]) {
                val bytes = cache.takeFile(a, r)
                if (bytes.first().toInt() != -1) {
                    val w = CLIENT._Component_()
                    w.decodeLegacy(CLIENT._Packet_(bytes))
                    if (w.cs1Comparisons != null && w.cs1Comparisons.isNotEmpty()) {
                        for (i in 0 until w.cs1Comparisons.size) {
                            s.append("$a\t$r\t$i\t")
                            s.append(w.cs1Comparisons[i])
                            s.append("\t")
                            s.append(w.cs1ComparisonValues[i])
                            s.append("\t")
                            w.cs1Instructions[i].joinTo(s, separator = "\t")
                            s.append("\n")
                        }
                    }
                }
            }
        }
        Files.write(file, s.toString().toByteArray())
    }
}