package com.runesuite.client.plugins

import ch.qos.logback.classic.Level
import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.LoggerContext
import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.rolling.FixedWindowRollingPolicy
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeBasedTriggeringPolicy
import ch.qos.logback.core.util.FileSize
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.WatchKey

class PluginHolder<T : PluginSettings>(
        private val plugin: Plugin<T>,
        private val watchKey: WatchKey
) {

    companion object {
        const val SETTINGS_FILE_NAME = "plugin.settings"
    }

    private val directory = watchKey.watchable() as Path

    private val logger = plugin.logger

    private val settingsFile = directory.resolve(SETTINGS_FILE_NAME)

    private var ignoreNextEvent = false

    private var active = false

    private var created = false

    private var destroyed = false

    init {
        addIndividualFileLogger()
        plugin.directory = directory
    }

    private fun tryWrite(file: Path, value: T) {
        try {
            ignoreNextEvent = true
            logger.info("Writing settings...")
            plugin.settingsWriter.write(file, value)
            logger.info("Write successful.")
        } catch (e: IOException) {
            logger.warn("Write failed.", e)
            safeDestroyPlugin()
        }
    }

    internal fun create() {
        createSettings()
        safeTryStartPlugin()
    }

    private fun createSettings() {
        if (Files.exists(settingsFile)) {
            logger.info("Settings file exists. Reading...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                plugin.settings = readSettings
                logger.info("Read successful.")
            } catch (e: IOException) {
                logger.warn("Read failed. Reverting to default settings.", e)
                plugin.settings = plugin.defaultSettings
                tryWrite(settingsFile, plugin.settings)
            }
        } else {
            logger.info("Settings file does not exist. Using default settings.")
            plugin.settings = plugin.defaultSettings
            tryWrite(settingsFile, plugin.settings)
        }
    }

    internal fun settingsFileChanged() {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        safeTryStopPlugin()
        if (Files.notExists(settingsFile)) {
            logger.info("Settings file missing. Switching to default settings.")
            plugin.settings = plugin.defaultSettings
            tryWrite(settingsFile, plugin.defaultSettings)
        } else {
            logger.info("Settings file modified. Reading new settings...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                logger.info("Read successful.")
                plugin.settings = readSettings
            } catch (e: IOException) {
                logger.warn("Read failed.", e)
                tryWrite(settingsFile, plugin.settings)
            }
        }
        safeTryStartPlugin()
    }

    internal fun destroy() {
        watchKey.cancel()
        safeDestroyPlugin()
    }

    private fun safeCreatePlugin() {
        if (created || destroyed) return
        try {
            plugin.create()
            created = true
        } catch (e: Exception) {
            logger.warn("Exception creating plugin.", e)
        }
    }

    private fun safeTryStartPlugin() {
        if (destroyed || active || !plugin.settings.enabled) return
        try {
            if (!created) safeCreatePlugin()
            plugin.start()
            active = true
        } catch (e: Throwable) {
            logger.warn("Exception starting plugin.", e)
            safeDestroyPlugin()
        }
    }

    private fun safeTryStopPlugin() {
        if (destroyed || !created || !active || !plugin.settings.enabled) return
        active = false
        try {
            plugin.stop()
        } catch (e: Throwable) {
            logger.warn("Exception stopping plugin.", e)
            safeDestroyPlugin()
        }
    }

    private fun safeDestroyPlugin() {
        safeTryStopPlugin()
        if (destroyed || !created) return
        destroyed = true
        try {
            plugin.destroy()
        } catch (e: Exception) {
            logger.warn("Exception destroying plugin.", e)
        }
    }

    private fun addIndividualFileLogger() {
        val lblogger = logger as Logger
        if (lblogger.getAppender("plugin-individual") != null) return

        lblogger.level = Level.ALL

        val logCtx = LoggerFactory.getILoggerFactory() as LoggerContext

        val logEncoder = PatternLayoutEncoder()
        logEncoder.context = logCtx
        logEncoder.pattern = "%date{ISO8601} [%thread] %-5level - %msg%n"
        logEncoder.start()

        val logFileAppender = RollingFileAppender<ILoggingEvent>()
        logFileAppender.context = logCtx
        logFileAppender.name = "plugin-individual"
        logFileAppender.encoder = logEncoder
        logFileAppender.isAppend = true
        logFileAppender.file = directory.resolve("plugin.log").toString()

        val rollingPolicy = FixedWindowRollingPolicy()
        rollingPolicy.minIndex = 0
        rollingPolicy.maxIndex = 0
        rollingPolicy.context = logCtx
        rollingPolicy.setParent(logFileAppender)
        rollingPolicy.fileNamePattern = directory.resolve("plugin%i.log").toString()
        rollingPolicy.start()

        val triggeringPolicy = SizeBasedTriggeringPolicy<ILoggingEvent>()
        triggeringPolicy.setMaxFileSize(FileSize.valueOf("2MB"))
        triggeringPolicy.context = logCtx
        triggeringPolicy.start()

        logFileAppender.rollingPolicy = rollingPolicy
        logFileAppender.triggeringPolicy = triggeringPolicy
        logFileAppender.start()

        lblogger.addAppender(logFileAppender)
    }
}