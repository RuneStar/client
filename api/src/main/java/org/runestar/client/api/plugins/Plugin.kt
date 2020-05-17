package org.runestar.client.api.plugins

interface Plugin<T : PluginSettings> {

    val name: String

    val defaultSettings: T

    fun init(ctx: PluginContext)

    fun start(settings: T)

    fun stop()
}