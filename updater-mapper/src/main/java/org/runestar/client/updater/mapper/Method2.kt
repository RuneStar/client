package org.runestar.client.updater.mapper

import org.objectweb.asm.Type
import org.objectweb.asm.tree.MethodNode

class Method2(val jar: Jar2, val klass: Class2, val node: MethodNode) {

    companion object {
        const val CONSTRUCTOR_NAME = "<init>"
        const val CLASS_INITIALIZER_NAME = "<clinit>"
    }

    val name: String get() = node.name

    val desc: String get() = node.desc

    val type: Type = Type.getMethodType(node.desc)

    val arguments = type.argumentTypes.asList()

    val returnType: Type = type.returnType

    val access get() = node.access

    val signature = name to arguments

    val mark = name to type

    val id = Triple(klass.type, name, type)

    val instructions get() = node.instructions.iterator().asSequence()
            .map { Instruction2(jar, klass, this, it) }

    val isClassInitializer: Boolean get() {
        return name == CLASS_INITIALIZER_NAME
    }

    val isConstructor: Boolean get() {
        return name == CONSTRUCTOR_NAME
    }

    override fun toString(): String {
        return "$klass.$name$desc"
    }
}