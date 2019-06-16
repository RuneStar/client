package org.runestar.client.updater.mapper

import org.objectweb.asm.ClassReader
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import java.nio.file.Path
import java.util.jar.JarFile

class Jar2(val path: Path) {

    val classes = readJar(path).map { Class2(this, it) }

    private val classResolver = classes.associate { it.id to it }

    private val methodResolver = classes.flatMap { it.methods }.associate { it.id to it }

    private val fieldResolver = classes.flatMap { it.fields }.associate { it.id to it }

    operator fun contains(classId: Type): Boolean {
        return classId in classResolver
    }

    operator fun contains(fieldId: Pair<Type, String>): Boolean {
        return fieldId in fieldResolver
    }

    operator fun contains(methodId: Triple<Type, String, Type>): Boolean {
        return methodId in methodResolver
    }

    operator fun get(classId: Type): Class2 {
        return classResolver.getValue(classId)
    }

    operator fun get(methodId: Triple<Type, String, Type>): Method2 {
        return methodResolver.getValue(methodId)
    }

    operator fun get(fieldId: Pair<Type, String>): Field2 {
        return fieldResolver.getValue(fieldId)
    }

    private fun readJar(source: Path, classReaderFlags: Int = 0) : Collection<ClassNode> {
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
}