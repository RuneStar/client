package org.runestar.client.updater.deob.util

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Files
import java.nio.file.Path
import java.util.jar.*

fun readJar(
        source: Path,
        classReaderFlags: Int = 0
) : Collection<ClassNode> {
    return JarFile(source.toFile(), true).use { jf ->
        jf.stream().iterator().asSequence().toList()
                .filter { it.name.endsWith(".class") }
                .map { je ->
                    val bytes = jf.getInputStream(je).use { it.readBytes() }
                    val classNode = ClassNode()
                    ClassReader(bytes).accept(classNode, classReaderFlags)
                    classNode
                }
    }
}

fun writeJar(
        classes: Iterable<ClassNode>,
        destination: Path,
        classWriter: () -> ClassWriter = { ClassWriter(0) }
) {
    Files.newOutputStream(destination).use { fos ->
        JarOutputStream(fos).use { jos ->
            classes.forEach { c ->
                val entry = JarEntry(c.name + ".class")
                jos.putNextEntry(entry)
                val cw = classWriter()
                c.accept(cw)
                jos.write(cw.toByteArray())
                jos.closeEntry()
            }
        }
    }
}