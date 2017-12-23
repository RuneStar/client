package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@DependsOn(Node::class)
class DualNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size == 2 }
            .and { c -> c.instanceFields.count { it.type == c.type } == 2 }

    @MethodParameters()
    class removeDual : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
    }

    @DependsOn(removeDual::class)
    class nextDual : OrderMapper.InMethod.Field(removeDual::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.GETFIELD }
    }

    @DependsOn(nextDual::class)
    class previousDual : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.id != field<nextDual>().id }
    }
}