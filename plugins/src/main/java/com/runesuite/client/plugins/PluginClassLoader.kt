package com.runesuite.client.plugins

import org.kxtra.slf4j.logger.info
import org.kxtra.slf4j.logger.warn
import org.kxtra.slf4j.loggerfactory.getLogger
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.net.URL
import java.nio.file.Path
import java.util.jar.JarFile

internal class PluginClassLoader
private constructor(jar: Path) : ClassLoader() {

    companion object {

        fun load(jar: Path): Collection<Plugin<*>> = PluginClassLoader(jar).plugins

        private val logger = getLogger()
    }

    private val plugins = ArrayList<Plugin<*>>()

    private var classBytes: Map<String, ByteArray> = jarClassBytes(jar)

    init {
        classBytes.keys.forEach { name ->
            logger.info { "Found class $name" }
            val c: Class<*>
            try {
                c = loadClass(name)
            } catch (e: ClassNotFoundException) {
                logger.warn(e) { "Failed to load class $name" }
                return@forEach
            }

            if (!Modifier.isAbstract(c.modifiers) && Plugin::class.java.isAssignableFrom(c)) {
                logger.info { "Found plugin ${c.name}" }
                val constructor: Constructor<*>
                try {
                    constructor = c.getDeclaredConstructor()
                } catch (e: Exception) {
                    logger.warn(e) { "Failed to get no-argument constructor for ${c.name}" }
                    return@forEach
                }
                val plugin: Plugin<*>
                try {
                    plugin = constructor.newInstance() as Plugin<*>
                } catch (e: Exception) {
                    logger.warn(e) { "Failed to create instance of ${c.name}" }
                    return@forEach
                }
                plugins.add(plugin)
            }
        }
        classBytes = emptyMap()
    }

    override fun findClass(name: String): Class<*>? {
        val bytes = classBytes[name] ?: throw ClassNotFoundException(name)
        try {
            return defineClass(name, bytes, 0, bytes.size)
        } catch (e: Exception) {
            throw ClassNotFoundException(name, e)
        }
    }

    override fun findResource(name: String): URL? {
        return null // todo
    }

    private fun jarClassBytes(jar: Path): Map<String, ByteArray> {
        val classes = HashMap<String, ByteArray>()
        JarFile(jar.toFile()).use { jarFile ->
            jarFile.stream().filter { !it.isDirectory && it.name.endsWith(".class") }.forEach { entry ->
                val className = entry.name.removeSuffix(".class").replace('/', '.')
                jarFile.getInputStream(entry).use { input ->
                    classes[className] = input.readBytes()
                }
            }
        }
        return classes
    }
}