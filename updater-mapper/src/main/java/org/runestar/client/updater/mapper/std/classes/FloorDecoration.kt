package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.std.MethodPutField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2

@DependsOn(Entity::class)
class FloorDecoration : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Entity>() } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 4 }

    @DependsOn(Entity::class)
    class entity : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Entity>() }
    }

    @DependsOn(Scene.newFloorDecoration::class)
    class x : MethodPutField(Scene.newFloorDecoration::class, 0, INT_TYPE)

    @DependsOn(Scene.newFloorDecoration::class)
    class y : MethodPutField(Scene.newFloorDecoration::class, 1, INT_TYPE)

    @DependsOn(Scene.newFloorDecoration::class)
    class tileHeight : MethodPutField(Scene.newFloorDecoration::class, 2, INT_TYPE)

    @DependsOn(Scene.newFloorDecoration::class)
    class tag : MethodPutField(Scene.newFloorDecoration::class, 0, LONG_TYPE)

    @DependsOn(Scene.newFloorDecoration::class)
    class flags : MethodPutField(Scene.newFloorDecoration::class, 3, INT_TYPE)
}