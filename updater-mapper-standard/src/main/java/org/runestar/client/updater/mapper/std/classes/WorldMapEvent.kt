package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2

@DependsOn(TileLocation::class)
class WorldMapEvent : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.instanceMethods.isEmpty() }
            .and { it.instanceFields.size == 3 }
            .and { it.instanceFields.count { it.type == type<TileLocation>() } == 2 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 1 }

    class n : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    @DependsOn(TileLocation::class)
    class location1 : OrderMapper.InConstructor.Field(WorldMapEvent::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TileLocation>() }
    }

    @DependsOn(TileLocation::class)
    class location2 : OrderMapper.InConstructor.Field(WorldMapEvent::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<TileLocation>() }
    }
}