package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.client.updater.mapper.std.IndexCacheFieldMapper
import com.runesuite.client.updater.mapper.std.StringsUniqueMapper
import com.runesuite.mapper.*
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import java.applet.Applet
import java.io.File
import java.lang.management.GarbageCollectorMXBean
import java.lang.reflect.Modifier
import java.net.URL
import java.util.zip.CRC32

class Client : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.name == "client" }

    @DependsOn(Player::class)
    class localPlayer : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Player>() }
    }

    @DependsOn(Player::class)
    class players : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Player>().withDimensions(1) }
    }

    @DependsOn(Npc::class)
    class npcs : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Npc>().withDimensions(1) }
    }

    @DependsOn(GameObject::class)
    class gameObjects : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GameObject>().withDimensions(1) }
    }

    @MethodParameters("id")
    @DependsOn(NpcDefinition::class)
    class getNpcDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<NpcDefinition>() }
    }

//    @MethodParameters("id")
//    @DependsOn(HealthBarDefinition::class)
//    class getHealthBarDefinition : StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<HealthBarDefinition>() }
//    }

    @DependsOn(KitDefinition::class)
    class getKitDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<KitDefinition>() }
    }

    @DependsOn(SpotAnimationDefinition::class)
    class getSpotAnimationDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<SpotAnimationDefinition>() }
    }

    @DependsOn(SequenceDefinition::class)
    class getSequenceDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<SequenceDefinition>() }
    }

    @MethodParameters("id")
    @DependsOn(ItemDefinition::class)
    class getItemDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ItemDefinition>() }
    }

    @MethodParameters("id")
    @DependsOn(ObjectDefinition::class)
    class getObjectDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ObjectDefinition>() }
    }

    @MethodParameters("argument1", "argument2", "opcode", "argument0", "action", "targetName", "mouseX", "mouseY")
    class menuAction : StaticMethod() {
        override val predicate = predicateOf<Method2> {
            it.arguments.startsWith(
                    INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                    String::class.type, String::class.type, INT_TYPE, INT_TYPE)
        }
    }

    @SinceVersion(134)
    class insertMenuItem : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type, String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC } }
    }

    @DependsOn(World::class)
    class worlds : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<World>().withDimensions(1) }
    }

    @DependsOn(worlds::class)
    class loadWorlds : StaticMethod() {
        override val predicate = predicateOf<Method2> {
            it.instructions.any {
                it.opcode == PUTSTATIC && it.fieldId == field<worlds>().id
            }
        }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuActions : OrderMapper.InMethod.Field(insertMenuItem::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == Array<String>::class.type }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuTargetNames : OrderMapper.InMethod.Field(insertMenuItem::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == Array<String>::class.type }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuOpcodes : OrderMapper.InMethod.Field(insertMenuItem::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuArguments0 : OrderMapper.InMethod.Field(insertMenuItem::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuArguments1 : OrderMapper.InMethod.Field(insertMenuItem::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuArguments2 : OrderMapper.InMethod.Field(insertMenuItem::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @SinceVersion(150)
    @DependsOn(insertMenuItem::class)
    class menuBooleanArray : OrderMapper.InMethod.Field(insertMenuItem::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BooleanArray::class.type }
    }

    class worldToScreen : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 3..4 }
                .and { it.instructions.any { it.opcode == ISHR } }
    }

    @DependsOn(worldToScreen::class)
    class plane : OrderMapper.InMethod.Field(worldToScreen::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class cameraX : OrderMapper.InMethod.Field(worldToScreen::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class cameraZ : OrderMapper.InMethod.Field(worldToScreen::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class cameraY : OrderMapper.InMethod.Field(worldToScreen::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class cameraPitch : OrderMapper.InMethod.Field(worldToScreen::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class cameraYaw : OrderMapper.InMethod.Field(worldToScreen::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class getTileHeight : OrderMapper.InMethod.Method(worldToScreen::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(getTileHeight::class)
    class tileRenderFlags : OrderMapper.InMethod.Field(getTileHeight::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BYTE_TYPE.withDimensions(3) }
    }

    @DependsOn(getTileHeight::class)
    class tileHeights : OrderMapper.InMethod.Field(getTileHeight::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE.withDimensions(3) }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class isMenuOpen : OrderMapper.InMethod.Field(insertMenuItem::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(134)
    @DependsOn(insertMenuItem::class)
    class menuOptionsCount : OrderMapper.InMethod.Field(insertMenuItem::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(menuAction::class)
    class crosshairColor : OrderMapper.InMethod.Field(menuAction::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    class visibleTiles : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE.withDimensions(2) }
    }

    @DependsOn(PacketBuffer::class)
    class updateExternalPlayer : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(type<PacketBuffer>(), INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 0xF_FF_FF_FF } }
    }

    @DependsOn(updateExternalPlayer::class)
    class playerRegions : OrderMapper.InMethod.Field(updateExternalPlayer::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Scene::class)
    class scene : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Scene>() }
    }

    @DependsOn(AbstractRasterProvider::class)
    class rasterProvider : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractRasterProvider>() }
    }

    @DependsOn(GrandExchangeOffer::class)
    class grandExchangeOffers : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GrandExchangeOffer>().withDimensions(1) }
    }

    @MethodParameters("x", "y")
    @SinceVersion(141)
    @DependsOn(isMenuOpen::class)
    class openMenu : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.opcode == PUTSTATIC && it.fieldId == field<isMenuOpen>().id } }
    }

    @SinceVersion(141)
    @DependsOn(openMenu::class)
    class menuX : OrderMapper.InMethod.Field(openMenu::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(openMenu::class)
    class menuY : OrderMapper.InMethod.Field(openMenu::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(openMenu::class)
    class menuWidth : OrderMapper.InMethod.Field(openMenu::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(openMenu::class)
    class menuHeight : OrderMapper.InMethod.Field(openMenu::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    class addMessage : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type, String::class.type, String::class.type) }
    }

    @DependsOn(ClanMate::class)
    class clanChat : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<ClanMate>().withDimensions(1) }
    }

    @DependsOn(Ignored::class)
    class ignoreList : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Ignored>().withDimensions(1) }
    }

    @DependsOn(Friend::class)
    class friendsList : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Friend>().withDimensions(1) }
    }

    @DependsOn(MouseHandler::class)
    class mouseX : OrderMapper.InClassInitializer.Field(MouseHandler::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(MouseHandler::class)
    class mouseY : OrderMapper.InClassInitializer.Field(MouseHandler::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(ClientPreferences::class)
    class clientPreferences : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<ClientPreferences>() }
    }

    @DependsOn(CollisionMap::class)
    class collisionMaps : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<CollisionMap>().withDimensions(1) }
    }

    @DependsOn(Projectile::class, NodeDeque::class)
    class projectiles : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<Projectile>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeDeque>() }
    }

    class displayFps : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "displayfps" }
                .nextWithin(8) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Widget.width::class)
    class widgetWidths : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Widget.width>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Widget.height::class)
    class widgetHeights : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Widget.height>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Widget.x::class)
    class widgetXs : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Widget.x>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Widget.x::class)
    class widgetCount : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Widget.x>().id }
                .prevWithin(9) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Widget.y::class)
    class widgetYs : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Widget.y>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    class sineTable : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.method.isClassInitializer }
                .and { it.opcode == INVOKESTATIC && it.methodId == Math::sin.id }
                .prevWithin(7) { it.opcode == LDC && it.ldcCst == 65536.0 }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    class cosineTable : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.method.isClassInitializer }
                .and { it.opcode == INVOKESTATIC && it.methodId == Math::cos.id }
                .prevWithin(7) { it.opcode == LDC && it.ldcCst == 65536.0 }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(worldToScreen::class)
    class viewportHeight : OrderMapper.InMethod.Field(worldToScreen::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_2 }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class viewportWidth : OrderMapper.InMethod.Field(worldToScreen::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_2 }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class viewportZoom : OrderMapper.InMethod.Field(worldToScreen::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ILOAD && it.varVar == 1 }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class viewportTempX : OrderMapper.InMethod.Field(worldToScreen::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class viewportTempY : OrderMapper.InMethod.Field(worldToScreen::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(viewportHeight::class, viewportWidth::class)
    class viewportOffsetY : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == RETURN }
                .prev { it.opcode == PUTSTATIC && it.fieldId == field<viewportHeight>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldId == field<viewportWidth>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(viewportHeight::class, viewportWidth::class, viewportOffsetY::class)
    class viewportOffsetX : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == RETURN }
                .prev { it.opcode == PUTSTATIC && it.fieldId == field<viewportHeight>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldId == field<viewportWidth>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldId == field<viewportOffsetY>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

//    class pauseFontMetrics : IdentityMapper.StaticField() {
//        override val predicate = predicateOf<Field2> { it.type == FontMetrics::class.type }
//    }
//
//    class pauseFont : IdentityMapper.StaticField() {
//        override val predicate = predicateOf<Field2> { it.type == Font::class.type }
//    }
//
//    class pauseImage : IdentityMapper.StaticField() {
//        override val predicate = predicateOf<Field2> { it.type == Image::class.type }
//    }

    @DependsOn(GameShell::class)
    class gameShell : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GameShell>() }
    }

    @DependsOn(NodeDeque::class)
    class groundItems : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeDeque>().withDimensions(3) }
    }

    @DependsOn(Widget::class)
    class widgets : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Widget>().withDimensions(2) }
    }

    @DependsOn(MouseHandler::class)
    class MouseHandler_instance : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseHandler>() }
    }

    @SinceVersion(141)
    @DependsOn(MouseWheel::class)
    class mouseWheel : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseWheel>() }
    }

    @DependsOn(KeyHandler::class)
    class KeyHandler_instance : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<KeyHandler>() }
    }

    @DependsOn(WidgetNode::class, NodeHashTable::class)
    class widgetNodes : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<WidgetNode>() }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(ItemContainer::class, NodeHashTable::class)
    class itemContainers : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ItemContainer>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    class garbageCollector : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == GarbageCollectorMXBean::class.type }
    }

    @DependsOn(ClassInfo::class, IterableNodeDeque::class)
    class classInfos : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ClassInfo>() }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == type<IterableNodeDeque>() }
    }

    @DependsOn(NpcDefinition::class, EvictingHashTable::class)
    class cachedNpcDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<NpcDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(Sprite::class, EvictingHashTable::class)
    class cachedSprites : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<Sprite>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(KitDefinition::class, EvictingHashTable::class)
    class cachedKitDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<KitDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(SpotAnimationDefinition::class, EvictingHashTable::class)
    class cachedSpotAnimationDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<SpotAnimationDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(ItemDefinition::class, EvictingHashTable::class)
    class cachedItemDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ItemDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(VarbitDefinition::class, EvictingHashTable::class)
    class cachedVarbitDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<VarbitDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(SequenceDefinition::class, EvictingHashTable::class)
    class cachedSequenceDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<SequenceDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(ObjectDefinition::class, EvictingHashTable::class)
    class cachedObjectDefinitions : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ObjectDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(GzipDecompressor::class)
    class gzipDecompressor : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GzipDecompressor>() }
    }

    @DependsOn(PlatformInfo::class)
    class platformInfo : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PlatformInfo>() }
    }

    class clanChatOwner : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3625 }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    class fps : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Fps:" }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }

        // static field called through subclass
        override fun resolve(instruction: Instruction2): Field2 {
            return instruction.jar[instruction.jar[instruction.fieldOwner].superType to instruction.fieldName]
        }
    }

    class worldToMinimap : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 90_000 } }
    }

    class minimapOrientation : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 5506 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class currentLevels : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3305 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    class levels : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3306 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    class experience : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3307 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    class isMembersWorld : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3312 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class rights : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3316 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class weight : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3322 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class runEnergy : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3321 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class worldId : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3318 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class worldProperties : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3324 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(plane::class)
    class baseX : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3308 }
                .nextWithin(10) { it.opcode == GETSTATIC && it.fieldId == field<plane>().id }
                .nextWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(plane::class, baseX::class)
    class baseY : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3308 }
                .nextWithin(10) { it.opcode == GETSTATIC && it.fieldId == field<plane>().id }
                .nextWithin(25) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE && it.fieldId != field<baseX>().id }
    }

    class cycle : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3300 }
                .nextWithin(30) { it.isLabel }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(loadWorlds::class)
    class worldsUrl : OrderMapper.InMethod.Field(loadWorlds::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(loadWorlds::class)
    class worldsCount : OrderMapper.InMethod.Field(loadWorlds::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(AccessFile::class)
    class getPreferencesFile : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AccessFile>() }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "preferences" } }
    }

    @MethodParameters
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == Applet::init.mark }
    }

    @DependsOn(init::class)
    class worldHost : OrderMapper.InMethod.Field(init::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == URL::getHost.id }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @MethodParameters("gameState")
    class updateGameState : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 40 } }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 45 } }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 20 } }
                .and { it.instructions.count() <= 200 }
    }

    @DependsOn(updateGameState::class)
    class gameState : OrderMapper.InMethod.Field(updateGameState::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(AbstractRasterProvider.draw::class)
    class methodDraw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == method<AbstractRasterProvider.draw>().id } }
    }

    @SinceVersion(141)
    @DependsOn(methodDraw::class, gameState::class)
    class gameDrawingMode : UniqueMapper.InMethod.Field(methodDraw::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 30 }
                .nextWithin(10) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE && it.fieldId != field<gameState>().id }
    }

    class username : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == String::class.type }
                .next { it.isMethod && it.methodName == "trim" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
                .next { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(username::class)
    class password : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "" }
                .next { it.opcode == PUTSTATIC && it.fieldId == field<username>().id }
                .next { it.opcode == LDC && it.ldcCst == "" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @MethodParameters("values", "ordinal")
    @DependsOn(Enumerated::class)
    class findEnumerated : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Enumerated>() }
    }

    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_pixels : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE.withDimensions(1) }
                .and { it.klass == klass<Rasterizer2D>() }
    }

    @SinceVersion(141)
    @MethodParameters("x", "y", "color")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_setPixel : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawLine : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.instructions.any { it.opcode == INVOKESTATIC && it.methodId == Math::floor.id } }
    }

    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_clear : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.any { it.opcode == GOTO } }
    }

    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_resetClip : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments.isEmpty() }
                .and { it.instructions.none { it.opcode == GOTO } }
    }

    @DependsOn(Rasterizer2D_resetClip::class)
    class Rasterizer2D_xClipStart : OrderMapper.InMethod.Field(Rasterizer2D_resetClip::class, 0, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer2D_resetClip::class)
    class Rasterizer2D_yClipStart : OrderMapper.InMethod.Field(Rasterizer2D_resetClip::class, 1, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer2D_resetClip::class)
    class Rasterizer2D_xClipEnd : OrderMapper.InMethod.Field(Rasterizer2D_resetClip::class, 2, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer2D_resetClip::class)
    class Rasterizer2D_yClipEnd : OrderMapper.InMethod.Field(Rasterizer2D_resetClip::class, 3, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer2D_resetClip::class)
    class Rasterizer2D_width : OrderMapper.InMethod.Field(Rasterizer2D_resetClip::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @DependsOn(Rasterizer2D_resetClip::class)
    class Rasterizer2D_height : OrderMapper.InMethod.Field(Rasterizer2D_resetClip::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @MethodParameters("x", "y", "length", "color")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawHorizontalLine : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == IMUL } == 1 }
    }

    @MethodParameters("x", "y", "length", "color")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawVerticalLine : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == IMUL } == 2 }
    }

    @MethodParameters("x", "y", "width", "height", "color")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawRectangle : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == INVOKESTATIC } == 4 }
    }

    @MethodParameters("dst")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_getClipArray : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE.withDimensions(1)) }
                .and { it.instructions.any { it.opcode == GETSTATIC } }
    }

    @MethodParameters("src")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_setClipArray : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE.withDimensions(1)) }
                .and { it.instructions.any { it.opcode == PUTSTATIC } }
    }

    @MethodParameters("pixels", "width", "height")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_replace : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("xStart", "yStart", "xEnd", "yEnd")
    @DependsOn(Rasterizer2D_replace::class)
    class Rasterizer2D_setClip : UniqueMapper.InMethod.Method(Rasterizer2D_replace::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @MethodParameters("xStart", "yStart", "xEnd", "yEnd")
    @DependsOn(Rasterizer2D::class, Rasterizer2D_setClip::class)
    class Rasterizer2D_expandClip : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 4..5 }
                .and { it != method<Rasterizer2D_setClip>() }
                .and { it.instructions.none { it.opcode in setOf(ICONST_0, ICONST_1, ISUB) } }
    }

    @DependsOn(ActionPriority::class)
    class ActionPriority_0 : OrderMapper.InClassInitializer.Field(ActionPriority::class, 0, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<ActionPriority>() }
    }

    @DependsOn(ActionPriority::class)
    class ActionPriority_1 : OrderMapper.InClassInitializer.Field(ActionPriority::class, 1, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<ActionPriority>() }
    }

    @DependsOn(ActionPriority::class)
    class ActionPriority_2 : OrderMapper.InClassInitializer.Field(ActionPriority::class, 2, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<ActionPriority>() }
    }

    @DependsOn(ActionPriority::class)
    class ActionPriority_3 : OrderMapper.InClassInitializer.Field(ActionPriority::class, 3, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<ActionPriority>() }
    }

    @SinceVersion(141)
    @DependsOn(BoundingBox2D::class)
    class addBoundingBox2D : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodOwner == type<BoundingBox2D>() && it.methodName == Method2.CONSTRUCTOR_NAME } }
    }

