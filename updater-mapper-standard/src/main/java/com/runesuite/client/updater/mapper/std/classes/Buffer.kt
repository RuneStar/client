package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.nextWithin
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(Node::class)
class Buffer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceFields.size == 2 }
            .and { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 1 }
            .and { it.instanceFields.count { it.type == ByteArray::class.type } == 1 }

    class index : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class array : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    class readLong : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
    }

    @DependsOn(readLong::class)
    class readInt : OrderMapper.InMethod.Method(readLong::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
    }

    class writeSmartByteShort : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == IllegalArgumentException::class.type } }
    }

    @DependsOn(writeSmartByteShort::class)
    class writeByte : OrderMapper.InMethod.Method(writeSmartByteShort::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 128 }
                .nextWithin(10) { it.opcode == INVOKEVIRTUAL }
    }

    @DependsOn(writeSmartByteShort::class)
    class writeShort : OrderMapper.InMethod.Method(writeSmartByteShort::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == 32_768 }
                .nextWithin(10) { it.opcode == INVOKEVIRTUAL }
    }

    @DependsOn(index::class)
    class writeMedium : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 3 }
                .and { it.instructions.last { it.opcode == BIPUSH }.intOperand == 8 }
                .and { it.instructions.filter { it.opcode == BASTORE }.drop(1).first().index >=
                        it.instructions.first { it.opcode == BIPUSH && it.intOperand == 8 }.index }
    }

    @DependsOn(index::class)
    class writeInt : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(24, 16, 8) }
    }

    @DependsOn(index::class)
    class writeIntLE : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(8, 16, 24) }
    }

    @DependsOn(index::class)
    class writeIntME : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(8, /* 0, */ 24, 16) }
    }

    @DependsOn(index::class)
    class writeIntLE16 : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(16, 24, /* 0, */ 8) }
    }

    @DependsOn(writeShort::class, index::class)
    class writeShortLE : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 2 }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
                .and { it.id != method<writeShort>().id }
    }

    class readByte : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BYTE_TYPE }
                .and { it.instructions.none { it.opcode == ICONST_0 } }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
    }

    class readUnsignedByte : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == BALOAD } == 1 }
                .and { it.instructions.count { it.opcode == PUTFIELD } == 1 }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
                .and { it.instructions.none { it.opcode == ICONST_0 } }
    }

    class readUnsignedByteNegate : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == BALOAD } == 1 }
                .and { it.instructions.count { it.opcode == PUTFIELD } == 1 }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
                .and { it.instructions.any { it.opcode == ICONST_0 } }
    }

    @MethodParameters("xteaKey", "start", "end")
    class xteaDecrypt : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == ISUB } > 2 }
    }

    @MethodParameters("xteaKey", "start", "end")
    class xteaEncrypt : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == ISUB } == 2 }
    }

    @MethodParameters("xteaKey")
    class xteaEncryptAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == ISUB } == 1 }
    }

    @MethodParameters("xteaKey")
    class xteaDecryptAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == ISUB } > 1 }
    }

    @MethodParameters("string")
    class writeStringCp1252NullTerminated : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.count { it.opcode == ICONST_1 } == 1 }
    }

    @MethodParameters("string")
    class writeStringCp1252NullSurrounded : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.count { it.opcode == ICONST_1 } > 1 }
    }

    @SinceVersion(154)
    @MethodParameters("boolean")
    class writeBooleanAsByte : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(BOOLEAN_TYPE) }
    }
}