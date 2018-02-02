package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.std.*
import org.kxtra.lang.list.startsWith
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.objectweb.asm.tree.JumpInsnNode
import java.applet.Applet
import java.io.File
import java.lang.management.GarbageCollectorMXBean
import java.lang.reflect.Modifier
import java.net.URL
import java.util.*
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

    @SinceVersion(141)
    @DependsOn(OverlayDefinition::class)
    class getOverlayDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<OverlayDefinition>() }
    }

    @DependsOn(UnderlayDefinition::class)
    class getUnderlayDefinition : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<UnderlayDefinition>() }
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

//    @SinceVersion(148)
//    @DependsOn(EnumDefinition::class)
//    class getEnumDefinition : StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<EnumDefinition>() }
//    }

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
                .and { it.instructions.any { it.opcode == SIPUSH && it.intOperand == 13056 } }
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
    class cameraY : OrderMapper.InMethod.Field(worldToScreen::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class cameraZ : OrderMapper.InMethod.Field(worldToScreen::class, 3) {
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
    class mouseCrossX : UniqueMapper.InMethod.Field(menuAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(7) {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(menuAction::class)
    class mouseCrossY : UniqueMapper.InMethod.Field(menuAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(7) {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(7) {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // 0 none, 1 yellow, 2 red
    @DependsOn(menuAction::class)
    class mouseCrossColor : UniqueMapper.InMethod.Field(menuAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(20) { it.opcode == ICONST_0 }
                .prev {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // 0 - 400
    @DependsOn(menuAction::class)
    class mouseCrossState : UniqueMapper.InMethod.Field(menuAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(20) { it.opcode == ICONST_0 }
                .next {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
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
    class Players_regions : OrderMapper.InMethod.Field(updateExternalPlayer::class, -1) {
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

//    @DependsOn(ClanMate::class)
//    class clanChat : StaticField() {
//        override val predicate = predicateOf<Field2> { it.type == type<ClanMate>().withDimensions(1) }
//    }

//    @DependsOn(Ignored::class)
//    class ignoreList : StaticField() {
//        override val predicate = predicateOf<Field2> { it.type == type<Ignored>().withDimensions(1) }
//    }

//    @DependsOn(Friend::class)
//    class friendsList : StaticField() {
//        override val predicate = predicateOf<Field2> { it.type == type<Friend>().withDimensions(1) }
//    }

    @DependsOn(MouseHandler::class)
    class MouseHandler_x : OrderMapper.InClassInitializer.Field(MouseHandler::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(MouseHandler::class)
    class MouseHandler_y : OrderMapper.InClassInitializer.Field(MouseHandler::class, 7) {
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

    class Rasterizer3D_sine : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.method.isClassInitializer }
                .and { it.opcode == INVOKESTATIC && it.methodId == Math::sin.id }
                .prevWithin(7) { it.opcode == LDC && it.ldcCst == 65536.0 }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    class Rasterizer3D_cosine : StaticUniqueMapper.Field() {
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
    class NpcDefinition_cached : CachedDefinitionMapper(NpcDefinition::class)

    @DependsOn(EnumDefinition::class, EvictingHashTable::class)
    class EnumDefinition_cached : CachedDefinitionMapper(EnumDefinition::class)

    @DependsOn(Sprite::class, EvictingHashTable::class)
    class Sprite_cached : CachedDefinitionMapper(Sprite::class)

    @DependsOn(KitDefinition::class, EvictingHashTable::class)
    class KitDefinition_cached : CachedDefinitionMapper(KitDefinition::class)

    @SinceVersion(141)
    @DependsOn(OverlayDefinition::class, EvictingHashTable::class)
    class OverlayDefinition_cached : CachedDefinitionMapper(OverlayDefinition::class)

    @DependsOn(UnderlayDefinition::class, EvictingHashTable::class)
    class UnderlayDefinition_cached : CachedDefinitionMapper(UnderlayDefinition::class)

    @DependsOn(SpotAnimationDefinition::class, EvictingHashTable::class)
    class SpotAnimationDefinition_cached : CachedDefinitionMapper(SpotAnimationDefinition::class)

    @DependsOn(ItemDefinition::class, EvictingHashTable::class)
    class ItemDefinition_cached : CachedDefinitionMapper(ItemDefinition::class)

    @DependsOn(VarbitDefinition::class, EvictingHashTable::class)
    class VarbitDefinition_cached : CachedDefinitionMapper(VarbitDefinition::class)

    @DependsOn(SequenceDefinition::class, EvictingHashTable::class)
    class SequenceDefinition_cached : CachedDefinitionMapper(SequenceDefinition::class)

    @DependsOn(ObjectDefinition::class, EvictingHashTable::class)
    class ObjectDefinition_cached : CachedDefinitionMapper(ObjectDefinition::class)

    @DependsOn(GzipDecompressor::class, EvictingHashTable::class)
    class gzipDecompressor : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GzipDecompressor>() }
    }

    @DependsOn(PlatformInfo::class)
    class platformInfo : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PlatformInfo>() }
    }

//    class clanChatOwner : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3625 }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
//    }

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

    class Login_username : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == String::class.type }
                .next { it.isMethod && it.methodName == "trim" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
                .next { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Login_username::class)
    class Login_password : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "" }
                .next { it.opcode == PUTSTATIC && it.fieldId == field<Login_username>().id }
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

    @DependsOn(AttackOption::class)
    class AttackOption_dependsOnCombatLevels : OrderMapper.InClassInitializer.Field(AttackOption::class, 0, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AttackOption>() }
    }

    @DependsOn(AttackOption::class)
    class AttackOption_alwaysRightClick : OrderMapper.InClassInitializer.Field(AttackOption::class, 1, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AttackOption>() }
    }

    @DependsOn(AttackOption::class)
    class AttackOption_leftClickWhereAvailable : OrderMapper.InClassInitializer.Field(AttackOption::class, 2, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AttackOption>() }
    }

    @DependsOn(AttackOption::class)
    class AttackOption_hidden : OrderMapper.InClassInitializer.Field(AttackOption::class, 3, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AttackOption>() }
    }

//    @SinceVersion(141)
//    @DependsOn(BoundingBox2D::class)
//    class addBoundingBox2D : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.instructions.any { it.isMethod && it.methodOwner == type<BoundingBox2D>() && it.methodName == Method2.CONSTRUCTOR_NAME } }
//    }

//    @SinceVersion(145)
//    @DependsOn(BoundingBox3D::class)
//    class addAxisAlignedBoundingBox : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.instructions.any { it.isMethod && it.methodOwner == type<BoundingBox3D>() && it.methodName == "<init>" } }
//    }

    @SinceVersion(141)
    @DependsOn(BoundingBoxes::class, IterableNodeDeque::class)
    class BoundingBoxes_deque : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<BoundingBoxes>() }
                .and { it.type == type<IterableNodeDeque>() }
    }

    @SinceVersion(141)
    @DependsOn(BoundingBox3DDrawMode::class)
    class BoundingBox3DDrawMode_mouseOver : OrderMapper.InClassInitializer.Field(BoundingBox3DDrawMode::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BoundingBox3DDrawMode>() }
    }

    @SinceVersion(141)
    @DependsOn(BoundingBox3DDrawMode::class)
    class BoundingBox3DDrawMode_all : OrderMapper.InClassInitializer.Field(BoundingBox3DDrawMode::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BoundingBox3DDrawMode>() }
    }

    @SinceVersion(141)
    @DependsOn(BoundingBox3DDrawMode::class, BoundingBox3DDrawMode_mouseOver::class, BoundingBox3DDrawMode_all::class)
    class BoundingBoxes_3DDrawMode : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<BoundingBox3DDrawMode>() }
                .and { it != field<BoundingBox3DDrawMode_mouseOver>() }
                .and { it != field<BoundingBox3DDrawMode_all>() }
    }

    @SinceVersion(141)
    class BoundingBoxes_draw3D : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "aabb" }
                .nextWithin(6) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(BoundingBoxes_draw3D::class, Model.draw::class)
    class BoundingBoxes_draw2D : UniqueMapper.InMethod.Field(Model.draw::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<BoundingBoxes_draw3D>().id }
                .nextWithin(40) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(Model::class)
    class boundingBox3DContainsMouse : IdentityMapper.StaticMethod() {
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

    class Strings_space : StringsUniqueMapper(" ")
    class Strings_walkHere : StringsUniqueMapper("Walk here")
    class Strings_cancel : StringsUniqueMapper("Cancel")
    class Strings_take : StringsUniqueMapper("Take")
    class Strings_close : StringsUniqueMapper("Close")
    class Strings_examine : StringsUniqueMapper("Examine")
    class Strings_attack : StringsUniqueMapper("Attack")
    class Strings_drop : StringsUniqueMapper("Drop")
    class Strings_unableToFind : StringsUniqueMapper("Unable to find ")
    class Strings_fromYourFriendListFirst : StringsUniqueMapper(" from your friend list first")
    class Strings_ok : StringsUniqueMapper("Ok")
    class Strings_select : StringsUniqueMapper("Select")
    class Strings_continue : StringsUniqueMapper("Continue")
    class Strings_moreOptions : StringsUniqueMapper(" more options")
    class Strings_hasLoggedIn : StringsUniqueMapper(" has logged in.")
    class Strings_hasLoggedOut : StringsUniqueMapper(" has logged out.")
    class Strings_level : StringsUniqueMapper("level-")
    class Strings_skill : StringsUniqueMapper("skill-")
    class Strings_use : StringsUniqueMapper("Use")
    class Strings_yourIgnoreListIsFull : StringsUniqueMapper("Your ignore list is full. Max of 100 for free users, and 400 for members")
    class Strings_yourFriendListIsFull : StringsUniqueMapper("Your friend list is full. Max of 200 for free users, and 400 for members")
    class Strings_connectingToServer : StringsUniqueMapper("Connecting to server...")
    class Strings_login : StringsUniqueMapper("Login: ")
    class Strings_password : StringsUniqueMapper("Password: ")
    class Strings_pin : StringsUniqueMapper("PIN: ")
    class Strings_pleaseEnterYourUsername : StringsUniqueMapper("Please enter your username/email address.")
    class Strings_pleaseEnterYourPassword : StringsUniqueMapper("Please enter your password.")
    class Strings_welcomeToRuneScape : StringsUniqueMapper("Welcome to RuneScape")
    class Strings_newUser : StringsUniqueMapper("New User")
    class Strings_existingUser : StringsUniqueMapper("Existing User")
    class Strings_almostEverywhere : StringsUniqueMapper("almost everywhere.")
    class Strings_warning : StringsUniqueMapper("Warning!")
    class Strings_thisIsAHighRiskWorld : StringsUniqueMapper("This is a <col=ffff00>High Risk<col=ffffff> world.")
    class Strings_theProtectItemPrayerWill : StringsUniqueMapper("The Protect Item prayer will")
    class Strings_notWorkOnThisWorld : StringsUniqueMapper("not work on this world.")
    class Strings_thisIsAHighRiskPvpWorld : StringsUniqueMapper("This is a <col=ffff00>High Risk <col=ff0000>PvP<col=ffffff> world.")
    class Strings_playersCanAttackEachOtherAlmostEverywhere : StringsUniqueMapper("Players can attack each other almost everywhere")
    class Strings_andTheProtectItemPrayerWontWork : StringsUniqueMapper("and the Protect Item prayer won't work.")
    class Strings_thisIsABetaWorld : StringsUniqueMapper("This is a <col=00ffff>Beta<col=ffffff> world.")
    class Strings_yourNormalAccountWillNotBeAffected : StringsUniqueMapper("Your normal account will not be affected.")
    class Strings_thisIsAPvpWorld : StringsUniqueMapper("This is a <col=ff0000>PvP<col=ffffff> world.")
    class Strings_playersCanAttackEachOther : StringsUniqueMapper("Players can attack each other")
    class Strings_preparedSoundEngine : StringsUniqueMapper("Prepared sound engine")
    class Strings_connectingToUpdateServer : StringsUniqueMapper("Connecting to update server")
    class Strings_startingGameEngine : StringsUniqueMapper("Starting game engine...")
    class Strings_preparedVisibilityMap : StringsUniqueMapper("Prepared visibility map")
    class Strings_checkingForUpdates : StringsUniqueMapper("Checking for updates - ")
    class Strings_loadedUpdateList : StringsUniqueMapper("Loaded update list")
    class Strings_loadingFonts : StringsUniqueMapper("Loading fonts - ")
    class Strings_loadedFonts : StringsUniqueMapper("Loaded fonts")
    class Strings_loadingTitleScreen : StringsUniqueMapper("Loading title screen - ")
    class Strings_loadedTitleScreen : StringsUniqueMapper("Loaded title screen")
    class Strings_loadingPleaseWait : StringsUniqueMapper("Loading - please wait.")
    class Strings_connectionLost : StringsUniqueMapper("Connection lost")
    class Strings_pleaseWaitAttemptingToReestablish : StringsUniqueMapper("Please wait - attempting to reestablish")
    class Strings_isAlreadyOnYourFriendList : StringsUniqueMapper(" is already on your friend list")
    class Strings_chooseOption : StringsUniqueMapper("Choose Option")
    class Strings_pleaseWait : StringsUniqueMapper("Please wait...")
    class Strings_fromYourIgnoreListFirst : StringsUniqueMapper(" from your ignore list first")
    class Strings_loadedInputHandler : StringsUniqueMapper("Loaded input handler")
    class Strings_loadedTextures : StringsUniqueMapper("Loaded textures")
    class Strings_loadedConfig : StringsUniqueMapper("Loaded config")
    class Strings_loadedSprites : StringsUniqueMapper("Loaded sprites")
    class Strings_loadedWordpack : StringsUniqueMapper("Loaded wordpack")
    class Strings_loadedInterfaces : StringsUniqueMapper("Loaded interfaces")
    class Strings_loadingSprites : StringsUniqueMapper("Loading sprites - ")
    class Strings_loadingConfig : StringsUniqueMapper("Loading config - ")
    class Strings_loadingTextures : StringsUniqueMapper("Loading textures - ")
    class Strings_loadingWordpack : StringsUniqueMapper("Loading wordpack - ")
    class Strings_loadingInterfaces : StringsUniqueMapper("Loading interfaces - ")
    @SinceVersion(141)
    class Strings_loadingWorldMap : StringsUniqueMapper("Loading world map - ")
    @SinceVersion(141)
    class Strings_loadedWorldMap : StringsUniqueMapper("Loaded world map")
    @DependsOn(Strings::class)
    class Strings_pleaseRemoveFriend : OrderMapper.InClassInitializer.Field(Strings::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please remove " }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }
    @DependsOn(Strings::class)
    class Strings_pleaseRemoveIgnore : OrderMapper.InClassInitializer.Field(Strings::class, 0, 2) {
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
    class Widget_cachedFonts : UniqueMapper.InMethod.Field(Widget.getFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(Widget.getModel::class, EvictingHashTable::class)
    class Widget_cachedModels : UniqueMapper.InMethod.Field(Widget.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @SinceVersion(141)
    @DependsOn(BaseVarType::class)
    class BaseVarType_integer : OrderMapper.InClassInitializer.Field(BaseVarType::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BaseVarType>() }
    }

    @SinceVersion(141)
    @DependsOn(BaseVarType::class)
    class BaseVarType_long : OrderMapper.InClassInitializer.Field(BaseVarType::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BaseVarType>() }
    }

    @SinceVersion(141)
    @DependsOn(BaseVarType::class)
    class BaseVarType_string : OrderMapper.InClassInitializer.Field(BaseVarType::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<BaseVarType>() }
    }

    @DependsOn(JagexGame::class)
    class jagexGame : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<JagexGame>() && it.klass != klass<JagexGame>() }
    }

    @DependsOn(LoginType::class)
    class loginType : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<LoginType>() && it.klass != klass<LoginType>() }
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
    @DependsOn(AbstractSoundSystemProvider::class)
    class soundSystemProvider : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractSoundSystemProvider>() }
    }

    @DependsOn(SoundSystems::class)
    class soundSystems : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<SoundSystems>() }
    }

    class isLowDetail : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Mem:" }
                .prevWithin(30) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    // else mono
    @DependsOn(SoundSystem.remaining::class)
    class isStereo : UniqueMapper.InMethod.Field(SoundSystem.remaining::class) {
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

    @MethodParameters("color")
    class colorStartTag : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "<col=" } }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == ">" } }
    }

    class Formatting_colorEndTag : FormattingUniqueMapper("</col>")
    class Formatting_lineBreakTag : FormattingUniqueMapper("<br>")
    class Formatting_rightParenthesis : FormattingUniqueMapper(")")
    class Formatting_spaceLeftParenthesis : FormattingUniqueMapper(" (")
    class Formatting_true : FormattingUniqueMapper("true")
    class Formatting_rightArrow : FormattingUniqueMapper("->")
    class Formatting_comma : FormattingUniqueMapper(",")
    class Formatting_pipe : FormattingUniqueMapper("|")


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
    class MouseHandler_x0 : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mouseMoved::class)
    class MouseHandler_y0 : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedX0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedY0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedTimeMillis0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastButton0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_currentButton0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_lastPressedX0::class)
    class MouseHandler_lastPressedX : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedX0>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_lastPressedY0::class)
    class MouseHandler_lastPressedY : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedY0>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_currentButton0::class)
    class MouseHandler_currentButton : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton0>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_lastButton0::class)
    class MouseHandler_lastButton : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastButton0>().id }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .and { it.klass != klass<MouseHandler>() }
    }

    @DependsOn(MouseHandler_lastPressedTimeMillis0::class)
    class MouseHandler_lastPressedTimeMillis : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedTimeMillis0>().id }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(MouseHandler_lastPressedTimeMillis::class)
    class mouseLastLastPressedTimeMillis : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_lastPressedTimeMillis>().id }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_plain11 : OrderMapper.InClassInitializer.Field(FontName::class, 0, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_plain12 : OrderMapper.InClassInitializer.Field(FontName::class, 1, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_bold12 : OrderMapper.InClassInitializer.Field(FontName::class, 2, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_verdana11 : OrderMapper.InClassInitializer.Field(FontName::class, 3, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_verdana13 : OrderMapper.InClassInitializer.Field(FontName::class, 4, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName::class)
    class FontName_verdana15 : OrderMapper.InClassInitializer.Field(FontName::class, 5, 6) {
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

    @DependsOn(MouseRecorder::class)
    class mouseRecorder : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseRecorder>() }
    }

    @SinceVersion(141)
    @DependsOn(Font::class, FontName_plain11::class)
    class fontPlain11 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_plain11>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @SinceVersion(141)
    @DependsOn(Font::class, FontName_plain12::class)
    class fontPlain12 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_plain12>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @SinceVersion(141)
    @DependsOn(Font::class, FontName_bold12::class)
    class fontBold12 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_bold12>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName_verdana11::class, FontName::class)
    class fontNameVerdana11 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_verdana11>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName_verdana13::class, FontName::class)
    class fontNameVerdana13 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_verdana13>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @SinceVersion(141)
    @DependsOn(FontName_verdana15::class, FontName::class)
    class fontNameVerdana15 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_verdana15>().id }
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

    @MethodParameters("bytes", "copyArray")
    class byteArrayToObject : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Any::class.type }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    @MethodParameters("o", "copyArray")
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

    @SinceVersion(160)
    @DependsOn(AbstractChannel::class, NetCache_pendingPriorityWritesCount::class)
    class NetCache_socket : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingPriorityWritesCount>().id }
                .prevWithin(3) { it.opcode == ISUB }
                .prevWithin(20) { it.opcode == GETSTATIC && it.fieldType == type<AbstractChannel>() }
    }

    @DependsOn(NetCache_pendingPriorityWritesCount::class)
    class NetCache_pendingPriorityResponsesCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == ISUB }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingPriorityWritesCount>().id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(NetCache_pendingWritesCount::class, NetCache_pendingPriorityWritesCount::class)
    class NetCache_pendingResponsesCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == ISUB }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingWritesCount>().id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE && it.fieldId != field<NetCache_pendingPriorityWritesCount>().id }
    }

    @MethodParameters()
    class currentTimeMs : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { Modifier.isSynchronized(it.access) }
    }

    @DependsOn(currentTimeMs::class)
    class currentTimeMsLast : OrderMapper.InMethod.Field(currentTimeMs::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @DependsOn(currentTimeMs::class)
    class currentTimeMsOffset : OrderMapper.InMethod.Field(currentTimeMs::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @MethodParameters("chars")
    @DependsOn(AbstractIndexCache.takeRecordByNames::class)
    class hashString : UniqueMapper.InMethod.Method(AbstractIndexCache.takeRecordByNames::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @MethodParameters("c")
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

    @MethodParameters()
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
    class ViewportMouse_x : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 50 }
                .nextIn(2) { it.opcode == SIPUSH && it.intOperand == 3500 }
                .nextWithin(5) { it.opcode == ISTORE }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(ViewportMouse_x::class)
    class ViewportMouse_y : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 50 }
                .nextIn(2) { it.opcode == SIPUSH && it.intOperand == 3500 }
                .nextWithin(20) { it.opcode == GETSTATIC && it.fieldId == field<ViewportMouse_x>().id }
                .nextWithin(10) { it.opcode == ISTORE }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(ViewportMouse_y::class)
    class ViewportMouse_isInViewport : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<ViewportMouse_y>().id }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(ViewportMouse_y::class)
    class ViewportMouse_entityCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<ViewportMouse_y>().id }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
                .next { it.opcode == ICONST_0 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @SinceVersion(141)
    @DependsOn(ViewportMouse_y::class)
    class ViewportMouse_false0 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<ViewportMouse_y>().id }
                .nextWithin(5) { it.opcode == ICONST_0 }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_zoom : UniqueMapper.InClassInitializer.Field(Rasterizer3D::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 512 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // nullable, includes color tags
    @DependsOn(Strings_use::class)
    class lastSelectedItemName : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_use>().id }
                .nextIn(4) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Widget.spellName::class)
    class lastSelectedSpellName : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Widget.spellName>().id }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    // "Cast"
    @DependsOn(menuAction::class)
    class lastSelectedSpellActionName : UniqueMapper.InMethod.Field(menuAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Null" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @MethodParameters("descriptor")
    class loadClassFromDescriptor : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == java.lang.Class::class.type }
    }

    @DependsOn(Track::class)
    class newTrack : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Track>() }
    }

    @DependsOn(Strings_thisIsAPvpWorld::class)
    class Login_response1 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_thisIsAPvpWorld>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings_playersCanAttackEachOther::class)
    class Login_response2 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_playersCanAttackEachOther>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings_almostEverywhere::class)
    class Login_response3 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_almostEverywhere>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings_warning::class)
    class Login_response0 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_warning>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(getObjectDefinition::class, AbstractIndexCache::class)
    class ObjectDefinition_indexCache : UniqueMapper.InMethod.Field(getObjectDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(getItemDefinition::class, AbstractIndexCache::class)
    class ItemDefinition_indexCache : UniqueMapper.InMethod.Field(getItemDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(getNpcDefinition::class, AbstractIndexCache::class)
    class NpcDefinition_indexCache : UniqueMapper.InMethod.Field(getNpcDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(getKitDefinition::class, AbstractIndexCache::class)
    class KitDefinition_indexCache : UniqueMapper.InMethod.Field(getKitDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @SinceVersion(141)
    @DependsOn(getOverlayDefinition::class, AbstractIndexCache::class)
    class OverlayDefinition_indexCache : UniqueMapper.InMethod.Field(getOverlayDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(getSpotAnimationDefinition::class, AbstractIndexCache::class)
    class SpotAnimationDefinition_indexCache : UniqueMapper.InMethod.Field(getSpotAnimationDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(getSequenceDefinition::class, AbstractIndexCache::class)
    class SequenceDefinition_indexCache : UniqueMapper.InMethod.Field(getSequenceDefinition::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(VarbitDefinition::class, AbstractIndexCache::class)
    class VarbitDefinition_indexCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<VarbitDefinition>() }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(EnumDefinition::class, AbstractIndexCache::class)
    class EnumDefinition_indexCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<EnumDefinition>() }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(UnderlayDefinition::class, AbstractIndexCache::class)
    class UnderlayDefinition_indexCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<UnderlayDefinition>() }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(ObjectDefinition.readNext::class)
    class ObjectDefinition_isLowDetail : UniqueMapper.InMethod.Field(ObjectDefinition.readNext::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class cacheDirectoryLocations : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "/tmp/" }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == Array<String>::class.type }
    }

//    @DependsOn(ClientPreferences.windowMode::class)
//    class isResizable : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<ClientPreferences.windowMode>().id }
//                .nextWithin(15) { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
//    }

    @DependsOn(AttackOption::class, AttackOption_dependsOnCombatLevels::class)
    class playerAttackOption : StaticOrderMapper.Field(0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<AttackOption_dependsOnCombatLevels>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<AttackOption>() }
    }

    @DependsOn(AttackOption::class, AttackOption_dependsOnCombatLevels::class)
    class npcAttackOption : StaticOrderMapper.Field(1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<AttackOption_dependsOnCombatLevels>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<AttackOption>() }
    }

    @DependsOn(Model.method0::class)
    class BoundingBoxes_drawObjectGeometry2D : OrderMapper.InMethod.Field(Model.method0::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Model.draw::class)
    class useBoundingBoxes3D : OrderMapper.InMethod.Field(Model.draw::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

//    @DependsOn(Strings_yourIgnoreListIsFull::class)
//    class ignoreListCount : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_yourIgnoreListIsFull>().id }
//                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//    }

//    @DependsOn(Strings_yourFriendListIsFull::class)
//    class friendsListCount : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_yourFriendListIsFull>().id }
//                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//    }

    @SinceVersion(154)
    @DependsOn(Login_username::class)
    class Login_isUsernameRemembered : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<Login_username>().id }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(garbageCollector::class)
    class garbageCollectorLastCheckTimeMs : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<garbageCollector>().id }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
        override fun resolve(instruction: Instruction2): Field2 {
            return if (instruction.jar.contains(instruction.fieldId)) {
                instruction.jar[instruction.fieldId]
            } else {
                val superType = instruction.jar[instruction.fieldOwner].superType
                instruction.jar[superType to instruction.fieldName]
            }
        }
    }

    @DependsOn(garbageCollector::class)
    class garbageCollectorLastCollectionTime : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<garbageCollector>().id }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
        override fun resolve(instruction: Instruction2): Field2 {
            return if (instruction.jar.contains(instruction.fieldId)) {
                instruction.jar[instruction.fieldId]
            } else {
                val superType = instruction.jar[instruction.fieldOwner].superType
                instruction.jar[superType to instruction.fieldName]
            }
        }
    }

    @DependsOn(Model.draw::class)
    class modelViewportXs : OrderMapper.InMethod.Field(Model.draw::class, -5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Model.draw::class)
    class modelViewportYs : OrderMapper.InMethod.Field(Model.draw::class, -6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_method1 : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer3D>() }
                .and { it.arguments.isEmpty() }
                .and { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod } }
    }

    @MethodParameters("xStart", "yStart", "xEnd", "yEnd")
    @DependsOn(Rasterizer3D_method1::class)
    class Rasterizer3D_setClip : UniqueMapper.InMethod.Method(Rasterizer3D_method1::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(Rasterizer3D_setClip::class)
    class Rasterizer3D_clipWidth : OrderMapper.InMethod.Field(Rasterizer3D_setClip::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer3D_setClip::class)
    class Rasterizer3D_clipHeight : OrderMapper.InMethod.Field(Rasterizer3D_setClip::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    // offsets into R2D.pixels for clip
    @DependsOn(Rasterizer3D_setClip::class)
    class Rasterizer3D_rowOffsets : UniqueMapper.InMethod.Field(Rasterizer3D_setClip::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Rasterizer3D_setClip::class)
    class Rasterizer3D_method3 : OrderMapper.InMethod.Method(Rasterizer3D_setClip::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters("n")
    @DependsOn(Rasterizer3D_setClip::class)
    class nextPowerOfTwo : OrderMapper.InMethod.Method(Rasterizer3D_setClip::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(Rasterizer3D_method3::class)
    class Rasterizer3D_clipMidX : OrderMapper.InMethod.Field(Rasterizer3D_method3::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer3D_method3::class)
    class Rasterizer3D_clipMidY : OrderMapper.InMethod.Field(Rasterizer3D_method3::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer3D_method3::class)
    class Rasterizer3D_clipNegativeMidX : OrderMapper.InMethod.Field(Rasterizer3D_method3::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer3D_method3::class)
    class Rasterizer3D_clipMidX2 : OrderMapper.InMethod.Field(Rasterizer3D_method3::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer3D_method3::class)
    class Rasterizer3D_clipNegativeMidY : OrderMapper.InMethod.Field(Rasterizer3D_method3::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(Rasterizer3D_method3::class)
    class Rasterizer3D_clipMidY2 : OrderMapper.InMethod.Field(Rasterizer3D_method3::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @MethodParameters("x", "y", "width", "height", "color")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_fillRectangle : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.returnType == VOID_TYPE }
                .and { it.arguments.size == 5 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.none { it.opcode == BIPUSH || it.isMethod } }
    }

    @DependsOn(Model.rotateZ::class)
    class Model_sine : OrderMapper.InMethod.Field(Model.rotateZ::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Model.rotateZ::class)
    class Model_cosine : OrderMapper.InMethod.Field(Model.rotateZ::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(EvictingHashTable::class, NpcDefinition.getModel::class)
    class NpcDefinition_cachedModels : UniqueMapper.InMethod.Field(NpcDefinition.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(AbstractIndexCache::class, NpcDefinition.getModel::class)
    class NpcDefinition_modelIndexCache : UniqueMapper.InMethod.Field(NpcDefinition.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @MethodParameters("strings")
    class prependIndices : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Array<String>::class.type }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(Array<String>::class.type) }
    }

    @DependsOn(prependIndices::class)
    class numberMenuOptions : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<prependIndices>().id }
                .prevWithin(5) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(ItemDefinition.getModel::class, EvictingHashTable::class)
    class ItemDefinition_cachedModels : OrderMapper.InMethod.Field(ItemDefinition.getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(AbstractIndexCache::class, ItemDefinition.getModel::class)
    class ItemDefinition_modelIndexCache : UniqueMapper.InMethod.Field(ItemDefinition.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @MethodParameters("x", "y", "width", "height", "rgb", "alpha")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawRectangleAlpha : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == INVOKESTATIC } == 4 }
    }

    @MethodParameters("x", "y", "length", "rgb", "alpha")
    @DependsOn(Rasterizer2D_drawRectangleAlpha::class)
    class Rasterizer2D_drawHorizontalLineAlpha : OrderMapper.InMethod.Method(Rasterizer2D_drawRectangleAlpha::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @MethodParameters("x", "y", "length", "rgb", "alpha")
    @DependsOn(Rasterizer2D_drawRectangleAlpha::class)
    class Rasterizer2D_drawVerticalLineAlpha : OrderMapper.InMethod.Method(Rasterizer2D_drawRectangleAlpha::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(EvictingHashTable::class, SpotAnimationDefinition.getModel::class)
    class SpotAnimationDefinition_cachedModels : UniqueMapper.InMethod.Field(SpotAnimationDefinition.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
    }

    @DependsOn(AbstractIndexCache::class, SpotAnimationDefinition.getModel::class)
    class SpotAnimationDefinition_modelIndexCache : UniqueMapper.InMethod.Field(SpotAnimationDefinition.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
    }

    @MethodParameters("id")
    @DependsOn(ObjectDefinition.transform::class)
    class getVarbit : OrderMapper.InMethod.Method(ObjectDefinition.transform::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    // no 0, yes 1
    @DependsOn(Client.menuAction::class)
    class isItemSelected : UniqueMapper.InMethod.Field(Client.menuAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 38 }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraPitchSine : OrderMapper.InMethod.Field(Scene.method1::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraPitchCosine : OrderMapper.InMethod.Field(Scene.method1::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraYawSine : OrderMapper.InMethod.Field(Scene.method1::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraYawCosine : OrderMapper.InMethod.Field(Scene.method1::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraX : OrderMapper.InMethod.Field(Scene.method1::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraY : OrderMapper.InMethod.Field(Scene.method1::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraZ : OrderMapper.InMethod.Field(Scene.method1::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraXTile : OrderMapper.InMethod.Field(Scene.method1::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraYTile : OrderMapper.InMethod.Field(Scene.method1::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_plane : OrderMapper.InMethod.Field(Scene.method1::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraXTileMin : OrderMapper.InMethod.Field(Scene.method1::class, 11) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraYTileMin : OrderMapper.InMethod.Field(Scene.method1::class, 13) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraXTileMax : OrderMapper.InMethod.Field(Scene.method1::class, 15) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.method1::class)
    class Scene_cameraYTileMax : OrderMapper.InMethod.Field(Scene.method1::class, 17) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("itemContainerId", "index", "itemId", "itemQuantity")
    @DependsOn(itemContainers::class)
    class itemContainerSetItem : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 4..5 }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<itemContainers>().id } }
    }

    @SinceVersion(152)
    @DependsOn(UrlRequester::class)
    class urlRequester : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<UrlRequester>() }
    }

    @MethodParameters("buffer", "hashTable")
    @SinceVersion(141)
    @DependsOn(IterableNodeHashTable::class)
    class readStringIntParameters : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<IterableNodeHashTable>() }
    }

//    @MethodParameters("name")
//    @DependsOn(Ignored.previousName::class)
//    class isOnIgnoreList : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
//                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Ignored.previousName>().id } }
//    }

//    @MethodParameters("c")
//    class isUsernameWhiteSpace : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
//                .and { it.arguments.startsWith(CHAR_TYPE) }
//                .and { it.arguments.size in 1..2 }
//                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 95 } }
//    }

//    @MethodParameters("name", "loginType")
//    @DependsOn(isOnIgnoreList::class)
//    class cleanUsername : OrderMapper.InMethod.Method(isOnIgnoreList::class, 0) {
//        override val predicate = predicateOf<Instruction2> { it.isMethod }
//    }

    @MethodParameters("bytes")
    @DependsOn(Script::class)
    class newScript : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Script>() }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    @DependsOn(Player::class, Actor.targetIndex::class, Actor.orientation::class)
    class Players_targetIndices : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldName == field<Actor.targetIndex>().name && it.fieldOwner == type<Player>() }
                .prevWithin(8) { it.opcode == PUTFIELD && it.fieldName == field<Actor.orientation>().name && it.fieldOwner == type<Player>() }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Player::class, Actor.targetIndex::class, Actor.orientation::class)
    class Players_orientations : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldName == field<Actor.targetIndex>().name && it.fieldOwner == type<Player>() }
                .prevWithin(8) { it.opcode == PUTFIELD && it.fieldName == field<Actor.orientation>().name && it.fieldOwner == type<Player>() }
                .prevWithin(7) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @SinceVersion(157)
    @DependsOn(PacketBufferNode::class)
    class packetBufferNodes : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PacketBufferNode>().withDimensions(1) }
    }

    @DependsOn(AbstractIndexCache.takeRecordEncrypted::class, IndexCache::class)
    class xteaKeys : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<IndexCache>() && it.methodMark == method<AbstractIndexCache.takeRecordEncrypted>().mark }
                .prevWithin(7) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @DependsOn(Varps::class)
    class tempVarps : OrderMapper.InClassInitializer.Field(Varps::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 2000 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    @DependsOn(Varps::class)
    class varps : OrderMapper.InClassInitializer.Field(Varps::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 2000 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    @DependsOn(Varps::class)
    class varpsMasks : OrderMapper.InClassInitializer.Field(Varps::class, 0, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 32 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE.withDimensions(1) }
    }

//    @MethodParameters("bytes")
//    @DependsOn(Sprite::class)
//    class imageToSprite : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>() }
//                .and { it.instructions.any { it.isMethod && it.methodName == "grabPixels" } }
//    }

//    @DependsOn(Script::class)
//    class getScript : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<Script>() }
//                .and { it.arguments.size in 1..2 }
//                .and { it.arguments.startsWith(INT_TYPE) }
//    }

    @SinceVersion(141)
    @DependsOn(ViewportMouse::class)
    class ViewportMouse_entityTags : UniqueMapper.InClassInitializer.Field(ViewportMouse::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1000 }
                .nextIn(2) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    @DependsOn(Npc::class)
    class npcIndices : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<Npc>() }
                .nextWithin(15) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Npc::class)
    class npcCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<Npc>() }
                .nextWithin(18) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Players::class)
    class Players_indices : OrderMapper.InClassInitializer.Field(Players::class, 0, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 2048 }
                .nextIn(2) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Players::class)
    class Players_count : OrderMapper.InClassInitializer.Field(Players::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_0 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    class publicChatMode : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 5001 }
                .skip(10)
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Strings_attack::class)
    class playerMenuActions : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_attack>().id }
                .next { it.isMethod && it.methodName == "equalsIgnoreCase" }
                .prevWithin(8) { it.opcode == GETSTATIC && it.fieldType == String::class.type.withDimensions(1) }
    }

    @DependsOn(playerMenuActions::class)
    class playerMenuOpcodes : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.method.isClassInitializer }
                .and { it.opcode == BIPUSH && it.intOperand == 7 }
                .next { it.opcode == BIPUSH && it.intOperand == 51 }
                .nextIn(2) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(IndexedSprite::class, AbstractIndexCache::class)
    class getIndexedSprites : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<IndexedSprite>().withDimensions(1) }
                .and { it.arguments.startsWith(type<AbstractIndexCache>(), String::class.type) }
    }

    @DependsOn(Sprite::class, AbstractIndexCache::class)
    class getSprites : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>().withDimensions(1) }
                .and { it.arguments.startsWith(type<AbstractIndexCache>(), String::class.type) }
    }

    class mapSceneSprites : IndexedSpritesFieldMapper("mapscene")
    class modIconSprites : IndexedSpritesFieldMapper("mod_icons")
    class scrollbarSprites : IndexedSpritesFieldMapper("scrollbar")
    class runeSprites : IndexedSpritesFieldMapper("runes")
    class titleMuteSprites : IndexedSpritesFieldMapper("title_mute")
    class slFlagSprites : IndexedSpritesFieldMapper("sl_flags")
    class slArrowSprites : IndexedSpritesFieldMapper("sl_arrows")
    class slStarSprites : IndexedSpritesFieldMapper("sl_stars")

    class headIconPrayerSprites : SpritesFieldMapper("headicons_prayer")
    class headIconPkSprites : SpritesFieldMapper("headicons_pk")
    class headIconHintSprites : SpritesFieldMapper("headicons_hint")
    class mapMarkerSprites : SpritesFieldMapper("mapmarker")
    class crossSprites : SpritesFieldMapper("cross")
    class mapDotSprites : SpritesFieldMapper("mapdots")

    @DependsOn(GrandExchangeEvents::class)
    class grandExchangeEvents : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GrandExchangeEvents>() }
    }

    class Instrument_noise : InstrumentStaticIntArrayMapper(0)
    class Instrument_sine : InstrumentStaticIntArrayMapper(1)
    class Instrument_samples : InstrumentStaticIntArrayMapper(2)
    class Instrument_phases : InstrumentStaticIntArrayMapper(3)
    class Instrument_delays : InstrumentStaticIntArrayMapper(4)
    class Instrument_volumeSteps : InstrumentStaticIntArrayMapper(5)
    class Instrument_pitchSteps : InstrumentStaticIntArrayMapper(6)
    class Instrument_pitchBaseSteps : InstrumentStaticIntArrayMapper(7)

    @DependsOn(AbstractFont::class)
    class AbstractFont_random : UniqueMapper.InClassInitializer.Field(AbstractFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == Random::class.type }
    }

    class AbstractFont_strike : AbstractFontStaticIntMapper(0)
    class AbstractFont_underline : AbstractFontStaticIntMapper(1)
    class AbstractFont_previousShadow : AbstractFontStaticIntMapper(2)
    class AbstractFont_shadow : AbstractFontStaticIntMapper(3)
    class AbstractFont_previousColor : AbstractFontStaticIntMapper(4)
    class AbstractFont_color : AbstractFontStaticIntMapper(5)
    class AbstractFont_alpha : AbstractFontStaticIntMapper(6)
    class AbstractFont_justificationTotal : AbstractFontStaticIntMapper(7)
    class AbstractFont_justificationCurrent : AbstractFontStaticIntMapper(8)

    @MethodParameters("s")
    class escapeBrackets : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "<lt>" } }
    }

    @DependsOn(Widget::class)
    class loadWidgetGroup : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == type<Widget>() } }
    }

    @DependsOn(loadWidgetGroup::class)
    class loadedWidgetGroups : OrderMapper.InMethod.Field(loadWidgetGroup::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BooleanArray::class.type }
    }

    @DependsOn(Widget::class, Widget.isHidden::class)
    class isWidgetHidden : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(type<Widget>()) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Widget.isHidden>().id } }
    }

    class rootWidgetGroup : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 2706 }
                .nextWithin(25) { it.isLabel }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

