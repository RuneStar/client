package com.runesuite.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import com.runesuite.mapper.*
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(DualNode::class)
class UnderlayDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size == 5 }
            .and { it.instanceFields.all { it.type == INT_TYPE } }

    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.none { it.opcode == PUTFIELD } }
    }

    @DependsOn(Buffer::class)
    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Buffer>()) }
                .and { it.instructions.any { it.opcode == PUTFIELD } }
    }

    class rgb : UniqueMapper.InConstructor.Field(UnderlayDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @MethodParameters()
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod } }
    }

    @MethodParameters("rgb")
    @DependsOn(init::class)
    class setHsl : UniqueMapper.InMethod.Method(init::class) {
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