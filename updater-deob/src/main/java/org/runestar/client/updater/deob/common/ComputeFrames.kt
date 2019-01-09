package org.runestar.client.updater.deob.common

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Type
import org.objectweb.asm.tree.ClassNode
import org.runestar.client.updater.deob.Transformer
import org.runestar.client.updater.deob.util.readJar
import org.runestar.client.updater.deob.util.writeJar
import java.nio.file.Path

object ComputeFrames : Transformer {

    override fun transform(source: Path, destination: Path) {
        val classNodes = readJar(source)
        val classNames = classNodes.associateBy { it.name }
        writeJar(classNodes, destination) { Writer(classNames) }
    }

    private class Writer(private val classNames: Map<String, ClassNode>) : ClassWriter(ClassWriter.COMPUTE_FRAMES) {

        companion object {
            val OBJECT_INTERNAL_NAME: String = Type.getInternalName(Any::class.java)
        }

        override fun getCommonSuperClass(type1: String, type2: String): String {
            if (isAssignable(type1, type2, classNames)) return type1
            if (isAssignable(type2, type1, classNames)) return type2
            var t1 = type1
            do {
                t1 = checkNotNull(superClassName(t1, classNames))
            } while (!isAssignable(t1, type2, classNames))
            return t1
        }

        private fun isAssignable(to: String, from: String, classNames: Map<String, ClassNode>): Boolean {
            if (to == from) return true
            val sup = superClassName(from, classNames) ?: return false
            if (isAssignable(to, sup, classNames)) return true
            return interfaceNames(from, classNames).any { isAssignable(to, it, classNames) }
        }

        private fun interfaceNames(type: String, classNames: Map<String, ClassNode>): List<String> {
            return if (type in classNames) {
                val c = classNames.getValue(type)
                c.interfaces
            } else {
                val c = Class.forName(type.replace('/', '.'))
                c.interfaces.map { Type.getInternalName(it) }
            }
        }

        private fun superClassName(type: String, classNames: Map<String, ClassNode>): String? {
            return if (type in classNames) {
                classNames.getValue(type).superName
            } else {
                val c = Class.forName(type.replace('/', '.'))
                if (c.isInterface) {
                    OBJECT_INTERNAL_NAME
                } else {
                    c.superclass?.let { Type.getInternalName(it) }
                }
            }
        }
    }
}