//    @DependsOn(Buffer::class)
//    class decodeStringHuffman : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
//                .and { it.arguments.size in 2..3 }
//                .and { it.arguments.startsWith(type<Buffer>(), INT_TYPE) }
//    }

    @MethodParameters("node", "old")
    @DependsOn(IterableNodeDeque::class, Node::class)
    class IterableNodeDeque_addBefore : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<IterableNodeDeque>() }
                .and { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(type<Node>(), type<Node>()) }
    }

    @DependsOn(GraphicsObject::class, NodeDeque::class)
    class graphicsObjects : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<GraphicsObject>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @DependsOn(Scene::class)
    class Scene_isLowDetail : OrderMapper.InClassInitializer.Field(Scene::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class Skills_enabled : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 24 }
                .next { it.opcode == ICONST_0 || it.opcode == ICONST_1 }
                .next { it.opcode == BASTORE }
                .next { it.opcode == PUTSTATIC && it.fieldType == BooleanArray::class.type }
    }

    @DependsOn(Skills_enabled::class)
    class Skills_experienceTable : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<Skills_enabled>().id }
                .nextIn(3) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    class PlayerType_normal : PlayerTypeInstance(0)
    class PlayerType_playerModerator : PlayerTypeInstance(1)
    class PlayerType_jagexModerator : PlayerTypeInstance(2)
    class PlayerType_ironman : PlayerTypeInstance(3)
    class PlayerType_ultimateIronman : PlayerTypeInstance(4)
    class PlayerType_hardcoreIronman : PlayerTypeInstance(5)

    @DependsOn(ModelData::class)
    class ModelData_sine : OrderMapper.InClassInitializer.Field(ModelData::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(ModelData::class)
    class ModelData_cosine : OrderMapper.InClassInitializer.Field(ModelData::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(AbstractSoundSystem::class)
    class newSoundSystem : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractSoundSystem>() }
    }

    @DependsOn(AbstractSoundSystem::class)
    class soundSystem0 : StaticOrderMapper.Field(0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AbstractSoundSystem>() }
    }

    @DependsOn(AbstractSoundSystem::class)
    class soundSystem1 : StaticOrderMapper.Field(1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AbstractSoundSystem>() }
    }

    class WorldMapCacheName_details : WorldMapCacheNameInstance(0)
    class WorldMapCacheName_compositeMap : WorldMapCacheNameInstance(1)
    class WorldMapCacheName_compositeTexture : WorldMapCacheNameInstance(2)
    class WorldMapCacheName_area : WorldMapCacheNameInstance(3)
    class WorldMapCacheName_labels : WorldMapCacheNameInstance(4)

