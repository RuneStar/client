package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@DependsOn(Entity::class, SequenceDefinition::class)
class DynamicObject : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { it.instanceMethods.size == 1 }
            .and { it.instanceFields.count { it.type == type<SequenceDefinition>() } == 1 }

    @DependsOn(SequenceDefinition::class)
    class sequenceDefinition : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<SequenceDefinition>() }
    }

    class id : OrderMapper.InConstructor.Field(DynamicObject::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class type : OrderMapper.InConstructor.Field(DynamicObject::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class orientation : OrderMapper.InConstructor.Field(DynamicObject::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class plane : OrderMapper.InConstructor.Field(DynamicObject::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class x : OrderMapper.InConstructor.Field(DynamicObject::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class y : OrderMapper.InConstructor.Field(DynamicObject::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }
}