package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Player.appearance::class)
class PlayerAppearance : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.type == field<Player.appearance>().type }

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
}