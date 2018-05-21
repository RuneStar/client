@file:JvmName("Main")

package org.runestar.client

import com.google.common.hash.Hashing
import io.reactivex.plugins.RxJavaPlugins
import org.kxtra.slf4j.logger.error
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.client.api.Application
import org.runestar.client.api.LafInstallation
import org.runestar.client.injector.inject
import org.runestar.client.updater.hooksFile
import org.runestar.general.JavConfig
import org.runestar.general.revision
import org.runestar.general.updateRevision
import java.nio.file.Paths
import javax.swing.SwingUtilities

private val logger = getLogger()

fun main(args: Array<String>) {
    setupLogging()
    SwingUtilities.invokeLater(LafInstallation)

    val javConfig = JavConfig.load(System.getProperty("runestar.world", ""))

    updateRevision(javConfig.codebase.host)

    Application.start(javConfig, injectGamepack(javConfig))
}

private fun setupLogging() {
    RxJavaPlugins.setErrorHandler { e ->
        logger.warn(e) { "RxJavaPlugins error handler" }
    }
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        logger.error(e) { "Uncaught exception for thread ${t.name}" }
    }
}

private fun injectGamepack(javConfig: JavConfig): ClassLoader {
    val tmpdir = Paths.get(System.getProperty("java.io.tmpdir"))
    val injectedGamepackPath = tmpdir.resolve("$revision-${clientVersion()}.zip")
    if (!verifyJar(injectedGamepackPath)) {
        val gamepackPath = tmpdir.resolve("runescape-gamepack.$revision.jar")
        if (!verifyJar(gamepackPath)) {
            downloadFile(javConfig.gamepackUrl, gamepackPath)
        }
        inject(gamepackPath, injectedGamepackPath)
    }
    return URLClassLoader(injectedGamepackPath)
}

private fun clientVersion(): String {
    return Hashing.crc32().hashBytes(hooksFile.readBytes()).asInt().toString() // todo
}