package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
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

@DependsOn(WorldMapManager::class)
class WorldMap : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<WorldMapManager>() } }
            .and { it.instanceFields.any { it.type == BOOLEAN_TYPE } }

    class worldMapManager : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapManager>() }
    }

//    @DependsOn(AbstractIndexCache::class)
//    class indexCache : IdentityMapper.InstanceField() {
//        override val predicate = predicateOf<Field2> { it.type == type<AbstractIndexCache>() }
//    }

    @DependsOn(Font::class)
    class font : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Font>() }
    }

    @DependsOn(AbstractArchive::class)
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<AbstractArchive>()) }
    }

    @MethodParameters("mapArea")
    @DependsOn(WorldMapArea::class)
    class initializeWorldMapManager : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<WorldMapArea>()) }
                .and { it.instructions.any { it.opcode == PUTFIELD } }
    }

    @DependsOn(initializeWorldMapManager::class, WorldMapArea::class)
    class currentMapArea0 : UniqueMapper.InMethod.Field(initializeWorldMapManager::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<WorldMapArea>() }
    }

    @MethodParameters()
    @DependsOn(currentMapArea0::class)
    class getCurrentMapArea : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<WorldMapArea>() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<currentMapArea0>().id } }
    }

    @MethodParameters("mapArea")
    @DependsOn(init::class)
    class setCurrentMapArea : OrderMapper.InMethod.Method(init::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

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

    @DependsOn(WorldMapArchiveLoader::class)
    class cacheLoader : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapArchiveLoader>() }
    }

    @MethodParameters()
    @DependsOn(WorldMapArchiveLoader.load::class)
    class loadCache : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<WorldMapArchiveLoader.load>().id } }
    }

    @MethodParameters()
    @DependsOn(WorldMapArchiveLoader.isLoaded::class)
    class isCacheLoaded : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<WorldMapArchiveLoader.isLoaded>().id } }
    }

    @MethodParameters("a", "b", "c", "d")
    @DependsOn(Coord::class)
    class menuAction : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, type<Coord>(), type<Coord>()) }
    }

    @MethodParameters("a", "b", "c", "d", "e", "f", "g")
    class onCycle : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, BOOLEAN_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(init::class)
    class mapAreas : OrderMapper.InMethod.Field(init::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashMap::class.type }
    }

    @DependsOn(init::class, WorldMapArea::class)
    class mainMapArea : OrderMapper.InMethod.Field(init::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == type<WorldMapArea>() }
    }

    @MethodParameters()
    @DependsOn(WorldMapArea.id::class)
    class currentMapAreaId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.opcode == INVOKEVIRTUAL && it.methodId == method<WorldMapArea.id>().id } }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(WorldMapArea::class)
    class mapAreaAtCoord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<WorldMapArea>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(Coord::class)
    class mouseCoord : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Coord>() }
    }

    @MethodParameters("plane", "x", "y")
    @DependsOn(setCurrentMapArea::class)
    class jump : OrderMapper.InMethod.Method(setCurrentMapArea::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(jump::class)
    class zoom : OrderMapper.InMethod.Field(jump::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == FLOAT_TYPE }
    }

    @DependsOn(jump::class)
    class zoomTarget : OrderMapper.InMethod.Field(jump::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == FLOAT_TYPE }
    }

    @MethodParameters()
    @DependsOn(onCycle::class)
    class smoothZoom : OrderMapper.InMethod.Method(onCycle::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters("id")
    class getMapArea : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<WorldMapArea>() }
                .and { it.arguments == listOf(INT_TYPE) }
    }

    @MethodParameters("id")
    @DependsOn(setCurrentMapArea::class)
    class setCurrentMapAreaId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<setCurrentMapArea>().id } }
    }

    @MethodParameters()
    @DependsOn(zoomTarget::class)
    class getZoomLevel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<zoomTarget>().id } }
    }

    @MethodParameters("zoom")
    @DependsOn(zoomTarget::class)
    class setZoomLevel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE) }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<zoomTarget>().id } }
    }

    class perpetualFlash0 : OrderMapper.InConstructor.Field(WorldMap::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class elementsDisabled : OrderMapper.InConstructor.Field(WorldMap::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(perpetualFlash0::class)
    class perpetualFlash : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(BOOLEAN_TYPE) }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<perpetualFlash0>().id } }
    }

    class flashingElements : OrderMapper.InConstructor.Field(WorldMap::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashSet::class.type }
    }

    class enabledElements : OrderMapper.InConstructor.Field(WorldMap::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashSet::class.type }
    }

    class enabledCategories : OrderMapper.InConstructor.Field(WorldMap::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == HashSet::class.type }
    }

    @DependsOn(flashingElements::class)
    class stopCurrentFlashes : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<flashingElements>().id } }
    }

    @MethodParameters()
    @DependsOn(elementsDisabled::class)
    class getElementsEnabled : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<elementsDisabled>().id } }
    }

    @DependsOn(elementsDisabled::class)
    class setElementsEnabled : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(BOOLEAN_TYPE) }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<elementsDisabled>().id } }
    }

    @DependsOn(enabledElements::class)
    class disableElement : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, BOOLEAN_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<enabledElements>().id } }
    }

    @DependsOn(enabledElements::class)
    class isElementDisabled : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments == listOf(INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<enabledElements>().id } }
    }

    @DependsOn(enabledCategories::class)
    class disableCategory : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, BOOLEAN_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<enabledCategories>().id } }
    }

    @DependsOn(enabledCategories::class)
    class isCategoryDisabled : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments == listOf(INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<enabledCategories>().id } }
    }

    @DependsOn(flashingElements::class)
    class flashElement : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<flashingElements>().id } }
                .and { it.instructions.none { it.opcode == IINC } }
    }

    @DependsOn(flashingElements::class)
    class flashCategory : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<flashingElements>().id } }
                .and { it.instructions.any { it.opcode == IINC } }
    }

    class iconIterator : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Iterator::class.type }
    }

    @DependsOn(iconIterator::class)
    class iconStart : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractWorldMapIcon>() }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<iconIterator>().id } }
    }

    @DependsOn(iconIterator::class)
    class iconNext : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractWorldMapIcon>() }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.none { it.opcode == PUTFIELD && it.fieldId == field<iconIterator>().id } }
    }
}