package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Instruction2

class Node : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceFields.size == 3 }
            .and { it.instanceFields.count { it.type == LONG_TYPE } == 1 }
            .and { it.superType == Any::class.type }
            .and { c -> c.instanceFields.count { it.type == c.type } == 2 }

    class key : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == LONG_TYPE }
    }

    @DependsOn(next::class)
    class previous : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() }
                .and { it != field<next>() }
    }

    @DependsOn(hasNext::class)
    class next : UniqueMapper.InMethod.Field(hasNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.GETFIELD }
    }

    @MethodParameters()
    class hasNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    class remove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }
}