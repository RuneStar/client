package org.runestar.client.updater.mapper

import org.objectweb.asm.Type
import org.objectweb.asm.tree.*

class Instruction2(val jar: Jar2, val klass: Class2, val method: Method2, node: AbstractInsnNode) {

    val opcode: Int get() {
        return node!!.opcode
    }

    var node: AbstractInsnNode? = node
        private set

    val exists get() = node != null

    fun next() {
        node = node?.next
    }

    fun prev() {
        node = node?.previous
    }

    val index: Int get() {
        return method.node.instructions.indexOf(node)
    }

    val isField: Boolean get() {
        return node is FieldInsnNode
    }

    val isLabel: Boolean get() {
        return node is LabelNode
    }

    val isMethod: Boolean get() {
        return node is MethodInsnNode
    }

    val intOperand: Int get() {
        return (node as IntInsnNode).operand
    }

    val typeDesc: String get() {
        return (node as TypeInsnNode).desc
    }

    val typeType: Type get() {
        return Type.getObjectType(typeDesc)
    }

    val varVar: Int get() {
        return (node as VarInsnNode).`var`
    }

    val ldcCst: Any get() {
        return (node as LdcInsnNode).cst
    }

    val fieldType: Type get() {
        return Type.getType((node as FieldInsnNode).desc)
    }

    val fieldOwner: Type get() {
        return Type.getObjectType((node as FieldInsnNode).owner)
    }

    val classType: Type get() {
        val n = node
        return when (n) {
            is TypeInsnNode -> Type.getObjectType(n.desc)
            is MultiANewArrayInsnNode -> Type.getObjectType(n.desc)
            else -> error(this)
        }
    }

    val methodType: Type get() {
        val n = node as MethodInsnNode
        return Type.getMethodType(n.desc)
    }

    val methodName: String get() {
        return (node as MethodInsnNode).name
    }

    val fieldName: String get() {
        return (node as FieldInsnNode).name
    }

    val methodOwner: Type get() {
        val n = node as MethodInsnNode
        return Type.getObjectType(n.owner)
    }

    val methodId: Triple<Type, String, Type> get() {
        return Triple(methodOwner, methodName, methodType)
    }

    val methodMark: Pair<String, Type> get() {
        return methodName to methodType
    }

    val fieldId: Pair<Type, String> get() {
        return fieldOwner to fieldName
    }

    val classId: Type get() {
        return classType
    }

    override fun toString(): String {
        return "$method:$index"
    }
}