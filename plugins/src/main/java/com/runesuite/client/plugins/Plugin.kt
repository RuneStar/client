package com.runesuite.client.plugins

import mu.KotlinLogging
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class Plugin<T : PluginSettings> {

    protected val logger = KotlinLogging.logger(javaClass.name)

    open val settingsWriter: ObjectReadWriter<T> = ObjectReadWriter.Yaml()

    abstract val defaultSettings: T

    lateinit var settings: T
        private set

    init {
        logger.debug("init")
    }

    @OverridingMethodsMustInvokeSuper
    open fun start() {
        logger.debug("start")
    }

    @OverridingMethodsMustInvokeSuper
    open fun stop() {
        logger.debug("stop")
    }
}