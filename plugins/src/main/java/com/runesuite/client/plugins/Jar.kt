package com.runesuite.client.plugins

import java.io.IOException
import java.net.URLClassLoader
import java.nio.file.FileSystems
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.jar.JarFile

internal val URLClassLoader.urlClasses: Collection<Class<*>> get() {
    val classes = ArrayList<Class<*>>()
    urLs.forEach { url ->
        FileSystems.newFileSystem(Paths.get(url.toURI()), null).use { zipfs ->
            val root = zipfs.getPath("/")
            Files.walkFileTree(root, object : SimpleFileVisitor<Path>() {
                override fun visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult {
                    if (attrs.isRegularFile && file.fileName.toString().endsWith(".class")) {
                        val className = file.toString().removePrefix("/").removeSuffix(".class").replace("/", ".")
                        try {
                            val clazz = loadClass(className)
                            classes.add(clazz)
                        } catch (skipped: ClassNotFoundException) {

                        } catch (skipped: NoClassDefFoundError) {

                        }
                    }
                    return super.visitFile(file, attrs)
                }
            })
        }
    }
    return classes
}

internal fun URLClassLoader(vararg paths: Path): URLClassLoader {
    return URLClassLoader(paths.map { it.toUri().toURL() }.toTypedArray())
}

internal fun verifyJar(jar: Path): Boolean {
    return try {
        JarFile(jar.toFile(), true)
        true
    } catch (e: IOException) {
        false
    } catch (e: SecurityException) {
        false
    }
}