//    @SinceVersion(145)
//    @DependsOn(AxisAlignedBoundingBox::class)
//    class addAxisAlignedBoundingBox : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.instructions.any { it.isMethod && it.methodOwner == type<AxisAlignedBoundingBox>() && it.methodName == "<init>" } }
//    }

    @SinceVersion(141)
    @DependsOn(addBoundingBox2D::class, IterableNodeDeque::class)
    class boundingBoxes : UniqueMapper.InMethod.Field(addBoundingBox2D::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<IterableNodeDeque>() }
    }

    @SinceVersion(141)
    @DependsOn(AxisAlignedBoundingBoxDrawMode::class)
    class AxisAlignedBoundingBoxDrawMode_MOUSE_OVER : OrderMapper.InClassInitializer.Field(AxisAlignedBoundingBoxDrawMode::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AxisAlignedBoundingBoxDrawMode>() }
    }

    @SinceVersion(141)
    @DependsOn(AxisAlignedBoundingBoxDrawMode::class)
    class AxisAlignedBoundingBoxDrawMode_ALL : OrderMapper.InClassInitializer.Field(AxisAlignedBoundingBoxDrawMode::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AxisAlignedBoundingBoxDrawMode>() }
    }

    @SinceVersion(141)
    @DependsOn(AxisAlignedBoundingBoxDrawMode::class, AxisAlignedBoundingBoxDrawMode_MOUSE_OVER::class, AxisAlignedBoundingBoxDrawMode_ALL::class)
    class axisAlignedBoundingBoxDrawMode : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<AxisAlignedBoundingBoxDrawMode>() }
                .and { it != field<AxisAlignedBoundingBoxDrawMode_MOUSE_OVER>() }
                .and { it != field<AxisAlignedBoundingBoxDrawMode_ALL>() }
    }

    @SinceVersion(141)
    class drawAxisAlignedBoundingBoxes : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "aabb" }
                .nextWithin(6) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(drawAxisAlignedBoundingBoxes::class, Model.renderAtPoint::class)
    class drawBoundingBoxes2D : UniqueMapper.InMethod.Field(Model.renderAtPoint::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<drawAxisAlignedBoundingBoxes>().id }
                .nextWithin(40) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(Model::class)
    class axisAlignedBoundingBoxContainsMouse : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(type<Model>(), INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    class localPlayerName : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "&u=" }
                .nextWithin(2) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Timer::class)
    class timer : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Timer>() }
    }

    class userHomeDirectory : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "user.home" }
                .nextWithin(3) { it.isField && it.fieldType == String::class.type }
    }

    class osName : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "os.name" }
                .nextWithin(3) { it.isField && it.fieldType == String::class.type }
    }

    @DependsOn(osName::class)
    class osNameLowerCase : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<osName>().id }
                .next { it.isMethod && it.methodName == "toLowerCase" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    class Strings_SPACE : StringsUniqueMapper(" ")
    class Strings_WALK_HERE : StringsUniqueMapper("Walk here")
    class Strings_CANCEL : StringsUniqueMapper("Cancel")
    class Strings_TAKE : StringsUniqueMapper("Take")
    class Strings_EXAMINE : StringsUniqueMapper("Examine")
    class Strings_ATTACK : StringsUniqueMapper("Attack")
    class Strings_DROP : StringsUniqueMapper("Drop")
    class Strings_MORE_OPTIONS : StringsUniqueMapper(" more options")
    class Strings_LEVEL : StringsUniqueMapper("level-")
    class Strings_SKILL : StringsUniqueMapper("skill-")
    class Strings_USE : StringsUniqueMapper("Use")
    class Strings_PREPARED_SOUND_ENGINE : StringsUniqueMapper("Prepared sound engine")
    class Strings_CONNECTING_TO_UPDATE_SERVER : StringsUniqueMapper("Connecting to update server")
    class Strings_STARTING_GAME_ENGINE : StringsUniqueMapper("Starting game engine...")
    class Strings_PREPARED_VISIBILITY_MAP : StringsUniqueMapper("Prepared visibility map")
    class Strings_CHECKING_FOR_UPDATES : StringsUniqueMapper("Checking for updates - ")
    class Strings_LOADED_UPDATE_LIST : StringsUniqueMapper("Loaded update list")
    class Strings_LOADING_FONTS : StringsUniqueMapper("Loading fonts - ")
    class Strings_LOADED_FONTS : StringsUniqueMapper("Loaded fonts")
    class Strings_LOADING_TITLE_SCREEN : StringsUniqueMapper("Loading title screen - ")
    class Strings_LOADED_TITLE_SCREEN : StringsUniqueMapper("Loaded title screen")
    class Strings_LOADING_PLEASE_WAIT : StringsUniqueMapper("Loading - please wait.")
    class Strings_CONNECTION_LOST : StringsUniqueMapper("Connection lost")
    class Strings_ATTEMPTING_TO_REESTABLISH : StringsUniqueMapper("Please wait - attempting to reestablish")
    class Strings_ALREADY_A_FRIEND : StringsUniqueMapper(" is already on your friend list")
    class Strings_CHOOSE_OPTION : StringsUniqueMapper("Choose Option")
    class Strings_PLEASE_WAIT : StringsUniqueMapper("Please wait...")
    class Strings_FROM_IGNORE : StringsUniqueMapper(" from your ignore list first")
    class Strings_LOADED_INPUT_HANDLER : StringsUniqueMapper("Loaded input handler")
    class Strings_LOADED_TEXTURES : StringsUniqueMapper("Loaded textures")
    class Strings_LOADED_CONFIG : StringsUniqueMapper("Loaded config")
    class Strings_LOADED_SPRITES : StringsUniqueMapper("Loaded sprites")
    class Strings_LOADED_WORDPACK : StringsUniqueMapper("Loaded wordpack")
    class Strings_LOADED_INTERFACES : StringsUniqueMapper("Loaded interfaces")
    class Strings_LOADING_SPRITES : StringsUniqueMapper("Loading sprites - ")
    class Strings_LOADING_CONFIG : StringsUniqueMapper("Loading config - ")
    class Strings_LOADING_TEXTURES : StringsUniqueMapper("Loading textures - ")
    class Strings_LOADING_WORDPACK : StringsUniqueMapper("Loading wordpack - ")
    class Strings_LOADING_INTERFACES : StringsUniqueMapper("Loading interfaces - ")
    @SinceVersion(141)
    class Strings_LOADING_WORLD_MAP : StringsUniqueMapper("Loading world map - ")

    @SinceVersion(141)
    class Strings_LOADED_WORLD_MAP : StringsUniqueMapper("Loaded world map")

    @DependsOn(Strings::class)
    class Strings_REMOVE_FRIEND : OrderMapper.InClassInitializer.Field(Strings::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please remove " }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_REMOVE_IGNORE : OrderMapper.InClassInitializer.Field(Strings::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please remove " }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Widget::class, Widget.children::class)
    class getWidgetChild : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Widget>() }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Widget.children>().id } }
    }

    @DependsOn(MenuAction::class)
    class tempMenuAction : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MenuAction>() }
    }

    @SinceVersion(141)
    @DependsOn(IterableNodeHashTable::class)
    class messages : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>() }
    }

    @SinceVersion(141)
    @DependsOn(Client::class)
    class client : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Client>() }
    }

    @DependsOn(Widget.getFont::class, EvictingHashTable::class)
    class cachedWidgetFonts : UniqueMapper.InMethod.Field(Widget.getFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(Widget.getModel::class, EvictingHashTable::class)
    class cachedWidgetModels : UniqueMapper.InMethod.Field(Widget.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @SinceVersion(141)
    @DependsOn(BaseVarType::class)
    class BaseVarType_INTEGER : OrderMapper.InClassInitializer.Field(BaseVarType::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BaseVarType>() }
    }

    @SinceVersion(141)
    @DependsOn(BaseVarType::class)
    class BaseVarType_LONG : OrderMapper.InClassInitializer.Field(BaseVarType::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BaseVarType>() }
    }

    @SinceVersion(141)
    @DependsOn(BaseVarType::class)
    class BaseVarType_STRING : OrderMapper.InClassInitializer.Field(BaseVarType::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BaseVarType>() }
    }

    @DependsOn(JagexGame::class)
    class jagexGame : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<JagexGame>() && it.klass != klass<JagexGame>() }
    }

    @DependsOn(JagexGame0::class)
    class jagexGame0 : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<JagexGame0>() && it.klass != klass<JagexGame0>() }
    }

    @DependsOn(BufferedFile::class)
    class randomDat : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "random.dat" }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == type<BufferedFile>() }
    }

    @DependsOn(BufferedFile::class)
    class dat2File : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.dat2" }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == type<BufferedFile>() }
    }

    @DependsOn(BufferedFile::class)
    class idx255File : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.idx255" }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == type<BufferedFile>() }
    }

    @DependsOn(BufferedFile::class)
    class idxFiles : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.idx" }
                .prevWithin(25) { it.opcode == GETSTATIC && it.fieldType == type<BufferedFile>().withDimensions(1) }
    }

    class idxCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.idx255" }
                .nextWithin(25) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(Widget::class)
    class widgetMethod0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<Widget>()) }
    }

    @MethodParameters("id")
    @SinceVersion(141)
    @DependsOn(widgetMethod0::class)
    class getWidget : UniqueMapper.InMethod.Method(widgetMethod0::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.jar[it.methodId].arguments.size in 1..2 }
    }

    class jagexClDat : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst is String && (it.ldcCst as String).contains("jagex_cl_") }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == File::class.type }
    }

    @DependsOn(Track::class)
    class tracks : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Track>().withDimensions(1) }
    }

    @DependsOn(TaskHandler::class)
    class javaVendor : OrderMapper.InConstructor.Field(TaskHandler::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(TaskHandler::class)
    class javaVersion : OrderMapper.InConstructor.Field(TaskHandler::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @SinceVersion(141)
    @DependsOn(TaskDataProvider::class)
    class soundTaskDataProvider : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<TaskDataProvider>() }
    }

    @DependsOn(Task0::class)
    class soundTask0 : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Task0>() }
    }

    class isLowDetail : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Mem:" }
                .prevWithin(30) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    // else mono
    @DependsOn(SoundTaskData.available::class)
    class isStereo : UniqueMapper.InMethod.Field(SoundTaskData.available::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
        // sometimes accesses static field through the subclass
        override fun resolve(instruction: Instruction2): Field2 {
            return if (instruction.jar.contains(instruction.fieldId)) {
                instruction.jar[instruction.fieldId]
            } else {
                val superType = instruction.jar[instruction.fieldOwner].superType
                instruction.jar[superType to instruction.fieldName]
            }
        }
    }

    class getColorStartTag : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "<col=" } }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == ">" } }
    }

    class colorEndTag : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "</col>" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    class lineBreakTag : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "<br>" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    class rightParenthesis : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == ")" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    class spaceLeftParenthesis : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == " (" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    class rightArrow : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "->" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(IndexStore::class)
    class indexStore255 : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexStore>() }
    }

