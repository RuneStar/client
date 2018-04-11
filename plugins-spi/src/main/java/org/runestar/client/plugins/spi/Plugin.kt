package org.runestar.client.plugins.spi

interface Plugin<T : PluginSettings> {

    val name: String

    val defaultSettings: T

    fun init(ctx: PluginContext<T>)

    fun start()

    fun stop()
}