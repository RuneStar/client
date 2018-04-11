package org.runestar.client.plugins.dev

import org.runestar.client.plugins.spi.AbstractPlugin
import org.runestar.client.plugins.spi.PluginSettings
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class NashornEngine : AbstractPlugin<PluginSettings>() {

    companion object {
        const val PROMPT = "> "
    }

    override val defaultSettings = PluginSettings()

    @Volatile
    var running = false

    override fun start() {
        running = true
        Thread({
            val reader = System.`in`.bufferedReader()
            val engine = ScriptEngineManager().getEngineByName("nashorn")
            while (running) {
                println(PROMPT)
                try {
                    val input = reader.readLine() ?: return@Thread
                    println(engine.eval(input))
                } catch (se: ScriptException) {
                    println(se)
                }
            }
        }).start()
    }

    override fun stop() {
        running = false
    }
}