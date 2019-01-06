@file:JvmName("AwtSystem")

package org.runestar.client.api.util

import org.kxtra.slf4j.getLogger
import org.kxtra.slf4j.info
import org.kxtra.slf4j.warn
import java.awt.*
import java.nio.file.Files
import java.nio.file.Path

private val logger = getLogger()

private inline fun <T> safe(f: () -> T): T? {
    return try {
        f()
    } catch (e: Exception) {
        logger.warn(e) { "" }
        null
    }
}

fun safeTrayIcon(image: Image, tooltip: String) = safe { TrayIcon(image, tooltip) }

val taskbar: Taskbar? get() = safe { Taskbar.getTaskbar() }

fun Taskbar.safeSetWindowProgressValue(w: Window, value: Int) {
    safe { setWindowProgressValue(w, value) }
}

fun Taskbar.safeSetWindowProgressState(w: Window, state: Taskbar.State) {
    safe { setWindowProgressState(w, state) }
}

fun Taskbar.safeRequestWindowUserAttention(w: Window) {
    safe { requestWindowUserAttention(w) }
}

val desktop: Desktop? get() = safe { Desktop.getDesktop() }

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

val systemTray: SystemTray? get() = safe { SystemTray.getSystemTray() }