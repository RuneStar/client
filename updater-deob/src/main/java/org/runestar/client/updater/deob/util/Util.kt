package org.runestar.client.updater.deob.util

import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.tree.analysis.Analyzer
import org.objectweb.asm.tree.analysis.BasicInterpreter
import org.zeroturnaround.zip.ByteSource
import org.zeroturnaround.zip.ZipUtil
import java.nio.file.Path

fun ClassNode(classFile: ByteArray): ClassNode {
    val c = ClassNode()
    ClassReader(classFile).accept(c, 0)
    return c
}

fun parse(classFile: ByteArray, readOptions: Int, writeOptions: Int): ByteArray {
    val w = ClassWriter(writeOptions)
    ClassReader(classFile).accept(w, readOptions)
    return w.toByteArray()
}

fun ClassNode.toByteArray(writer: ClassWriter = ClassWriter(0)): ByteArray {
    accept(writer)
    return writer.toByteArray()
}

fun analyze(classNode: ClassNode) {
    for (m in classNode.methods) {
        try {
            Analyzer(BasicInterpreter()).analyze(classNode.name, m)
        } catch (e: Exception) {
            throw Exception("${classNode.name}.${m.name}${m.desc}", e)
        }
    }
}

fun readClasses(jar: Path): List<ByteArray> {
    val classes = ArrayList<ByteArray>()
    ZipUtil.iterate(jar.toFile()) { input, entry ->
        if (!entry.name.endsWith(".class")) return@iterate
        classes.add(input.readAllBytes())
    }
    return classes
}

fun readClassNodes(jar: Path) = readClasses(jar).map { ClassNode(it) }

fun writeClasses(classes: Iterable<ByteArray>, jar: Path) {
    ZipUtil.pack(classes.map { ByteSource("${ClassReader(it).className}.class", it) }.toTypedArray(), jar.toFile())
}