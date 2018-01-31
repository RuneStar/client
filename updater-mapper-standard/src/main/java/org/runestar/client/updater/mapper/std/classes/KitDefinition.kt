package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.tree.Instruction2

@DependsOn(DualNode::class)
class KitDefinition : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == ShortArray::class.type } == 4 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 2 }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 1 }

    class archives : UniqueMapper.InConstructor.Field(KitDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class readNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == Opcodes.BIPUSH && it.intOperand == 41 } }
    }

    @DependsOn(readNext::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it != method<readNext>() }
    }

    @DependsOn(readNext::class)
    class recolorFrom : OrderMapper.InMethod.Field(readNext::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SASTORE }
                .prevWithin(7) { it.opcode == Opcodes.GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(readNext::class)
    class recolorTo : OrderMapper.InMethod.Field(readNext::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SASTORE }
                .prevWithin(7) { it.opcode == Opcodes.GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(readNext::class)
    class retextureFrom : OrderMapper.InMethod.Field(readNext::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SASTORE }
                .prevWithin(7) { it.opcode == Opcodes.GETFIELD && it.fieldType == ShortArray::class.type }
    }

    @DependsOn(readNext::class)
    class retextureTo : OrderMapper.InMethod.Field(readNext::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SASTORE }
                .prevWithin(7) { it.opcode == Opcodes.GETFIELD && it.fieldType == ShortArray::class.type }
    }
}