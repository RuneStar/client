package com.runesuite.client.inject

import net.bytebuddy.implementation.Implementation
import net.bytebuddy.implementation.bytecode.Addition
import net.bytebuddy.implementation.bytecode.StackManipulation
import net.bytebuddy.implementation.bytecode.StackSize
import net.bytebuddy.jar.asm.MethodVisitor
import net.bytebuddy.jar.asm.Opcodes

/**
 * @see[Addition]
 */
internal enum class Multiplication(val opcode: Int, val stackSize: StackSize) : StackManipulation {

    INTEGER(Opcodes.IMUL, StackSize.SINGLE),

    LONG(Opcodes.LMUL, StackSize.DOUBLE);

    override fun isValid(): Boolean {
        return true
    }

    override fun apply(
            methodVisitor: MethodVisitor,
            implementationContext: Implementation.Context
    ): StackManipulation.Size {
        methodVisitor.visitInsn(opcode)
        return stackSize.toDecreasingSize()
    }
}