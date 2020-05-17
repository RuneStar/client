package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(IndexedSprite::class)
class WorldMapManager : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.constructors.isNotEmpty() }
            .and { it.constructors.first().arguments.startsWith(listOf(type<IndexedSprite>().withDimensions(1), HashMap::class.type)) }

    @DependsOn(IndexedSprite::class)
    class mapSceneSprites : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexedSprite>().withDimensions(1) }
    }

    @DependsOn(WorldMapRegion::class)
    class regions : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapRegion>().withDimensions(2) }
    }

    class fonts : OrderMapper.InConstructor.Field(WorldMapManager::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashMap::class.type }
    }

    @MethodParameters()
    class buildIcons : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == HashMap::class.type }
    }

    @MethodParameters()
    @DependsOn(buildIcons::class)
    class buildIcons0 : UniqueMapper.InMethod.Method(buildIcons::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(buildIcons::class)
    class icons : UniqueMapper.InMethod.Field(buildIcons::class) {
        override val predicate = predicateOf<Instruction2> { it.isField }
    }

    class drawOverview : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size == 7 }
    }

    @MethodParameters("indexCache", "cacheName", "isMembersWorld")
    @DependsOn(AbstractArchive::class)
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<AbstractArchive>()) }
    }

    @MethodParameters()
    class clearIcons : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.arguments.isEmpty() }
                .and { it.instructions.none { it.isLabel } }
    }

    @DependsOn(Sprite::class)
    class overviewSprite : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Sprite>() }
    }

    @DependsOn(WorldMapAreaData::class)
    class mapAreaData : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapAreaData>() }
    }

    class isLoaded0 : OrderMapper.InConstructor.Field(WorldMapManager::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    class isLoaded : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }

    class loadStarted : OrderMapper.InConstructor.Field(WorldMapManager::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

//    @MethodParameters("x", "y", "dst")
//    @DependsOn(WorldMapRegion::class)
//    class getNeighboringRegions : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, type<WorldMapRegion>().withDimensions(1)) }
//    }
}