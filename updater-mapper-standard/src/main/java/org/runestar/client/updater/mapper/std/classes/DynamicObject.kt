package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
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

@DependsOn(Entity::class, SeqType::class)
class DynamicObject : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { it.instanceMethods.size == 1 }
            .and { it.instanceFields.count { it.type == type<SeqType>() } == 1 }

    @DependsOn(SeqType::class)
    class seqType : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<SeqType>() }
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

    class frame : OrderMapper.InConstructor.Field(DynamicObject::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    // todo
    class cycleStart : OrderMapper.InConstructor.Field(DynamicObject::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    @MethodParameters()
    @DependsOn(Entity.getModel::class)
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Entity.getModel>().mark }
    }
}