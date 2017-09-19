package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Entity::class)
class FloorDecoration : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 5 }

    @DependsOn(Entity::class)
    class entity : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Entity>() }
    }

    @DependsOn(Scene.setFloorDecoration::class)
    class x : OrderMapper.InMethod.Field(Scene.setFloorDecoration::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setFloorDecoration::class)
    class y : OrderMapper.InMethod.Field(Scene.setFloorDecoration::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setFloorDecoration::class)
    class height : OrderMapper.InMethod.Field(Scene.setFloorDecoration::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setFloorDecoration::class)
    class id : OrderMapper.InMethod.Field(Scene.setFloorDecoration::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setFloorDecoration::class)
    class flags : OrderMapper.InMethod.Field(Scene.setFloorDecoration::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}