package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
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

    @SinceVersion(165)
    @DependsOn(Sprite::class)
    class sprite : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Sprite>() }
    }

    @DependsOn(IndexedSprite::class)
    class mapSceneSprites : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexedSprite>().withDimensions(1) }
    }

    @DependsOn(init::class)
    class fonts : OrderMapper.InMethod.Field(WorldMap.init::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashMap::class.type }
    }

    @DependsOn(Client.Strings_loading::class)
    class drawLoading : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.Strings_loading>().id } }
    }

    @MethodParameters("x", "y", "width", "height", "cycle")
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "Coord: " } }
    }

    @MethodParameters("x", "y", "width", "height")
    @DependsOn(Client.isMembersWorld::class)
    class drawOverview : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.isMembersWorld>().id } }
    }

    @DependsOn(WorldMapIndexCacheLoader::class)
    class cacheLoader : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapIndexCacheLoader>() }
    }

    @MethodParameters()
    @DependsOn(WorldMapIndexCacheLoader.load::class)
    class loadCache : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<WorldMapIndexCacheLoader.load>().id } }
    }

    @MethodParameters()
    @DependsOn(WorldMapIndexCacheLoader.isLoaded::class)
    class isCacheLoaded : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<WorldMapIndexCacheLoader.isLoaded>().id } }
    }

    @MethodParameters("a", "b", "c", "d")
    @DependsOn(TileLocation::class)
    class menuAction : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, type<TileLocation>(), type<TileLocation>()) }
    }

    @MethodParameters("a", "b", "c", "d", "e", "f", "g")
    class onCycle : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, BOOLEAN_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }
}