//    @DependsOn(FriendLoginUpdate::class, LinkDeque::class)
//    class friendLoginUpdates : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<FriendLoginUpdate>() && it.methodName == Method2.CONSTRUCTOR_NAME }
//                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<LinkDeque>() }
//    }

    @DependsOn(Rasterizer3D::class, TextureLoader::class)
    class Rasterizer3D_textureLoader : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<Rasterizer3D>() }
                .and { it.type == type<TextureLoader>() }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_isLowDetailTexture : OrderMapper.InClassInitializer.Field(Rasterizer3D::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(AreaDefinition::class)
    class AreaDefinition_areas : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<AreaDefinition>().withDimensions(1) }
    }

    @DependsOn(AreaDefinition::class, EvictingHashTable::class)
    class AreaDefinition_cachedSprites : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<AreaDefinition>() }
                .and { it.type == type<EvictingHashTable>() }
    }

    class ByteArrayPool_small : ByteArrayPoolArray(0)
    class ByteArrayPool_medium : ByteArrayPoolArray(1)
    class ByteArrayPool_large : ByteArrayPoolArray(2)

    class ByteArrayPool_smallCount : ByteArrayPoolCount(0)
    class ByteArrayPool_mediumCount : ByteArrayPoolCount(1)
    class ByteArrayPool_largeCount : ByteArrayPoolCount(2)

    @DependsOn(ByteArrayPool_large::class)
    class ByteArrayPool_get : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { Modifier.isSynchronized(it.access) }
                .and { it.returnType == ByteArray::class.type }
                .and { it.instructions.any { it.isField && it.fieldId == field<ByteArrayPool_large>().id } }
    }

