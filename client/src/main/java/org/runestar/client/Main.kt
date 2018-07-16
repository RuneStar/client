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
import javax.swing.SwingUtilities

private lateinit var javConfig: JavConfig

fun main(args: Array<String>) {
    setupLogging()
    SwingUtilities.invokeLater(LafInstallation())

    javConfig = JavConfig.load(System.getProperty("runestar.world", ""))
    updateRevision(javConfig.codebase.host)

    Application.start(javConfig)
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

class PatchedClientProvider : ClientProvider {

    override fun get(): XClient {
        return patchGamePack(javConfig).loadClass(javConfig.initialClass).getDeclaredConstructor().newInstance() as XClient
    }

    private fun patchGamePack(javConfig: JavConfig): ClassLoader {
        val tmpdir = tmpdir()
        val patchedGamepackPath = tmpdir.resolve("${codeSourceLastModifiedMillis()}.zip")
        if (!verifyJar(patchedGamepackPath)) {
            val gamepackPath = tmpdir.resolve("runescape-gamepack.$revision.jar")
            if (!verifyJar(gamepackPath)) {
                downloadFile(javConfig.gamepackUrl, gamepackPath)
            }
            patch(gamepackPath, patchedGamepackPath)
        }
        return URLClassLoader(patchedGamepackPath)
    }
}