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
import java.io.IOException
import java.net.InetAddress
import java.net.Socket
import java.nio.ByteBuffer
import javax.swing.SwingUtilities

fun main(args: Array<String>) {
    setupLogging()
    SwingUtilities.invokeLater(LafInstallation())

    if (isOutOfDate()) throw Exception("Client is out of date")

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

@Throws(IOException::class)
fun isOutOfDate(): Boolean {
    val rev = REVISION
    val address = InetAddress.getByName(JAV_CONFIG.codebase.host)
    Socket(address, 43594).use { socket ->
        socket.outputStream.write(ByteBuffer.allocate(5).put(15).putInt(rev).array())
        socket.outputStream.flush()
        return socket.inputStream.read() != 0
    }
}