package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Type.*

@DependsOn(Packet::class)
class PacketBit : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Packet>() }

    class bitIndex : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class isaac0 : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type in it.jar }
    }

    @MethodParameters("array")
    class newIsaac : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(IntArray::class.type) }
    }

    @MethodParameters("isaac")
    @DependsOn(Isaac::class)
    class setIsaac : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Isaac>()) }
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
    class gBit : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.count { it.opcode == GETFIELD && it.fieldId == field<bitIndex>().id } == 3 }
    }

    @MethodParameters("b")
    class pIsaac1 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == I2B } }
    }

    @MethodParameters()
    class gIsaac1 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.none { it.opcode == ISHL } }
                .and { it.instructions.any { it.opcode == IAND } }
    }

    @MethodParameters()
    class gIsaacSmart1or2 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == SIPUSH && it.intOperand == 128 } }
    }
}