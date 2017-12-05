package com.runesuite.client.plugins

import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import java.io.InputStream
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.nio.file.Path

internal class PluginClassLoader
private constructor(jar: Path) : ClassLoader() {

    companion object {

        fun load(jar: Path): Collection<Plugin<*>> = PluginClassLoader(jar).plugins

        private val logger = getLogger()
    }

    private val plugins = ArrayList<Plugin<*>>()

    private val files = jarFileBytes(jar)

    init {
        val classNames = ArrayList<String>()
        files.keys.forEach { fileName ->
            if (fileName.endsWith(".class")) {
                val className = fileName.removeSuffix(".class").replace('/', '.')
                logger.info { "Found class $className" }
                classNames.add(className)
            } else {
                logger.info { "Found resource $fileName" }
            }
        }
        classNames.forEach { name ->
            val c: Class<*>
            try {
                c = loadClass(name)
            } catch (e: ClassNotFoundException) {
                logger.warn(e) { "Failed to load class $name" }
                return@forEach
            }

            if (!Modifier.isAbstract(c.modifiers) && Plugin::class.java.isAssignableFrom(c)) {
                logger.info { "Found plugin $name" }
                val constructor: Constructor<*>
                try {
                    constructor = c.getDeclaredConstructor()
                } catch (e: Exception) {
                    logger.warn(e) { "Failed to get no-argument constructor for $name" }
                    return@forEach
                }
                val plugin: Plugin<*>
                try {
                    plugin = constructor.newInstance() as Plugin<*>
                } catch (e: Exception) {
                    logger.warn(e) { "Failed to create instance of $name" }
                    return@forEach
                }
                plugins.add(plugin)
            }
        }
    }

    override fun findClass(name: String): Class<*>? {
        val fileName = name.replace('.', '/').plus(".class")
        val bytes = files[fileName] ?: throw ClassNotFoundException(name)
        try {
            return defineClass(name, bytes, 0, bytes.size)
        } catch (e: Exception) {
            throw ClassNotFoundException(name, e)
        }
    }

    override fun getResourceAsStream(name: String): InputStream? {
        return parent.getResourceAsStream(name) ?: files[name]?.inputStream()
    }
}