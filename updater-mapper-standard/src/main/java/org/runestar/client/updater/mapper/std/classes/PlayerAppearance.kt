package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

class PlayerAppearance : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.count { it.type == LONG_TYPE } == 2 }
            .and { it.instanceFields.count { it.type == IntArray::class.type } == 2 }

    @DependsOn(Model::class)
    class getModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Model>() }
    }

    class npcTransformId : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class isFemale : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    @DependsOn(getModel::class)
    class equipment : OrderMapper.InMethod.Field(getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(equipment::class)
    class bodyColors : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
                .and { it != field<equipment>() }
    }

    @MethodParameters()
    class getChatHeadId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
    }

    @MethodParameters()
    @DependsOn(UnlitModel::class)
    class getChatHeadModel : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<UnlitModel>() }
    }

    @MethodParameters("packet")
    @DependsOn(Packet::class)
    class encode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments == listOf(type<Packet>()) }
    }
}