package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.extensions.Predicate

@DependsOn(Buffer::class)
class PacketBuffer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Buffer>() }

    class bitIndex : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class isaacCipher0 : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type in it.jar }
    }

    @MethodParameters("array")
    class newIsaacCipher : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
    }

    @SinceVersion(157)
    @MethodParameters("isaacCipher")
    @DependsOn(IsaacCipher::class)
    class setIsaacCipher : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<IsaacCipher>()) }
    }

    @MethodParameters()
    @DependsOn(bitIndex::class)
    class importIndex : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<bitIndex>().id } }
    }

    @MethodParameters()
    class exportIndex : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 7 } }
    }

    @MethodParameters("index")
    @DependsOn(bitIndex::class)
    class bitsRemaining : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.count { it.opcode == GETFIELD && it.fieldId == field<bitIndex>().id } == 1 }
    }

    @MethodParameters("bits")
    @DependsOn(bitIndex::class)
    class readBits : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.count { it.opcode == GETFIELD && it.fieldId == field<bitIndex>().id } == 3 }
    }

    @MethodParameters("b")
    class writeByteIsaac : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == I2B } }
    }

    @MethodParameters()
    class readByteIsaac : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.none { it.opcode == ISHL } }
                .and { it.instructions.any { it.opcode == IAND } }
    }

    @MethodParameters()
    @SinceVersion(157)
    class readSmartByteShortIsaac : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == SIPUSH && it.intOperand == 128 } }
    }
}