//    @DependsOn(ClientPreferences::class)
//    class readClientPreferences : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<ClientPreferences>() }
//    }

    @SinceVersion(141)
    @DependsOn(MouseWheel.useRotation::class)
    class mouseWheelRotation : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<MouseWheel.useRotation>().id }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mouseMoved::class)
    class MouseHandler_x : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mouseMoved::class)
    class MouseHandler_y : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedX : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedY : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedTimeMillis : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastButton : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_currentButton : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_lastPressedX::class)
    class mouseLastPressedX : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedX>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_lastPressedY::class)
    class mouseLastPressedY : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedY>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_currentButton::class)
    class mouseCurrentButton : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_lastButton::class)
    class mouseLastButton : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastButton>().id }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .and { it.klass != klass<MouseHandler>() }
    }

    @DependsOn(MouseHandler_lastPressedTimeMillis::class)
    class mouseLastPressedTimeMillis : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedTimeMillis>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(mouseLastPressedTimeMillis::class)
    class mouseLastLastPressedTimeMillis : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<mouseLastPressedTimeMillis>().id }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_PLAIN11 : OrderMapper.InClassInitializer.Field(FontName::class, 0, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_PLAIN12 : OrderMapper.InClassInitializer.Field(FontName::class, 1, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_BOLD12 : OrderMapper.InClassInitializer.Field(FontName::class, 2, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_VERDANA11 : OrderMapper.InClassInitializer.Field(FontName::class, 3, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_VERDANA13 : OrderMapper.InClassInitializer.Field(FontName::class, 4, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_VERDANA15 : OrderMapper.InClassInitializer.Field(FontName::class, 5, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

//    @SinceVersion(141)
//    @DependsOn(FontName::class)
//    class FontName_values : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<FontName>().withDimensions(1) }
//    }

    @SinceVersion(141)
    @DependsOn(Fonts::class)
    class fonts : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Fonts>() }
    }

    @SinceVersion(141)
    class fontsMap : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == HashMap::class.type }
    }

    @DependsOn(IndexCache::class)
    class newIndexCache : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<IndexCache>() }
    }

    class indexCache0 : IndexCacheFieldMapper(0)
    class indexCache1 : IndexCacheFieldMapper(1)
    class indexCache2 : IndexCacheFieldMapper(2)
    class indexCache3 : IndexCacheFieldMapper(3)
    class indexCache4 : IndexCacheFieldMapper(4)
    class indexCache5 : IndexCacheFieldMapper(5)
    class indexCache6 : IndexCacheFieldMapper(6)
    class indexCache7 : IndexCacheFieldMapper(7)
    class indexCache8 : IndexCacheFieldMapper(8)
    class indexCache9 : IndexCacheFieldMapper(9)
    class indexCache10 : IndexCacheFieldMapper(10)
    class indexCache11 : IndexCacheFieldMapper(11)
    class indexCache12 : IndexCacheFieldMapper(12)
    class indexCache13 : IndexCacheFieldMapper(13)
    class indexCache14 : IndexCacheFieldMapper(14)
    class indexCache15 : IndexCacheFieldMapper(15)
    @SinceVersion(141)
    class indexCache16 : IndexCacheFieldMapper(16)

    @DependsOn(MouseTracker::class)
    class mouseTracker : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseTracker>() }
    }

    @SinceVersion(141)
    @DependsOn(Font::class, FontName_PLAIN11::class)
    class fontPlain11 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_PLAIN11>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @SinceVersion(141)
    @DependsOn(Font::class, FontName_PLAIN12::class)
    class fontPlain12 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_PLAIN12>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @SinceVersion(141)
    @DependsOn(Font::class, FontName_BOLD12::class)
    class fontBold12 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_BOLD12>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName_VERDANA11::class, FontName::class)
    class fontNameVerdana11 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_VERDANA11>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName_VERDANA13::class, FontName::class)
    class fontNameVerdana13 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_VERDANA13>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName_VERDANA15::class, FontName::class)
    class fontNameVerdana15 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_VERDANA15>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(IndexStore::class)
    class IndexStore_buffer : UniqueMapper.InClassInitializer.Field(IndexStore::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 520 }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == ByteArray::class.type }
    }

    @DependsOn(IndexStoreActionHandler::class)
    class IndexStoreActionHandler_lock : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type }
                .and { it.klass == klass<IndexStoreActionHandler>() }
    }

    @DependsOn(IndexStoreActionHandler::class, NodeDeque::class)
    class IndexStoreActionHandler_requestQueue : OrderMapper.InClassInitializer.Field(IndexStoreActionHandler::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @DependsOn(IndexStoreActionHandler::class, NodeDeque::class)
    class IndexStoreActionHandler_responseQueue : OrderMapper.InClassInitializer.Field(IndexStoreActionHandler::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @SinceVersion(141)
    @DependsOn(IndexStoreActionHandler::class)
    class IndexStoreActionHandler_thread : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<IndexStoreActionHandler>() }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == Thread::class.type }
    }

    @DependsOn(IndexCache.load::class)
    class IndexCache_crc : UniqueMapper.InMethod.Field(IndexCache.load::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == CRC32::class.type }
    }

    class byteArrayToObject : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Any::class.type }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    class byteArrayFromObject : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(Any::class.type) }
    }

    @DependsOn(byteArrayToObject::class)
    class directBufferUnavailable : UniqueMapper.InMethod.Field(byteArrayToObject::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(IndexCache::class)
    class NetCache_indexCaches : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexCache>().withDimensions(1) }
    }

    @DependsOn(NetCache::class)
    class NetCache_crc : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<NetCache>() }
                .and { it.type == CRC32::class.type }
    }

    @DependsOn(IndexCache::class)
    class requestNetFile : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<IndexCache>(), INT_TYPE, INT_TYPE, INT_TYPE, BYTE_TYPE, BOOLEAN_TYPE) }
    }

    @DependsOn(NodeHashTable::class, requestNetFile::class)
    class NetCache_pendingPriorityWrites : OrderMapper.InMethod.Field(requestNetFile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(NodeHashTable::class, requestNetFile::class)
    class NetCache_pendingPriorityResponses : OrderMapper.InMethod.Field(requestNetFile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(NodeHashTable::class, requestNetFile::class)
    class NetCache_pendingWrites : OrderMapper.InMethod.Field(requestNetFile::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(NodeHashTable::class, requestNetFile::class)
    class NetCache_pendingResponses : OrderMapper.InMethod.Field(requestNetFile::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(DualNodeDeque::class, requestNetFile::class)
    class NetCache_pendingWritesQueue : UniqueMapper.InMethod.Field(requestNetFile::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<DualNodeDeque>() }
    }

    @DependsOn(NodeHashTable::class, requestNetFile::class)
    class NetCache_pendingWritesCount : OrderMapper.InMethod.Field(requestNetFile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(NodeHashTable::class, requestNetFile::class)
    class NetCache_pendingPriorityWritesCount : OrderMapper.InMethod.Field(requestNetFile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(NetSocket::class, NetCache_pendingPriorityWritesCount::class)
    class NetCache_socket : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingPriorityWritesCount>().id }
                .prevWithin(3) { it.opcode == ISUB }
                .prevWithin(20) { it.opcode == GETSTATIC && it.fieldType == type<NetSocket>() }
    }

    @DependsOn(NetSocket::class, NetCache_pendingPriorityWritesCount::class)
    class NetCache_pendingPriorityResponsesCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == ISUB }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingPriorityWritesCount>().id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(NetSocket::class, NetCache_pendingWritesCount::class, NetCache_pendingPriorityWritesCount::class)
    class NetCache_pendingResponsesCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == ISUB }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingWritesCount>().id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE && it.fieldId != field<NetCache_pendingPriorityWritesCount>().id }
    }

    class currentTimeMillis : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { Modifier.isSynchronized(it.access) }
    }

    @DependsOn(currentTimeMillis::class)
    class currentTimeMillisLast : OrderMapper.InMethod.Field(currentTimeMillis::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @DependsOn(currentTimeMillis::class)
    class currentTimeMillisOffset : OrderMapper.InMethod.Field(currentTimeMillis::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @DependsOn(AbstractIndexCache.takeRecordByNames::class)
    class hashString : UniqueMapper.InMethod.Method(AbstractIndexCache.takeRecordByNames::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(hashString::class)
    class charToByteCp1252 : UniqueMapper.InMethod.Method(hashString::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(Huffman::class)
    class huffman : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Huffman>() }
    }

    @DependsOn(NetFileRequest::class)
    class NetCache_currentResponse : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<NetFileRequest>() }
    }

    @DependsOn(Buffer::class, NetCache::class)
    class NetCache_responseHeaderBuffer : UniqueMapper.InClassInitializer.Field(NetCache::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 8 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == type<Buffer>() }
    }

    @DependsOn(Buffer::class, NetCache_crc::class)
    class NetCache_responseArchiveBuffer : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<NetCache_crc>().id }
                .nextWithin(3) { it.isMethod && it.methodId == CRC32::reset.id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == type<Buffer>() }
    }

    @DependsOn(Bzip2State::class)
    class Bzip2Decompressor_state : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Bzip2State>() }
    }

    @MethodParameters("src", "srcStart", "srcEnd", "dst", "dstStart")
    class encodeStringCp1252 : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.startsWith(CharSequence::class.type, INT_TYPE, INT_TYPE, ByteArray::class.type, INT_TYPE) }
    }

    @MethodParameters("src", "srcStart", "length")
    @DependsOn(Buffer.readStringCp1252NullTerminated::class)
    class decodeStringCp1252 : UniqueMapper.InMethod.Method(Buffer.readStringCp1252NullTerminated::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(decodeStringCp1252::class)
    class cp1252AsciiExtension : UniqueMapper.InMethod.Field(decodeStringCp1252::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @DependsOn(gzipDecompressor::class)
    class decompressBytes : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(ByteArray::class.type) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<gzipDecompressor>().id } }
    }

    @DependsOn(decompressBytes::class)
    class Bzip2Decompressor_decompress : UniqueMapper.InMethod.Method(decompressBytes::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @SinceVersion(154)
    @DependsOn(IndexCache.loadIndexReference::class, Buffer::class)
    class NetCache_reference : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<IndexCache.loadIndexReference>().id }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == type<Buffer>() }
    }

    @DependsOn(KeyHandler.keyPressed::class)
    class KeyHandler_keyCodes : OrderMapper.InMethod.Field(KeyHandler.keyPressed::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(ClientPreferences.windowMode::class)
    class setUp : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<ClientPreferences.windowMode>().id } }
    }

    @DependsOn(setUp::class)
    class port1 : OrderMapper.InMethod.Field(setUp::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(setUp::class)
    class port2 : OrderMapper.InMethod.Field(setUp::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(setUp::class)
    class port3 : OrderMapper.InMethod.Field(setUp::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // normal, rc, qa, wip, i, local
    @DependsOn(setUp::class)
    class gameBuild : OrderMapper.InMethod.Field(setUp::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(ServerBuild::class)
    class serverBuild : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<ServerBuild>() && it.klass != klass<ServerBuild>() }
    }

    @SinceVersion(141)
    class viewportMouseX : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 50 }
                .nextIn(2) { it.opcode == SIPUSH && it.intOperand == 3500 }
                .nextWithin(5) { it.opcode == ISTORE }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(viewportMouseX::class)
    class viewportMouseY : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 50 }
                .nextIn(2) { it.opcode == SIPUSH && it.intOperand == 3500 }
                .nextWithin(20) { it.opcode == GETSTATIC && it.fieldId == field<viewportMouseX>().id }
                .nextWithin(10) { it.opcode == ISTORE }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(viewportMouseY::class)
    class viewportContainsMouse : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<viewportMouseY>().id }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(viewportMouseY::class)
    class viewportFalse0 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<viewportMouseY>().id }
                .nextWithin(5) { it.opcode == ICONST_0 }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }
}