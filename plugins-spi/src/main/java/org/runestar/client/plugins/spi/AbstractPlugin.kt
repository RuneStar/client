package org.runestar.client.plugins.spi

import org.kxtra.slf4j.getLogger
import org.slf4j.Logger

abstract class AbstractPlugin<T : PluginSettings> : Plugin<T> {

    protected lateinit var ctx: PluginContext
        private set

    protected lateinit var logger: Logger
        private set

    protected lateinit var settings: T
        private set

    override val name: String get() = javaClass.simpleName

    final override fun init(ctx: PluginContext) {
        this.ctx = ctx
        logger = getLogger(name)
        init()
    }

    protected open fun init() {}

    final override fun start(settings: T) {
        this.settings = settings
        start()
    }

    protected open fun start() {}

    override fun stop() {}
}