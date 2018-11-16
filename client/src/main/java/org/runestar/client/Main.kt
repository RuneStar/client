@file:JvmName("Main")

package org.runestar.client

import io.reactivex.plugins.RxJavaPlugins
import org.kxtra.slf4j.logger.error
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.client.api.Application
import org.runestar.client.api.LafInstallation
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.REVISION
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    setupLogging()
    SwingUtilities.invokeLater(LafInstallation())

    if (JAV_CONFIG.revision != REVISION) throw Exception("Client is out of date")

    Application.start()
}

private fun setupLogging() {
    val logger = getLogger()
    RxJavaPlugins.setErrorHandler { e ->
        logger.warn(e) { "RxJavaPlugins error handler" }
    }
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        logger.error(e) { "Uncaught exception for thread ${t.name}" }
    }
}