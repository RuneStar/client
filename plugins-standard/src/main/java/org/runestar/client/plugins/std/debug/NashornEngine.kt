package org.runestar.client.plugins.std.debug

import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class NashornEngine : Plugin<PluginSettings>() {

    companion object {
        const val PROMPT = "> "
    }

    override val defaultSettings = PluginSettings()

    @Volatile
    var running = false

    override fun start() {
        super.start()
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
        super.stop()
        running = false
    }
}