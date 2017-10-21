package com.runesuite.client.plugins

import mu.KotlinLogging
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class Plugin<T : PluginSettings> {

    protected val logger = KotlinLogging.logger(javaClass.name)

    open val settingsWriter: ObjectReadWriter<T> = ObjectReadWriter.Yaml()

    abstract val defaultSettings: T

    private var _settings: T? = null

    val settings: T get() = _settings ?:
            throw IllegalStateException("settings is not initialized until after object construction")

    init {
        logger.debug("init")
    }

    @OverridingMethodsMustInvokeSuper
    open fun create() {
        logger.debug("create")
    }

    @OverridingMethodsMustInvokeSuper
    open fun start() {
        logger.debug("start")
    }

    @OverridingMethodsMustInvokeSuper
    open fun stop() {
        logger.debug("stop")
    }

    @OverridingMethodsMustInvokeSuper
    open fun destroy() {
        logger.debug("destroy")
    }
}