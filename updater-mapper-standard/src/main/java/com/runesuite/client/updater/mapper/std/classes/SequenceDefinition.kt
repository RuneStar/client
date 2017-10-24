package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.lang.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(DualNode::class)
class SequenceDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 5 }

    @DependsOn(Buffer::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == Opcodes.GOTO } }
                .and { it.instructions.none { it.opcode == Opcodes.BIPUSH && it.intOperand == 13 } }
                .and { it.arguments.startsWith(type<Buffer>()) }
    }

    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == Opcodes.GOTO } }
                .and { it.instructions.any { it.opcode == Opcodes.BIPUSH && it.intOperand == 13 } }
    }
}