package org.runestar.client.plugins

import com.google.errorprone.annotations.OverridingMethodsMustInvokeSuper
import org.kxtra.slf4j.loggerfactory.getLogger
import java.nio.file.Path

abstract class Plugin<T : PluginSettings> {

    @JvmField
    val logger = getLogger(javaClass)

    abstract val defaultSettings: T

    /**
     * Should not be modified. Any modifications will not be saved.
     */
    lateinit var settings: T
        internal set

    lateinit var directory: Path
        internal set

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