//    @DependsOn(ByteArrayPool_large::class)
//    class ByteArrayPool_release : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { Modifier.isSynchronized(it.access) }
//                .and { it.returnType == VOID_TYPE }
//                .and { it.instructions.any { it.isField && it.fieldId == field<ByteArrayPool_large>().id } }
//    }

    @DependsOn(Strings_connectingToUpdateServer::class)
    class Login_loadingText : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_connectingToUpdateServer>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings_connectingToUpdateServer::class)
    class Login_loadingPercent : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_connectingToUpdateServer>().id }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Strings_connectingToUpdateServer::class)
    class titleLoadingStage : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_connectingToUpdateServer>().id }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // inlined
//    @DependsOn(PacketBuffer::class, Npc::class, Actor.overheadTextCyclesRemaining::class)
//    class updateNpcs : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.size in 1..2 }
//                .and { it.arguments.startsWith(type<PacketBuffer>()) }
//                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldOwner == type<Npc>() && it.fieldType == INT_TYPE &&
//                        it.fieldName == field<Actor.overheadTextCyclesRemaining>().name } }
//    }

    @DependsOn(Player::class, PacketBuffer::class)
    class updatePlayer : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<PacketBuffer>()) }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == type<Player>() } }
    }

    @DependsOn(Buffer::class)
    class loadTerrain : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 7..8 }
                .and { it.arguments.startsWith(type<Buffer>(), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    // inlined
//    class chatCommand : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.size in 1..2 }
//                .and { it.arguments.startsWith(String::class.type) }
//                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "displayfps" } }
//    }

    class isResizable : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_2 }
                .next { it.node is JumpInsnNode }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    @DependsOn(cycle::class)
    class doCycle : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == PUTSTATIC && it.fieldId == field<cycle>().id } }
    }

    @DependsOn(PacketBuffer::class)
    class PacketBuffer_masks : UniqueMapper.InClassInitializer.Field(PacketBuffer::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @SinceVersion(160)
    @DependsOn(PacketWriter::class)
    class packetWriter : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PacketWriter>() }
    }

    @DependsOn(KeyHandler::class)
    class KeyHandler_idleCycles : OrderMapper.InClassInitializer.Field(KeyHandler::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler::class)
    class MouseHandler_idleCycles : OrderMapper.InClassInitializer.Field(MouseHandler::class, 0 ) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .prevWithin(7) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextXOffsets : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .prevWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextXs : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .nextWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .nextWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextYs : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .nextWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .nextWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .nextWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadText : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .nextWithin(70) { it.opcode == GETSTATIC && it.fieldType == String::class.type.withDimensions(1) }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextCyclesRemaining : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .nextWithin(70) { it.opcode == GETSTATIC && it.fieldType == String::class.type.withDimensions(1) }
                .prevWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

