package org.runestar.client.plugins.std.debug

import org.runestar.client.plugins.Plugin
import org.runestar.client.plugins.PluginSettings
import java.io.IOError
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class NashornEngine : Plugin<PluginSettings>() {

    companion object {
        const val PROMPT = "> "
    }

    override val defaultSettings = PluginSettings()

    lateinit var engine: ScriptEngine

    @Volatile
    var running = false

    override fun create() {
        super.create()
        engine = ScriptEngineManager().getEngineByName("nashorn")
    }

    override fun start() {
        super.start()
        running = true
        val console = System.console()
        if (console == null) {
            logger.warn("No console found")
            return
        }

        Thread({
            val bindings = engine.createBindings()
            while (running) {
                print(PROMPT)
                try {
                    val input = console.readLine() ?: return@Thread
                    println(engine.eval(input, bindings))
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