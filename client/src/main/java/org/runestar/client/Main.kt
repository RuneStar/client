@file:JvmName("Main")

package org.runestar.client

import io.reactivex.plugins.RxJavaPlugins
import org.kxtra.slf4j.error
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.warn
import org.runestar.client.api.Application
import org.runestar.client.api.LafInstallation
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.MANIFEST_NAME
import org.runestar.client.common.lookupClassLoader
import java.io.IOException
import java.util.jar.JarInputStream
import java.util.jar.Manifest
import javax.swing.SwingUtilities

fun main() {
    setupLogging()
    SwingUtilities.invokeLater(LafInstallation())

    checkUpToDate()

    Application.start()
}

private fun checkUpToDate() {
    val serverManifest = JarInputStream(JAV_CONFIG.gamepackUrl.openStream()).use { it.manifest }
    val bundledManifest = lookupClassLoader.getResourceAsStream(MANIFEST_NAME).use { Manifest(it) }
    if (serverManifest != bundledManifest) {
        throw IOException("Client is out of date")
    }
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