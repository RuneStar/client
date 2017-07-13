package com.runesuite.client.dev.plugins

import com.hunterwb.kxtra.swing.Font
import mu.KotlinLogging
import java.awt.Color
import java.awt.Font
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchEvent
import java.util.function.Supplier
import javax.annotation.OverridingMethodsMustInvokeSuper

abstract class Plugin<T : Plugin.Settings> {

    open val settingsWriter: ObjectReadWriter<T> = ObjectReadWriter.Yaml()

    protected val logger = KotlinLogging.logger(javaClass.name)

    lateinit var loader: PluginLoader

    abstract val defaultSettings: T

    private val settingsFile: Path by lazy { loader.settingsDirectory.resolve(javaClass.name + ".txt") }

    @Volatile
    protected lateinit var settings: T
        private set

    private fun tryWrite(file: Path, type: Class<T>, value: T) {
        try {
            ignoreNextEvent = true
            settingsWriter.write(file, type, value)
        } catch (e: IOException) {
            logger.error(e) { "Write failed. Destroying." }
            destroy()
        }
    }

    fun create() {
        logger.debug { "Creating" }
        if (Files.exists(settingsFile)) {
            try {
                settings = settingsWriter.read(settingsFile, defaultSettings.javaClass)
            } catch (e: IOException) {
                logger.warn(e) { "Read failed. Reverting to default settings." }
                settings = defaultSettings
                tryWrite(settingsFile, defaultSettings.javaClass, settings)
            }
        } else {
            settings = defaultSettings
            tryWrite(settingsFile, defaultSettings.javaClass, settings)
        }
        if (settings.active) {
            start()
        }
    }

    @OverridingMethodsMustInvokeSuper
    open fun start() {
        logger.debug { "Starting" }
    }

    @OverridingMethodsMustInvokeSuper
    open fun stop() {
        logger.debug { "Stopping" }
    }

    fun destroy() {
        logger.debug { "Destroying" }
        if (settings.active) {
            stop()
        }
    }

    @Volatile
    private var ignoreNextEvent = false

    fun settingsFileChanged(event: WatchEvent.Kind<Path>) {
        if (ignoreNextEvent) {
            // ignore events caused by this class writing
            ignoreNextEvent = false
            return
        }
        when (event) {
            StandardWatchEventKinds.ENTRY_CREATE -> {
                logger.warn { "Create event. Should have been ignored or never happened." }
            }
            StandardWatchEventKinds.ENTRY_DELETE -> {
                tryWrite(settingsFile, defaultSettings.javaClass, settings)
            }
            StandardWatchEventKinds.ENTRY_MODIFY -> {
                if (settings.active) {
                    stop()
                }
                try {
                    settings = settingsWriter.read(settingsFile, defaultSettings.javaClass)
                } catch (e: IOException) {
                    logger.warn(e) { "Read failed. Writing current settings." }
                    tryWrite(settingsFile, defaultSettings.javaClass, settings)
                }
                if (settings.active) {
                    start()
                }
            }
            else -> logger.warn { "Unknown event: $event" }
        }
    }

    open class Settings {
        val active = false

        data class AwtFont(
                val name: String = Font.DIALOG,
                val style: String = "plain",
                val size: Float = 12f
        ) : Supplier<Font> {
            override fun get() = Font(name, style, size)
        }

        data class AwtColor(
                val r: Int = 0,
                val g: Int = 0,
                val b: Int = 0,
                val a: Int = 255
        ) : Supplier<Color> {
            override fun get() = Color(r, g, b, a)
        }
    }
}