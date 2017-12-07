package com.runesuite.client

import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import java.awt.Desktop
import java.nio.file.Files
import java.nio.file.Path

private val desktop = if (Desktop.isDesktopSupported()) Desktop.getDesktop() else null

private val isOpenFileSupported = desktop != null && desktop.isSupported(Desktop.Action.OPEN)

private val logger = getLogger()

internal fun openFile(path: Path) {
    if (desktop != null && isOpenFileSupported) {
        try {
            desktop.open(path.toFile())
        } catch (e: Exception) {
            logger.warn(e) { "failed to open $path" }
            if (Files.isRegularFile(path)) {
                val dir = path.parent
                logger.info { "opening parent directory $dir" }
                openFile(dir)
            }
        }
    } else {
        logger.warn("file open is not supported")
    }
}