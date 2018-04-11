package org.runestar.client.plugins.spi

abstract class AbstractPlugin<T : PluginSettings> : Plugin<T> {

    abstract override fun start()

    abstract override fun stop()

    override val name: String = javaClass.simpleName

    lateinit var ctx: PluginContext<T>
        private set

    override fun init(ctx: PluginContext<T>) {
        this.ctx = ctx
    }
}