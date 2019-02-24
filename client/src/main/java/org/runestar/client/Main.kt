@file:JvmName("Main")

package org.runestar.client

import com.alee.managers.style.StyleId
import com.google.common.base.Throwables
import io.reactivex.plugins.RxJavaPlugins
import org.kxtra.slf4j.error
import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.warn
import org.runestar.client.api.Application
import org.runestar.client.api.LafInstallation
import org.runestar.client.common.JAV_CONFIG
import org.runestar.client.common.MANIFEST_NAME
import org.runestar.client.common.lookupClassLoader
import java.util.jar.JarInputStream
import java.util.jar.Manifest
import javax.swing.JOptionPane
import javax.swing.JScrollPane
import javax.swing.JTextArea
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
        showErrorDialog("Client is out of date")
        System.exit(1)
    }
}

private fun setupLogging() {
    val logger = getLogger()
    RxJavaPlugins.setErrorHandler { e ->
        logger.warn(e) { "RxJavaPlugins error handler" }
    }
    Thread.setDefaultUncaughtExceptionHandler { thread, exception ->
        logger.error(exception) { "Uncaught exception" }
        showErrorDialog(Throwables.getStackTraceAsString(exception))
    }
}

private fun showErrorDialog(s: String) {
    val runnable = Runnable {
        val component = JScrollPane().apply {
            setViewportView(JTextArea(s, 5, 40).apply {
                isEditable = false
            })
            putClientProperty(StyleId.STYLE_PROPERTY, StyleId.scrollpaneUndecoratedButtonless)
        }
        JOptionPane.showMessageDialog(null, component, "Error", JOptionPane.ERROR_MESSAGE)
    }

    if (SwingUtilities.isEventDispatchThread()) {
        runnable.run()
    } else {
        SwingUtilities.invokeAndWait(runnable)
    }
}