package com.runesuite.client.plugins

import mu.KotlinLogging
import java.nio.file.Path
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class Plugin<T : PluginSettings> {

    val logger = KotlinLogging.logger(javaClass.name)

    open val settingsWriter: ObjectReadWriter<T> = ObjectReadWriter.Yaml()

    abstract val defaultSettings: T

    private lateinit var _settings: T

    val settings get() = _settings

    private lateinit var _directory: Path

    val directory get() = _directory

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