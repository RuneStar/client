package com.runesuite.client.plugins

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

internal class PluginHolder<T : PluginSettings>(
        val plugin: Plugin<T>,
        val watchKey: WatchKey
) {

    companion object {
        const val SETTINGS_FILE_NAME = "plugin.settings"
        private val settingsField = Plugin::class.java.getDeclaredField("_settings").apply {
            isAccessible = true
        }
        private val directoryField = Plugin::class.java.getDeclaredField("_directory").apply {
            isAccessible = true
        }
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
        directoryField.set(plugin, directory)
    }

    private fun tryWrite(file: Path, value: T) {
        try {
            ignoreNextEvent = true
            logger.debug("Writing settings...")
            plugin.settingsWriter.write(file, value)
            logger.debug("Write successful.")
        } catch (e: IOException) {
            logger.error(e) { "Write failed." }
            safeDestroyPlugin()
        }
    }

    fun create() {
        createSettings()
        safeTryStartPlugin()
    }

    private fun createSettings() {
        if (Files.exists(settingsFile)) {
            logger.debug("Settings file exists. Reading...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                settingsField.set(plugin, readSettings)
                logger.debug("Read successful.")
            } catch (e: IOException) {
                logger.warn(e) { "Read failed. Reverting to default settings." }
                settingsField.set(plugin, plugin.defaultSettings)
                tryWrite(settingsFile, plugin.settings)
            }
        } else {
            logger.debug("Settings file does not exist. Using default settings.")
            settingsField.set(plugin, plugin.defaultSettings)
            tryWrite(settingsFile, plugin.settings)
        }
    }

    fun settingsFileChanged() {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        safeTryStopPlugin()
        if (Files.notExists(settingsFile)) {
            logger.debug("Settings file missing. Switching to default settings.")
            settingsField.set(plugin, plugin.defaultSettings)
            tryWrite(settingsFile, plugin.defaultSettings)
        } else {
            logger.debug("Settings file modified. Reading new settings...")
            try {
                val readSettings = plugin.settingsWriter.read(settingsFile, plugin.defaultSettings.javaClass)
                logger.debug("Read successful.")
                settingsField.set(plugin, readSettings)
            } catch (e: IOException) {
                logger.warn(e) { "Read failed." }
                tryWrite(settingsFile, plugin.settings)
            }
        }
        safeTryStartPlugin()
    }

    fun destroy() {
        watchKey.cancel()
        safeDestroyPlugin()
    }

    private fun safeCreatePlugin() {
        if (created || destroyed) return
        try {
            plugin.create()
            created = true
        } catch (e: Exception) {
            logger.error(e) { "Exception creating plugin." }
        }
    }

    private fun safeTryStartPlugin() {
        if (destroyed || active || !plugin.settings.active) return
        try {
            if (!created) safeCreatePlugin()
            plugin.start()
            active = true
        } catch (e: Throwable) {
            logger.error(e) { "Exception starting plugin." }
            safeDestroyPlugin()
        }
    }

    private fun safeTryStopPlugin() {
        if (destroyed || !created || !active || !plugin.settings.active) return
        active = false
        try {
            plugin.stop()
        } catch (e: Throwable) {
            logger.error(e) { "Exception stopping plugin." }
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
            logger.error(e) { "Exception destroying plugin." }
        }
    }

    private fun addIndividualFileLogger() {
        val lblogger = logger.underlyingLogger as Logger
        if (lblogger.getAppender("plugin-individual") != null) return

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