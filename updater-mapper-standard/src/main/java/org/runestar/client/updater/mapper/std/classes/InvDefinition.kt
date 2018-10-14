package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.ICONST_2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import java.lang.reflect.Modifier

@DependsOn(DualNode::class)
class InvDefinition : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.size == 1 }
            .and { it.instanceFields.all { it.type == INT_TYPE } }
            .and { !Modifier.isAbstract(it.access) }
            .and { it.instanceMethods.any { it.instructions.any { it.opcode == ICONST_2 } } }

    class size : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { true }
    }

    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.none { it.opcode == PUTFIELD } }
    }

    @DependsOn(Buffer::class)
    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.opcode == PUTFIELD } }
    }
}