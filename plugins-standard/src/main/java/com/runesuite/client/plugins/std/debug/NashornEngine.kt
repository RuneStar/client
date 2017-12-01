package com.runesuite.client.plugins.std.debug

import com.runesuite.client.plugins.Plugin
import com.runesuite.client.plugins.PluginSettings
import java.io.BufferedReader
import java.io.InputStreamReader
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

        Thread({
            val bindings = engine.createBindings()
            BufferedReader(InputStreamReader(System.`in`)).use { br ->
                print(PROMPT)
                while (running) {
                    try {
                        val input = br.readLine() ?: return@Thread
                        println(engine.eval(input, bindings))
                    } catch (se: ScriptException) {
                        println(se)
                    } finally {
                        print(PROMPT)
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