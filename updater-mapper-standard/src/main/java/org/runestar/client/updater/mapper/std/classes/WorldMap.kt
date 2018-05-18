package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(WorldMapManager::class)
class WorldMap : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<WorldMapManager>() } }
            .and { it.instanceFields.any { it.type == BOOLEAN_TYPE } }

    class worldMapManager : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapManager>() }
    }

    @DependsOn(AbstractIndexCache::class)
    class indexCache : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractIndexCache>() }
    }

    @DependsOn(Font::class)
    class font : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Font>() }
    }

    @DependsOn(AbstractIndexCache::class)
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<AbstractIndexCache>()) }
    }

    @MethodParameters("worldMapData")
    @DependsOn(WorldMapData::class)
    class initializeWorldMapManager : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<WorldMapData>()) }
                .and { it.instructions.any { it.opcode == PUTFIELD } }
    }

    @DependsOn(initializeWorldMapManager::class, WorldMapData::class)
    class worldMapData : UniqueMapper.InMethod.Field(initializeWorldMapManager::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<WorldMapData>() }
    }
}