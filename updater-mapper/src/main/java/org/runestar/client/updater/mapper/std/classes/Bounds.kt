package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.Predicate
import java.lang.reflect.Modifier

class Bounds : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.java.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.size == 4 }
            .and { it.instanceFields.all { it.type == Type.INT_TYPE } }
            .and { it.instanceMethods.isNotEmpty() }
            .and { !Modifier.isAbstract(it.access) }

    @MethodParameters("x", "y")
    class setLocation : OrderMapper.InConstructor.Method(Bounds::class, 0, 2) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size == 4 }
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
    }

    @MethodParameters("width", "height")
    class setSize : OrderMapper.InConstructor.Method(Bounds::class, 1, 2) {
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size == 4 }
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
    }

    @DependsOn(setLocation::class)
    class x : OrderMapper.InMethod.Field(setLocation::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(setLocation::class)
    class y : OrderMapper.InMethod.Field(setLocation::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(setSize::class)
    class width : OrderMapper.InMethod.Field(setSize::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(setSize::class)
    class height : OrderMapper.InMethod.Field(setSize::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }
}