package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(DualNode::class)
class IterableDualNodeQueue : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.contains(Iterable::class.type) }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == type<DualNode>() } }

    class sentinel : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }

    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GOTO } }
    }

    class add : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.none { it.opcode == GOTO } }
    }
}