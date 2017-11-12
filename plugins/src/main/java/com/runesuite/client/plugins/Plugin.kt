package com.runesuite.client.plugins

import org.kxtra.slf4j.loggerfactory.getLogger
import org.slf4j.LoggerFactory
import java.nio.file.Path
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class Plugin<T : PluginSettings> {

    val logger = getLogger(javaClass)

    open val settingsWriter: ObjectReadWriter<T> = ObjectReadWriter.Yaml()

    abstract val defaultSettings: T

    private lateinit var _settings: T

    val settings get() = _settings

    private lateinit var _directory: Path

    val directory get() = _directory

    @OverridingMethodsMustInvokeSuper
    open fun create() {
        logger.info("create")
    }

    @OverridingMethodsMustInvokeSuper
    open fun start() {
        logger.info("start")
    }

    @OverridingMethodsMustInvokeSuper
    open fun stop() {
        logger.info("stop")
    }

    @OverridingMethodsMustInvokeSuper
    open fun destroy() {
        logger.info("destroy")
    }
}