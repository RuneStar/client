package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.nextWithin
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import java.math.BigInteger

@DependsOn(Node::class)
class Packet : IdentityMapper.Class() {
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

    @MethodParameters()
    class g8s : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
    }

    @MethodParameters()
    @DependsOn(g8s::class)
    class g4s : OrderMapper.InMethod.Method(g8s::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
    }

    @MethodParameters("value")
    class pSmart1or2 : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == IllegalArgumentException::class.type } }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 32768 } }
    }

    @MethodParameters("value")
    @DependsOn(pSmart1or2::class)
    class p1 : OrderMapper.InMethod.Method(pSmart1or2::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 128 }
                .nextWithin(10) { it.opcode == INVOKEVIRTUAL }
    }

    @MethodParameters("value")
    @DependsOn(pSmart1or2::class)
    class p2 : OrderMapper.InMethod.Method(pSmart1or2::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == 32_768 }
                .nextWithin(10) { it.opcode == INVOKEVIRTUAL }
    }

    @MethodParameters("value")
    @DependsOn(index::class)
    class p3 : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 3 }
                .and { it.instructions.last { it.opcode == BIPUSH }.intOperand == 8 }
                .and { it.instructions.filter { it.opcode == BASTORE }.drop(1).first().index >=
                        it.instructions.first { it.opcode == BIPUSH && it.intOperand == 8 }.index }
    }

    @MethodParameters("value")
    @DependsOn(index::class)
    class p4s : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(24, 16, 8) }
    }

    @MethodParameters("value")
    @DependsOn(index::class)
    class p4LE : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(8, 16, 24) }
    }

    @MethodParameters("value")
    @DependsOn(index::class)
    class p4ME : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(8, /* 0, */ 24, 16) }
    }

    @MethodParameters("value")
    @DependsOn(index::class)
    class p4LE16 : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 4 }
                .and { it.instructions.filter { it.opcode == BIPUSH }.map { it.intOperand }.toList() ==
                        listOf(16, 24, /* 0, */ 8) }
    }

    @MethodParameters("value")
    @DependsOn(p2::class, index::class)
    class p2LE : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == PUTFIELD && it.fieldId == field<index>().id } == 2 }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
                .and { it.id != method<p2>().id }
    }

    @MethodParameters()
    class g1s : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BYTE_TYPE }
                .and { it.instructions.none { it.opcode == ICONST_0 } }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
    }

    @MethodParameters()
    class g1 : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == BALOAD } == 1 }
                .and { it.instructions.count { it.opcode == PUTFIELD } == 1 }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
                .and { it.instructions.none { it.opcode == ICONST_0 } }
    }

    @MethodParameters()
    class g1n : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.count { it.opcode == BALOAD } == 1 }
                .and { it.instructions.count { it.opcode == PUTFIELD } == 1 }
                .and { it.instructions.none { it.opcode == SIPUSH && it.intOperand == 128 } }
                .and { it.instructions.any { it.opcode == ICONST_0 } }
    }

    @MethodParameters("key", "start", "end")
    class tinyKeyDecrypt : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == ISUB } > 2 }
    }

    @MethodParameters("key", "start", "end")
    class tinyKeyEncrypt : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == ISUB } == 2 }
    }

    @MethodParameters("key")
    class tinyKeyEncryptAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == ISUB } == 1 }
    }

    @MethodParameters("key")
    class tinyKeyDecryptAll : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.count { it.opcode == ISUB } > 1 }
    }

    @MethodParameters("string")
    class pjstr : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.count { it.opcode == ICONST_1 } == 2 }
    }

    @MethodParameters("string")
    class pjstr2 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.count { it.opcode == ICONST_1 } == 4 }
    }

    @MethodParameters("value")
    class pbool : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(BOOLEAN_TYPE) }
    }

    @MethodParameters()
    class gjstr : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.count { it.opcode == ICONST_1 } == 3 }
    }

    @MethodParameters()
    @DependsOn(gjstr::class)
    class gjstr0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<gjstr>().id } }
    }

    @MethodParameters()
    class gjstr2 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.count { it.opcode == ICONST_1 } == 5 }
                .and { it.instructions.none { it.opcode == CASTORE } }
    }

    @MethodParameters()
    @DependsOn(FloorUnderlayType.decode0::class)
    class g3 : UniqueMapper.InMethod.Method(FloorUnderlayType.decode0::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters("n")
    class p8s : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(LONG_TYPE) }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 56 } }
    }

    @MethodParameters("n")
    class p6 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(LONG_TYPE) }
                .and { it.instructions.none { it.opcode == BIPUSH && it.intOperand == 56 } }
    }

    @MethodParameters()
    class gbool : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.none { it.opcode == ISUB } }
    }

    class rsaEncrypt : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size == 2 }
                .and { it.arguments.all { it == BigInteger::class.type } }
    }

    @DependsOn(InvType.decode0::class)
    class g2 : UniqueMapper.InMethod.Method(InvType.decode0::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodOwner == type<Packet>() }
    }

    class gSmart2or4 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == Integer.MAX_VALUE } }
                .and { it.instructions.any { it.opcode == ICONST_M1 } }
    }

    class gSmart1or2s : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 49152 } }
    }
}