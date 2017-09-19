package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE

@DependsOn(Entity::class)
class WallDecoration : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 2 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 9 }

    @DependsOn(Scene.setWallDecoration::class, Entity::class)
    class entity : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.setWallDecoration::class, Entity::class)
    class entity2 : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class id : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class flags : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class x : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class y : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class height : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class int6 : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class int7 : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class int8 : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.setWallDecoration::class)
    class int9 : OrderMapper.InMethod.Field(Scene.setWallDecoration::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}