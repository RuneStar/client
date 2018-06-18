@file:JvmName("Main")

package org.runestar.client

import io.reactivex.plugins.RxJavaPlugins
import org.kxtra.slf4j.logger.error
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import org.runestar.client.api.Application
import org.runestar.client.api.LafInstallation
import org.runestar.client.game.raw.ClientProvider
import org.runestar.client.game.raw.access.XClient
import org.runestar.client.patch.patch
import org.runestar.general.JavConfig
import org.runestar.general.revision
import org.runestar.general.updateRevision
import java.lang.invoke.MethodHandles
import java.nio.file.Files
import java.nio.file.Paths
import javax.swing.SwingUtilities

private val logger = getLogger()

private val javConfig: JavConfig = JavConfig.load(System.getProperty("runestar.world", ""))

fun main(args: Array<String>) {
    setupLogging()
    SwingUtilities.invokeLater(LafInstallation)

    updateRevision(javConfig.codebase.host)

    Application.start(javConfig)
}

class ClientProviderImpl : ClientProvider {
    override fun get(): XClient {
        return patchGamePack(javConfig).loadClass(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
    }
}

private fun setupLogging() {
    RxJavaPlugins.setErrorHandler { e ->
        logger.warn(e) { "RxJavaPlugins error handler" }
    }
    Thread.setDefaultUncaughtExceptionHandler { t, e ->
        logger.error(e) { "Uncaught exception for thread ${t.name}" }
    }
}

private fun patchGamePack(javConfig: JavConfig): ClassLoader {
    val tmpdir = Paths.get(System.getProperty("java.io.tmpdir"))
    val patchedGamepackPath = tmpdir.resolve("${clientVersion()}.zip")
    if (!verifyJar(patchedGamepackPath)) {
        val gamepackPath = tmpdir.resolve("runescape-gamepack.$revision.jar")
        if (!verifyJar(gamepackPath)) {
            downloadFile(javConfig.gamepackUrl, gamepackPath)
        }
        patch(gamepackPath, patchedGamepackPath)
    }
    return URLClassLoader(patchedGamepackPath)
}

private fun clientVersion(): String {
    val klass = MethodHandles.lookup().lookupClass()
    val codeSource = klass.protectionDomain.codeSource ?: return System.currentTimeMillis().toString()
    val file = Paths.get(codeSource.location.toURI())
    return Files.getLastModifiedTime(file).toMillis().toString()
}