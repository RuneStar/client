@file:JvmName("AwtSystem")

package org.runestar.client.api.util

import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import java.awt.Desktop
import java.awt.SystemTray
import java.nio.file.Files
import java.nio.file.Path

private val logger = getLogger()

val desktop: Desktop? get() {
    return if (Desktop.isDesktopSupported()) {
        try {
            Desktop.getDesktop()
        } catch (e: Exception) {
            logger.warn(e) { "Error getting desktop" }
            null
        }
    } else {
        logger.warn { "Desktop is unsupported" }
        null
    }
}

fun Desktop.safeOpen(path: Path) {
    if (isSupported(Desktop.Action.OPEN)) {
        try {
            open(path.toFile())
        } catch (e: Exception) {
            logger.warn(e) { "Desktop failed to open $path" }
            if (Files.isRegularFile(path)) {
                val dir = path.parent
                logger.info { "Desktop opening parent directory $dir" }
                safeOpen(dir)
            }
        }
    } else {
        logger.warn { "Desktop open is not supported" }
    }
}

val systemTray: SystemTray? get() {
    return if (SystemTray.isSupported()) {
        try {
            SystemTray.getSystemTray()
        } catch (e: Exception) {
            logger.warn(e) { "Error getting system tray" }
            null
        }
    } else {
        logger.warn { "System tray is not supported" }
        null
    }
}