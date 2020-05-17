package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(Node::class)
class DualNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size >= 2 }
            .and { c -> c.instanceFields.count { it.type == c.type } == 2 }

    @MethodParameters()
    class removeDual : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
    }

    @DependsOn(removeDual::class)
    class nextDual : OrderMapper.InMethod.Field(removeDual::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD }
    }

    @DependsOn(nextDual::class)
    class previousDual : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.id != field<nextDual>().id }
                .and { it.type == type<DualNode>() }
    }

    class keyDual : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == LONG_TYPE }
    }
}