//    class clanChatCount : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3614 }
//                .nextWithin(17) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
//                .nextWithin(7) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//    }

//    class clanChatName : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3614 }
//                .nextWithin(17) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
//    }

    // inlined
//    @MethodParameters("l")
//    class longToString : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
//                .and { it.arguments.size in 1..2 }
//                .and { it.arguments.startsWith(LONG_TYPE) }
//                .and { it.instructions.none { it.isMethod && it.methodName == "toUpperCase" } }
//                .and { it.instructions.any { it.opcode == LREM } }
//    }

    // inlined
//    @MethodParameters("l")
//    class longToTitleString : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
//                .and { it.arguments.size in 1..2 }
//                .and { it.arguments.startsWith(LONG_TYPE) }
//                .and { it.instructions.any { it.isMethod && it.methodName == "toUpperCase" } }
//                .and { it.instructions.any { it.opcode == LREM } }
//    }

    class base37Table : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 37 }
                .next { it.opcode == NEWARRAY && it.intOperand == 5 }
                .nextWithin(200) { it.opcode == PUTSTATIC && it.fieldType == CharArray::class.type }
    }

    @DependsOn(AbstractFont::class)
    class AbstractFont_lines : UniqueMapper.InClassInitializer.Field(AbstractFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == String::class.type.withDimensions(1) }
    }

    @DependsOn(AbstractFont::class, IndexedSprite::class)
    class AbstractFont_modIconSprites : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<AbstractFont>() }
                .and { it.type == type<IndexedSprite>().withDimensions(1) }
    }

    @DependsOn(Font.drawGlyph::class)
    class AbstractFont_placeGlyph : UniqueMapper.InMethod.Method(Font.drawGlyph::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[Triple(instruction.jar[instruction.methodOwner].superType, instruction.methodName, instruction.methodType)]
        }
    }

    @DependsOn(Font.drawGlyphAlpha::class)
    class AbstractFont_placeGlyphAlpha : UniqueMapper.InMethod.Method(Font.drawGlyphAlpha::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
        override fun resolve(instruction: Instruction2): Method2 {
            return instruction.jar[Triple(instruction.jar[instruction.methodOwner].superType, instruction.methodName, instruction.methodType)]
        }
    }

    @MethodParameters("pixels", "x", "y", "width", "height", "color")
    @DependsOn(AbstractFont_placeGlyph::class)
    class AbstractFont_drawGlyph : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == method<AbstractFont_placeGlyph>().id } }
    }

    @MethodParameters("pixels", "x", "y", "width", "height", "color", "alpha")
    @DependsOn(AbstractFont_placeGlyphAlpha::class)
    class AbstractFont_drawGlyphAlpha : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == method<AbstractFont_placeGlyphAlpha>().id } }
    }

    // 0 - 27
    @DependsOn(Widget.swapItems::class)
    class inventorySlotHovered : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Widget.swapItems>().id }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Widget.swapItems::class)
    class inventorySlotPressed : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Widget.swapItems>().id }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(GameShell.focusGained::class)
    class hasFocus : UniqueMapper.InMethod.Field(GameShell.focusGained::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }
}