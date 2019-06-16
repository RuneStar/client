package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.std.MethodPutField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Instruction2

@DependsOn(Entity::class)
class WallDecoration : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 2 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 8 }

    @DependsOn(Scene.newWallDecoration::class, Entity::class)
    class entity1 : OrderMapper.InMethod.Field(Scene.newWallDecoration::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newWallDecoration::class, Entity::class)
    class entity2 : OrderMapper.InMethod.Field(Scene.newWallDecoration::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<Entity>() }
    }

    @DependsOn(Scene.newWallDecoration::class)
    class tag : MethodPutField(Scene.newWallDecoration::class, 0, LONG_TYPE)

    @DependsOn(Scene.newWallDecoration::class)
    class flags : MethodPutField(Scene.newWallDecoration::class, 0, INT_TYPE)

    @DependsOn(Scene.newWallDecoration::class)
    class x : MethodPutField(Scene.newWallDecoration::class, 1, INT_TYPE)

    @DependsOn(Scene.newWallDecoration::class)
    class y : MethodPutField(Scene.newWallDecoration::class, 2, INT_TYPE)

    @DependsOn(Scene.newWallDecoration::class)
    class tileHeight : MethodPutField(Scene.newWallDecoration::class, 3, INT_TYPE)

    // todo
    @DependsOn(Scene.newWallDecoration::class)
    class orientation : MethodPutField(Scene.newWallDecoration::class, 4, INT_TYPE)

    // todo
    @DependsOn(Scene.newWallDecoration::class)
    class int7 : MethodPutField(Scene.newWallDecoration::class, 5, INT_TYPE)

    @DependsOn(Scene.newWallDecoration::class)
    class xOffset : MethodPutField(Scene.newWallDecoration::class, 6, INT_TYPE)

    @DependsOn(Scene.newWallDecoration::class)
    class yOffset : MethodPutField(Scene.newWallDecoration::class, 7, INT_TYPE)
}