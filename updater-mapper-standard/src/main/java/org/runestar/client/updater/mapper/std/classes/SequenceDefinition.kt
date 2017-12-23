package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(DualNode::class)
class SequenceDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 5 }

    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GOTO } }
                .and { it.instructions.none { it.opcode == BIPUSH && it.intOperand == 13 } }
                .and { it.arguments.startsWith(type<Buffer>()) }
    }

    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GOTO } }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 13 } }
    }

    class shield : OrderMapper.InConstructor.Field(SequenceDefinition::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class weapon : OrderMapper.InConstructor.Field(SequenceDefinition::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}