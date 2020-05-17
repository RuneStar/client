package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(DualNode::class)
class FloorUnderlayType : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size == 5 }
            .and { it.instanceFields.all { it.type == INT_TYPE } }

    @DependsOn(Packet::class)
    class decode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Packet>()) }
                .and { it.instructions.none { it.opcode == PUTFIELD } }
    }

    @DependsOn(Packet::class)
    class decode0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Packet>()) }
                .and { it.instructions.any { it.opcode == PUTFIELD } }
    }

    class rgb : UniqueMapper.InConstructor.Field(FloorUnderlayType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @MethodParameters()
    class postDecode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod } }
    }

    @MethodParameters("rgb")
    @DependsOn(postDecode::class)
    class setHsl : UniqueMapper.InMethod.Method(postDecode::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(setHsl::class)
    class saturation : OrderMapper.InMethod.Field(setHsl::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(setHsl::class)
    class lightness : OrderMapper.InMethod.Field(setHsl::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(setHsl::class)
    class hue : UniqueMapper.InMethod.Field(setHsl::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == RETURN }
                .prev { it.opcode == PUTFIELD }
    }

    @DependsOn(setHsl::class)
    class hueMultiplier : UniqueMapper.InMethod.Field(setHsl::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == RETURN }
                .prevWithin(10) { it.opcode == GETFIELD }
    }
}