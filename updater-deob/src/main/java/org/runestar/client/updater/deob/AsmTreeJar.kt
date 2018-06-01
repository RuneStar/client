package org.runestar.client.updater.deob

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import java.io.FileOutputStream
import java.nio.file.Path
import java.util.jar.JarFile
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

fun readJar(source: Path, classReaderFlags: Int = 0) : Collection<ClassNode> {
    return JarFile(source.toFile()).use { jf ->
        jf.stream().iterator().asSequence()
                .filter { it.name.endsWith(".class") }
                .map {
                    ClassNode().apply {
                        ClassReader(jf.getInputStream(it)).accept(this, classReaderFlags)
                    }
                }.toList()
    }
}

fun writeJar(classes: Iterable<ClassNode>, destination: Path, classWriterFlags: Int = 0) {
    FileOutputStream(destination.toFile()).use { fos ->
        ZipOutputStream(fos).use { zos ->
            classes.forEach { c ->
                zos.putNextEntry(ZipEntry(c.name + ".class"))
                val cw = ClassWriter(classWriterFlags)
                c.accept(cw)
                zos.write(cw.toByteArray())
                zos.closeEntry()
            }
        }
    }
}