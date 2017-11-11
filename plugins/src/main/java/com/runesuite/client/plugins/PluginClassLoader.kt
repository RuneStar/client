package com.runesuite.client.plugins

import mu.KotlinLogging
import java.lang.reflect.Constructor
import java.lang.reflect.Modifier
import java.net.URL
import java.nio.file.Path
import java.util.jar.JarFile

internal class PluginClassLoader
private constructor(jar: Path) : ClassLoader() {

    companion object {
        fun load(jar: Path): Collection<Plugin<*>> {
            return PluginClassLoader(jar).plugins
        }
    }

    private val plugins: Collection<Plugin<*>>

    private val logger = KotlinLogging.logger { }

    init {
        val classes = ArrayList<Class<*>>()
        val plugins = ArrayList<Plugin<*>>()
        jarClassBytes(jar).forEach { (name, bytes) ->
            val c: Class<*>
            try {
                c = defineClass(name, bytes, 0, bytes.size)
                logger.info("Defined class $name")
            } catch (e: Exception) {
                logger.warn("Failed to define class $name")
                return@forEach
            }
            resolveClass(c)
            classes.add(c)
        }
        classes.forEach { c ->
            if (!Modifier.isAbstract(c.modifiers) && Plugin::class.java.isAssignableFrom(c)) {
                logger.info("Found plugin ${c.name}")
                val constructor: Constructor<*>
                try {
                    constructor = c.getDeclaredConstructor()
                } catch (e: Exception) {
                    logger.warn("Failed to get no-argument constructor for ${c.name}", e)
                    return@forEach
                }
                val plugin: Plugin<*>
                try {
                    plugin = constructor.newInstance() as Plugin<*>
                } catch (e: Exception) {
                    logger.warn("Failed to create instance of ${c.name}", e)
                    return@forEach
                }
                logger.info("Initialized plugin ${c.name}")
                plugins.add(plugin)
            }
        }
        this.plugins = plugins
    }

    override fun findResource(name: String): URL? {
        return null // todo
    }

    private fun jarClassBytes(jar: Path): List<Pair<String, ByteArray>> {
        val classes = ArrayList<Pair<String, ByteArray>>()
        JarFile(jar.toFile()).use { jarFile ->
            jarFile.stream().filter { !it.isDirectory && it.name.endsWith(".class") }.forEach { entry ->
                val className = entry.name.removeSuffix(".class").replace('/', '.')
                jarFile.getInputStream(entry).use { input ->
                    classes.add(className to input.readBytes())
                }
            }
        }
        return classes
    }
}