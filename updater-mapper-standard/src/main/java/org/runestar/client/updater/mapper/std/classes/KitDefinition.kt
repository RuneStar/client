package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(DualNode::class)
class KitDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } == 4 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 2 }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 1 }

    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == Opcodes.BIPUSH && it.intOperand == 41 } }
    }

    @DependsOn(readNext::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it != method<readNext>() }
    }
}