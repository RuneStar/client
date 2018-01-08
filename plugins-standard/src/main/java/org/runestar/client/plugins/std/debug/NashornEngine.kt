package org.runestar.client.plugins.std.debug

import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings
import java.io.*
import javax.script.ScriptEngine
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
            System.`in`.bufferedReader().use { reader ->
                System.out.bufferedWriter().use { writer ->
                    val engine = ScriptEngineManager().getEngineByName("nashorn")
                    while (running) {
                        writer.write(PROMPT)
                        try {
                            val input = reader.readLine() ?: return@Thread
                            writer.write(engine.eval(input).toString())
                        } catch (se: ScriptException) {
                            writer.write(se.toString())
                        }
                    }
                }
            }
        }).start()
    }

    override fun stop() {
        super.stop()
        running = false
    }
}