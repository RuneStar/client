package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.objectweb.asm.tree.JumpInsnNode
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.std.*
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import java.applet.Applet
import java.io.File
import java.lang.management.GarbageCollectorMXBean
import java.lang.reflect.Modifier
import java.net.URL
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.zip.CRC32
import kotlin.collections.ArrayList

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
    @DependsOn(NPCType::class)
    class getNPCType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<NPCType>() }
    }

//    //    @DependsOn(OverlayDefinition::class)
//    class getOverlayDefinition : StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<OverlayDefinition>() }
//    }

//    @DependsOn(UnderlayDefinition::class)
//    class getUnderlayDefinition : StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<UnderlayDefinition>() }
//    }

//    @MethodParameters("id")
//    @DependsOn(HealthBarDefinition::class)
//    class getHealthBarDefinition : StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<HealthBarDefinition>() }
//    }

    @DependsOn(IDKType::class)
    class getIDKType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<IDKType>() }
    }

//    //    @DependsOn(EnumType::class)
//    class getEnumType : StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<EnumType>() }
//    }

    @DependsOn(SpotType::class)
    class getSpotType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<SpotType>() }
    }

    @DependsOn(SeqType::class)
    class getSeqType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<SeqType>() }
    }

    @MethodParameters("id")
    @DependsOn(ObjType::class)
    class getObjType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ObjType>() }
    }

    @MethodParameters("id")
    @DependsOn(LocType::class)
    class getLocType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<LocType>() }
    }

    @MethodParameters("argument1", "argument2", "opcode", "argument0", "action", "targetName", "mouseX", "mouseY")
    class doAction : StaticMethod() {
        override val predicate = predicateOf<Method2> {
            it.arguments.startsWith(
                    INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                    String::class.type, String::class.type, INT_TYPE, INT_TYPE)
        }
    }

    @MethodParameters("targetVerb", "opbase", "opcode", "arg0", "arg1", "arg2", "b")
    class addMiniMenuEntry : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(String::class.type, String::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC } }
    }

    @DependsOn(World::class)
    class worlds : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<World>().withDimensions(1) }
    }

    @DependsOn(worlds::class)
    @MethodParameters()
    class loadWorlds : StaticMethod() {
        override val predicate = predicateOf<Method2> {
            it.instructions.any {
                it.opcode == PUTSTATIC && it.fieldId == field<worlds>().id
            }
        }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuActions : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == Array<String>::class.type }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuTargetNames : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == Array<String>::class.type }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuOpcodes : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuArguments0 : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuArguments1 : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuArguments2 : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuShiftClick : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 0) {
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
    class Tiles_renderFlags : OrderMapper.InMethod.Field(getTileHeight::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BYTE_TYPE.withDimensions(3) }
    }

    @DependsOn(getTileHeight::class)
    class Tiles_heights : OrderMapper.InMethod.Field(getTileHeight::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE.withDimensions(3) }
    }

    @DependsOn(addMiniMenuEntry::class)
    class isMiniMenuOpen : OrderMapper.InMethod.Field(addMiniMenuEntry::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(addMiniMenuEntry::class)
    class menuOptionsCount : OrderMapper.InMethod.Field(addMiniMenuEntry::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doAction::class)
    class mouseCrossX : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(7) {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doAction::class)
    class mouseCrossY : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(7) {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(7) {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // 0 none, 1 yellow, 2 red
    @DependsOn(doAction::class)
    class mouseCrossColor : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(20) { it.opcode == ICONST_0 }
                .prev {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    // 0 - 400
    @DependsOn(doAction::class)
    class mouseCrossState : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1004 }
                .nextWithin(20) { it.opcode == ICONST_0 }
                .next {it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    class visibleTiles : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE.withDimensions(2) }
    }

    @DependsOn(PacketBit::class)
    class updateExternalPlayer : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(type<PacketBit>(), INT_TYPE) }
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
    @DependsOn(isMiniMenuOpen::class)
    class openMiniMenu : InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.opcode == PUTSTATIC && it.fieldId == field<isMiniMenuOpen>().id } }
    }

    @MethodParameters("x", "y")
    @DependsOn(menuOptionsCount::class)
    class openMenu0 : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.count { it.opcode == GETSTATIC && it.fieldId == field<menuOptionsCount>().id } == 3 }
    }

    @DependsOn(openMenu0::class)
    class menuX : OrderMapper.InMethod.Field(openMenu0::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(openMenu0::class)
    class menuY : OrderMapper.InMethod.Field(openMenu0::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(openMenu0::class)
    class menuWidth : OrderMapper.InMethod.Field(openMenu0::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(openMenu0::class)
    class menuHeight : OrderMapper.InMethod.Field(openMenu0::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("type", "sender", "text", "prefix")
    class addMessage : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type, String::class.type, String::class.type) }
    }

    @DependsOn(ClanChat::class)
    class clanChat : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<ClanChat>() }
    }

    @DependsOn(FriendSystem::class)
    class friendSystem : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<FriendSystem>() }
    }

    @DependsOn(MouseHandler_currentButton0::class)
    class MouseHandler_x : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton0>().id }
                .nextIn(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_currentButton0::class)
    class MouseHandler_y : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton0>().id }
                .nextIn(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_currentButton0::class)
    class MouseHandler_millis : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton0>().id }
                .nextWithin(7) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(MouseHandler_currentButton0::class)
    class MouseHandler_millis0 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton0>().id }
                .nextWithin(7) { it.opcode == GETSTATIC && it.fieldType == LONG_TYPE }
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

    @DependsOn(Component.width::class)
    class rootComponentWidths : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.width>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Component.height::class)
    class rootComponentHeights : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.height>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Component.x::class)
    class rootComponentXs : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.x>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Component.x::class)
    class rootComponentCount : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.x>().id }
                .prevWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .nextWithin(1) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Component.y::class)
    class rootComponentYs : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.y>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_sine : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.method.isClassInitializer }
                .and { it.klass == klass<Rasterizer3D>() }
                .and { it.opcode == INVOKESTATIC && it.methodId == Math::sin.id }
                .prevWithin(7) { it.opcode == LDC && it.ldcCst == 65536.0 }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_cosine : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.method.isClassInitializer }
                .and { it.klass == klass<Rasterizer3D>() }
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
    class viewportTempX : OrderMapper.InMethod.Field(worldToScreen::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_M1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(worldToScreen::class)
    class viewportTempY : OrderMapper.InMethod.Field(worldToScreen::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_M1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
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
    class objStacks : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeDeque>().withDimensions(3) }
    }

    @DependsOn(Component::class)
    class interfaceComponents : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Component>().withDimensions(2) }
    }

    @DependsOn(MouseHandler::class)
    class MouseHandler_instance : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseHandler>() }
    }

    @DependsOn(MouseWheel::class)
    class mouseWheel : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseWheel>() }
    }

    @DependsOn(KeyHandler::class)
    class KeyHandler_instance : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<KeyHandler>() }
    }

    @DependsOn(InterfaceParent::class, NodeHashTable::class)
    class interfaceParents : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<InterfaceParent>() }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(Inventory::class, NodeHashTable::class)
    class itemContainers : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<Inventory>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    class garbageCollector : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == GarbageCollectorMXBean::class.type }
    }

    @DependsOn(ReflectionCheck::class, IterableNodeDeque::class)
    class reflectionChecks : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ReflectionCheck>() }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == type<IterableNodeDeque>() }
    }

    @DependsOn(NPCType::class, EvictingDualNodeHashTable::class)
    class NPCType_cached : CachedDefinitionMapper(NPCType::class)

    @DependsOn(EnumType::class, EvictingDualNodeHashTable::class)
    class EnumType_cached : CachedDefinitionMapper(EnumType::class)

    @DependsOn(Sprite::class, EvictingDualNodeHashTable::class)
    class Sprite_cached : CachedDefinitionMapper(Sprite::class)

    @DependsOn(IDKType::class, EvictingDualNodeHashTable::class)
    class IDKType_cached : CachedDefinitionMapper(IDKType::class)

//    //    @DependsOn(OverlayDefinition::class, EvictingDualNodeHashTable::class)
//    class OverlayDefinition_cached : CachedDefinitionMapper(OverlayDefinition::class)

    @DependsOn(FloorUnderlayType::class, EvictingDualNodeHashTable::class)
    class FloorUnderlayType_cached : CachedDefinitionMapper(FloorUnderlayType::class)

    @DependsOn(SpotType::class, EvictingDualNodeHashTable::class)
    class SpotType_cached : CachedDefinitionMapper(SpotType::class)

    @DependsOn(ObjType::class, EvictingDualNodeHashTable::class)
    class ObjType_cached : CachedDefinitionMapper(ObjType::class)

    @DependsOn(VarBitType::class, EvictingDualNodeHashTable::class)
    class VarBitType_cached : CachedDefinitionMapper(VarBitType::class)

    @DependsOn(SeqType::class, EvictingDualNodeHashTable::class)
    class SeqType_cached : CachedDefinitionMapper(SeqType::class)

    @DependsOn(LocType::class, EvictingDualNodeHashTable::class)
    class LocType_cached : CachedDefinitionMapper(LocType::class)

    @DependsOn(HitmarkType::class, EvictingDualNodeHashTable::class)
    class HitmarkType_cached : CachedDefinitionMapper(HitmarkType::class)

    @DependsOn(GzipDecompressor::class, EvictingDualNodeHashTable::class)
    class gzipDecompressor : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GzipDecompressor>() }
    }

    @DependsOn(PlatformInfoProvider::class)
    class platformInfoProvider : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PlatformInfoProvider>() }
    }

//    class clanChatOwner : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3625 }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
//    }

    class fps : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Fps:" }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class worldToMinimap : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 90_000 } }
    }

    class camAngleX : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 5505 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class camAngleY : StaticUniqueMapper.Field() {
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

    class staffModLevel : StaticUniqueMapper.Field() {
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
                .nextWithin(16) { it.isLabel }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
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

    @DependsOn(AbstractRasterProvider.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == method<AbstractRasterProvider.draw>().id } }
    }

    @DependsOn(draw::class, gameState::class)
    class gameDrawingMode : UniqueMapper.InMethod.Field(draw::class) {
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

    @MethodParameters("x", "y", "color")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_setPixel : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("x0", "y0", "x1", "y1", "color")
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

    class localPlayerName : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "&u=" }
                .nextWithin(2) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Clock::class)
    class clock : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Clock>() }
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

    class Strings_membersObject : StringsUniqueMapper("Members object")
    class Strings_hidden : StringsUniqueMapper("Hidden")
    class Strings_space : StringsUniqueMapper(" ")
    class Strings_loading : StringsUniqueMapper("Loading...")
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
    class Strings_loadingWorldMap : StringsUniqueMapper("Loading world map - ")
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
    class Strings_null : StringsUniqueMapper("null")

    @DependsOn(Component::class, Component.children::class)
    class getComponentChild : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Component>() }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Component.children>().id } }
    }

    @DependsOn(MiniMenuEntry::class)
    class tempMenuAction : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MiniMenuEntry>() }
    }

    @DependsOn(IterableNodeHashTable::class)
    class Messages_hashTable : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>() }
    }

    @DependsOn(Client::class)
    class client : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Client>() }
    }

    @DependsOn(Component.getFont::class, EvictingDualNodeHashTable::class)
    class Component_cachedFonts : UniqueMapper.InMethod.Field(Component.getFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(Component.getModel::class, EvictingDualNodeHashTable::class)
    class Component_cachedModels : UniqueMapper.InMethod.Field(Component.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(StudioGame::class)
    class studioGame : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<StudioGame>() && it.klass != klass<StudioGame>() }
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

    @MethodParameters("component")
    @DependsOn(Component::class)
    class alignComponent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<Component>()) }
    }

    @MethodParameters("id")
    @DependsOn(alignComponent::class)
    class getInterfaceComponent : UniqueMapper.InMethod.Method(alignComponent::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.jar[it.methodId].arguments.size in 1..2 }
    }

    class clDat : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst is String && (it.ldcCst as String).contains("ex_cl_") }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == File::class.type }
    }

    @DependsOn(SoundEffect::class)
    class soundEffects : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<SoundEffect>().withDimensions(1) }
    }

    @DependsOn(TaskHandler::class)
    class javaVendor : OrderMapper.InConstructor.Field(TaskHandler::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(TaskHandler::class)
    class javaVersion : OrderMapper.InConstructor.Field(TaskHandler::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(PcmPlayerProvider::class)
    class pcmPlayerProvider : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PcmPlayerProvider>() }
    }

    @DependsOn(SoundSystem::class)
    class soundSystem : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<SoundSystem>() }
    }

    class isLowDetail : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Mem:" }
                .prevWithin(30) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    // else mono
    @DependsOn(DevicePcmPlayer.position::class)
    class isStereo : UniqueMapper.InMethod.Field(DevicePcmPlayer.position::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
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


    @DependsOn(ArchiveDisk::class)
    class masterDisk : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<ArchiveDisk>() }
    }

//    @DependsOn(ClientPreferences::class)
//    class readClientPreferences : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<ClientPreferences>() }
//    }

    @DependsOn(MouseWheel.useRotation::class)
    class mouseWheelRotation : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<MouseWheel.useRotation>().id }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mouseMoved::class)
    class MouseHandler_x0 : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mouseMoved::class)
    class MouseHandler_y0 : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedX0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedY0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastPressedTimeMillis0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_lastButton0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class MouseHandler_currentButton0 : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
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

    @DependsOn(FontName::class)
    class FontName_plain11 : OrderMapper.InClassInitializer.Field(FontName::class, 0, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName::class)
    class FontName_plain12 : OrderMapper.InClassInitializer.Field(FontName::class, 1, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName::class)
    class FontName_bold12 : OrderMapper.InClassInitializer.Field(FontName::class, 2, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName::class)
    class FontName_verdana11 : OrderMapper.InClassInitializer.Field(FontName::class, 3, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName::class)
    class FontName_verdana13 : OrderMapper.InClassInitializer.Field(FontName::class, 4, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName::class)
    class FontName_verdana15 : OrderMapper.InClassInitializer.Field(FontName::class, 5, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

//    //    @DependsOn(FontName::class)
//    class FontName_values : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<FontName>().withDimensions(1) }
//    }

    @DependsOn(Fonts::class)
    class fonts : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Fonts>() }
    }

    class fontsMap : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == HashMap::class.type }
    }

    @DependsOn(Archive::class)
    class newArchive : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Archive>() }
    }

    class archive0 : ArchiveFieldMapper(0)
    class archive1 : ArchiveFieldMapper(1)
    class archive2 : ArchiveFieldMapper(2)
    class archive3 : ArchiveFieldMapper(3)
    class archive4 : ArchiveFieldMapper(4)
    class archive5 : ArchiveFieldMapper(5)
    class archive6 : ArchiveFieldMapper(6)
    class archive7 : ArchiveFieldMapper(7)
    class archive8 : ArchiveFieldMapper(8)
    class archive9 : ArchiveFieldMapper(9)
    class archive10 : ArchiveFieldMapper(10)
    class archive11 : ArchiveFieldMapper(11)
    class archive12 : ArchiveFieldMapper(12)
    class archive13 : ArchiveFieldMapper(13)
    class archive14 : ArchiveFieldMapper(14)
    class archive15 : ArchiveFieldMapper(15)
//    class archive16 : ArchiveFieldMapper(16)

    @DependsOn(MouseRecorder::class)
    class mouseRecorder : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseRecorder>() }
    }

    @DependsOn(Font::class, FontName_plain11::class)
    class fontPlain11 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_plain11>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @DependsOn(Font::class, FontName_plain12::class)
    class fontPlain12 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_plain12>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @DependsOn(Font::class, FontName_bold12::class)
    class fontBold12 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_bold12>().id }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == type<Font>() }
    }

    @DependsOn(FontName_verdana11::class, FontName::class)
    class fontNameVerdana11 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_verdana11>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName_verdana13::class, FontName::class)
    class fontNameVerdana13 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_verdana13>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(FontName_verdana15::class, FontName::class)
    class fontNameVerdana15 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<FontName_verdana15>().id }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<FontName>() }
    }

    @DependsOn(ArchiveDisk::class)
    class ArchiveDisk_buffer : UniqueMapper.InClassInitializer.Field(ArchiveDisk::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 520 }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == ByteArray::class.type }
    }

    @DependsOn(ArchiveDiskActionHandler::class)
    class ArchiveDiskActionHandler_lock : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type }
                .and { it.klass == klass<ArchiveDiskActionHandler>() }
    }

    @DependsOn(ArchiveDiskActionHandler::class, NodeDeque::class)
    class ArchiveDiskActionHandler_requestQueue : OrderMapper.InClassInitializer.Field(ArchiveDiskActionHandler::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @DependsOn(ArchiveDiskActionHandler::class, NodeDeque::class)
    class ArchiveDiskActionHandler_responseQueue : OrderMapper.InClassInitializer.Field(ArchiveDiskActionHandler::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @DependsOn(ArchiveDiskActionHandler::class)
    class ArchiveDiskActionHandler_thread : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<ArchiveDiskActionHandler>() }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == Thread::class.type }
    }

    @DependsOn(Archive.load::class)
    class Archive_crc : UniqueMapper.InMethod.Field(Archive.load::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == CRC32::class.type }
    }

//    @MethodParameters("bytes", "copyArray")
//    class byteArrayToObject : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == Any::class.type }
//                .and { it.arguments.startsWith(ByteArray::class.type) }
//    }

    @MethodParameters("o", "copyArray")
    class byteArrayFromObject : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(Any::class.type) }
    }

//    @DependsOn(byteArrayToObject::class)
//    class directBufferUnavailable : UniqueMapper.InMethod.Field(byteArrayToObject::class) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
//    }

    @DependsOn(Archive::class)
    class NetCache_archives : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Archive>().withDimensions(1) }
    }

    @DependsOn(NetCache::class)
    class NetCache_crc : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<NetCache>() }
                .and { it.type == CRC32::class.type }
    }

    @DependsOn(Archive::class)
    class requestNetFile : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Archive>(), INT_TYPE, INT_TYPE, INT_TYPE, BYTE_TYPE, BOOLEAN_TYPE) }
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

    @DependsOn(AbstractSocket::class, NetCache_pendingPriorityWritesCount::class)
    class NetCache_socket : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<NetCache_pendingPriorityWritesCount>().id }
                .prevWithin(3) { it.opcode == ISUB }
                .prevWithin(20) { it.opcode == GETSTATIC && it.fieldType == type<AbstractSocket>() }
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
    @DependsOn(AbstractArchive.takeFileByNames::class)
    class hashString : UniqueMapper.InMethod.Method(AbstractArchive.takeFileByNames::class) {
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

    @DependsOn(Packet::class, NetCache::class)
    class NetCache_responseHeaderBuffer : UniqueMapper.InClassInitializer.Field(NetCache::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 8 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == type<Packet>() }
    }

    @DependsOn(Packet::class, NetCache_crc::class)
    class NetCache_responseArchiveBuffer : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<NetCache_crc>().id }
                .nextWithin(3) { it.isMethod && it.methodId == CRC32::reset.id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == type<Packet>() }
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
    @DependsOn(Packet.gjstr::class)
    class decodeStringCp1252 : UniqueMapper.InMethod.Method(Packet.gjstr::class) {
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

    @DependsOn(Archive.loadIndex::class, Packet::class)
    class NetCache_reference : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Archive.loadIndex>().id }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == type<Packet>() }
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

    class ViewportMouse_x : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 50 }
                .nextIn(2) { it.opcode == SIPUSH && it.intOperand == 3500 }
                .nextWithin(5) { it.opcode == ISTORE }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(ViewportMouse_x::class)
    class ViewportMouse_y : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 50 }
                .nextIn(2) { it.opcode == SIPUSH && it.intOperand == 3500 }
                .nextWithin(20) { it.opcode == GETSTATIC && it.fieldId == field<ViewportMouse_x>().id }
                .nextWithin(10) { it.opcode == ISTORE }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(ViewportMouse_y::class)
    class ViewportMouse_isInViewport : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<ViewportMouse_y>().id }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(ViewportMouse_y::class)
    class ViewportMouse_entityCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<ViewportMouse_y>().id }
                .next { it.opcode == ICONST_1 }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
                .next { it.opcode == ICONST_0 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

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
    class selectedItemName : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_use>().id }
                .nextIn(4) { it.opcode == GETSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Component.spellName::class)
    class selectedSpellName : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.spellName>().id }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    // "Cast"
    @DependsOn(doAction::class, Component.opbase::class)
    class selectedSpellActionName : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.opbase>().id }
                .prevWithin(15) { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @MethodParameters("descriptor")
    class loadClassFromDescriptor : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == java.lang.Class::class.type }
    }

    @DependsOn(SoundEffect::class)
    class readSoundEffect : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<SoundEffect>() }
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

    @DependsOn(getLocType::class, AbstractArchive::class)
    class LocType_archive : UniqueMapper.InMethod.Field(getLocType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(getObjType::class, AbstractArchive::class)
    class ObjType_archive : UniqueMapper.InMethod.Field(getObjType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(getNPCType::class, AbstractArchive::class)
    class NPCType_archive : UniqueMapper.InMethod.Field(getNPCType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(getIDKType::class, AbstractArchive::class)
    class IDKType_archive : UniqueMapper.InMethod.Field(getIDKType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

//    //    @DependsOn(getOverlayDefinition::class, AbstractIndexCache::class)
//    class OverlayDefinition_archive : UniqueMapper.InMethod.Field(getOverlayDefinition::class) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractIndexCache>() }
//    }

    @DependsOn(getSpotType::class, AbstractArchive::class)
    class SpotType_archive : UniqueMapper.InMethod.Field(getSpotType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(getSeqType::class, AbstractArchive::class)
    class SeqType_archive : UniqueMapper.InMethod.Field(getSeqType::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(VarBitType::class, AbstractArchive::class)
    class VarBitType_archive : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<VarBitType>() }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(EnumType::class, AbstractArchive::class)
    class EnumType_archive : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<EnumType>() }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(FloorUnderlayType::class, AbstractArchive::class)
    class FloorUnderlayType_archive : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESPECIAL && it.methodOwner == type<FloorUnderlayType>() }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(LocType.decode0::class)
    class LocType_isLowDetail : UniqueMapper.InMethod.Field(LocType.decode0::class) {
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
    }

    @DependsOn(garbageCollector::class)
    class garbageCollectorLastCollectionTime : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<garbageCollector>().id }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
                .nextWithin(4) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE }
    }

    @DependsOn(Model.draw::class)
    class modelViewportXs : OrderMapper.InMethod.Field(Model.draw::class, -6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Model.draw::class)
    class modelViewportYs : OrderMapper.InMethod.Field(Model.draw::class, -5) {
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

//    @MethodParameters("n")
//    @DependsOn(Rasterizer3D_setClip::class)
//    class nextPowerOfTwo : OrderMapper.InMethod.Method(Rasterizer3D_setClip::class, 1) {
//        override val predicate = predicateOf<Instruction2> { it.isMethod }
//    }

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

    @DependsOn(EvictingDualNodeHashTable::class, NPCType.getModel::class)
    class NPCType_cachedModels : UniqueMapper.InMethod.Field(NPCType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(AbstractArchive::class, NPCType.getModel::class)
    class NPCType_modelArchive : UniqueMapper.InMethod.Field(NPCType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

//    @MethodParameters("strings")
//    class prependIndices : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == Array<String>::class.type }
//                .and { it.arguments.size in 1..2 }
//                .and { it.arguments.startsWith(Array<String>::class.type) }
//    }

//    @DependsOn(prependIndices::class)
//    class numberMenuOptions : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<prependIndices>().id }
//                .prevWithin(5) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
//    }

    @DependsOn(ObjType.getModel::class, EvictingDualNodeHashTable::class)
    class ObjType_cachedModels : OrderMapper.InMethod.Field(ObjType.getModel::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(AbstractArchive::class, ObjType.getModel::class)
    class ObjType_modelArchive : UniqueMapper.InMethod.Field(ObjType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
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

    @DependsOn(EvictingDualNodeHashTable::class, SpotType.getModel::class)
    class SpotType_cachedModels : UniqueMapper.InMethod.Field(SpotType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(AbstractArchive::class, SpotType.getModel::class)
    class SpotType_modelArchive : UniqueMapper.InMethod.Field(SpotType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @MethodParameters("id")
    @DependsOn(LocType.transform::class)
    class getVarbit : OrderMapper.InMethod.Method(LocType.transform::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    // no 0, yes 1
    @DependsOn(Client.doAction::class)
    class isItemSelected : UniqueMapper.InMethod.Field(Client.doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 38 }
                .nextWithin(2) { it.node is JumpInsnNode }
                .nextWithin(10) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraPitchSine : OrderMapper.InMethod.Field(Scene.draw::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraPitchCosine : OrderMapper.InMethod.Field(Scene.draw::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraYawSine : OrderMapper.InMethod.Field(Scene.draw::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraYawCosine : OrderMapper.InMethod.Field(Scene.draw::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraX : OrderMapper.InMethod.Field(Scene.draw::class, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraY : OrderMapper.InMethod.Field(Scene.draw::class, 6) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraZ : OrderMapper.InMethod.Field(Scene.draw::class, 7) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraXTile : OrderMapper.InMethod.Field(Scene.draw::class, 8) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraYTile : OrderMapper.InMethod.Field(Scene.draw::class, 9) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_plane : OrderMapper.InMethod.Field(Scene.draw::class, 10) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraXTileMin : OrderMapper.InMethod.Field(Scene.draw::class, 11) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraYTileMin : OrderMapper.InMethod.Field(Scene.draw::class, 13) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraXTileMax : OrderMapper.InMethod.Field(Scene.draw::class, 15) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.draw::class)
    class Scene_cameraYTileMax : OrderMapper.InMethod.Field(Scene.draw::class, 17) {
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

    @DependsOn(UrlRequester::class)
    class urlRequester : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<UrlRequester>() }
    }

    @MethodParameters("packet", "hashTable")
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
    @DependsOn(ClientScript::class)
    class loadClientScript : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ClientScript>() }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    @DependsOn(Actor::class, Actor.targetIndex::class, Actor.orientation::class)
    class Players_targetIndices : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldName == field<Actor.targetIndex>().name && it.fieldOwner == type<Actor>() }
                .prevWithin(8) { it.opcode == PUTFIELD && it.fieldName == field<Actor.orientation>().name && it.fieldOwner == type<Actor>() }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Actor::class, Actor.targetIndex::class, Actor.orientation::class)
    class Players_orientations : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldName == field<Actor.targetIndex>().name && it.fieldOwner == type<Actor>() }
                .prevWithin(8) { it.opcode == PUTFIELD && it.fieldName == field<Actor.orientation>().name && it.fieldOwner == type<Actor>() }
                .prevWithin(7) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(PacketBitNode::class)
    class packetBitNodes : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PacketBitNode>().withDimensions(1) }
    }

    @DependsOn(AbstractArchive.takeFileEncrypted::class, Archive::class)
    class xteaKeys : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodOwner == type<Archive>() && it.methodMark == method<AbstractArchive.takeFileEncrypted>().mark }
                .prevWithin(7) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @DependsOn(Varps::class)
    class Varps_temp : OrderMapper.InClassInitializer.Field(Varps::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand >= 2000 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    @DependsOn(Varps::class)
    class Varps_main : OrderMapper.InClassInitializer.Field(Varps::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand >= 2000 }
                .nextWithin(3) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE.withDimensions(1) }
    }

    @DependsOn(Varps::class)
    class Varps_masks : OrderMapper.InClassInitializer.Field(Varps::class, 0, 1) {
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

    @DependsOn(ViewportMouse::class)
    class ViewportMouse_entityTags : UniqueMapper.InClassInitializer.Field(ViewportMouse::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1000 }
                .nextIn(2) { it.opcode == PUTSTATIC && it.fieldType == LONG_TYPE.withDimensions(1) }
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
                .skip(8)
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

//    @DependsOn(IndexedSprite::class, AbstractIndexCache::class)
//    class getIndexedSprites : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<IndexedSprite>().withDimensions(1) }
//                .and { it.arguments.startsWith(type<AbstractIndexCache>(), String::class.type) }
//    }

    @DependsOn(IndexedSprite::class, GraphicsDefaults.mapscene::class)
    class mapSceneSprites : IndexedSpriteArrayField(GraphicsDefaults.mapscene::class)
    @DependsOn(IndexedSprite::class, GraphicsDefaults.modicons::class)
    class modIconSprites : IndexedSpriteArrayField(GraphicsDefaults.modicons::class)
    @DependsOn(IndexedSprite::class, GraphicsDefaults.scrollbar::class)
    class scrollBarSprites : IndexedSpriteArrayField(GraphicsDefaults.scrollbar::class)

//    class runeSprites : IndexedSpritesFieldMapper("runes")
//    class titleMuteSprites : IndexedSpritesFieldMapper("title_mute")
//    class slFlagSprites : IndexedSpritesFieldMapper("sl_flags")
//    class slArrowSprites : IndexedSpritesFieldMapper("sl_arrows")
//    class slStarSprites : IndexedSpritesFieldMapper("sl_stars")

    @DependsOn(Sprite::class, GraphicsDefaults.headiconsprayer::class)
    class headIconPrayerSprites : SpriteArrayField(GraphicsDefaults.headiconsprayer::class)
    @DependsOn(Sprite::class, GraphicsDefaults.headiconspk::class)
    class headIconPkSprites : SpriteArrayField(GraphicsDefaults.headiconspk::class)
    @DependsOn(Sprite::class, GraphicsDefaults.headiconshint::class)
    class headIconHintSprites : SpriteArrayField(GraphicsDefaults.headiconshint::class)
    @DependsOn(Sprite::class, GraphicsDefaults.mapmarker::class)
    class mapMarkerSprites : SpriteArrayField(GraphicsDefaults.mapmarker::class)
    @DependsOn(Sprite::class, GraphicsDefaults.cross::class)
    class crossSprites : SpriteArrayField(GraphicsDefaults.cross::class)
    @DependsOn(Sprite::class, GraphicsDefaults.mapdots::class)
    class mapDotSprites : SpriteArrayField(GraphicsDefaults.mapdots::class)

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

    @MethodParameters("itf")
    @DependsOn(Component::class)
    class loadInterface : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == type<Component>() } }
    }

    @DependsOn(loadInterface::class)
    class loadedInterfaces : OrderMapper.InMethod.Field(loadInterface::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BooleanArray::class.type }
    }

//    @DependsOn(Component::class, Component.isHidden::class)
//    class isComponentHidden : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
//                .and { it.arguments.startsWith(type<Component>()) }
//                .and { it.arguments.size in 1..2 }
//                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Component.isHidden>().id } }
//    }

    class rootInterface : StaticUniqueMapper.Field() {
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

    @DependsOn(UnlitModel::class)
    class UnlitModel_sine : OrderMapper.InClassInitializer.Field(UnlitModel::class, -2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(UnlitModel::class)
    class UnlitModel_cosine : OrderMapper.InClassInitializer.Field(UnlitModel::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(PcmPlayer::class)
    class newPcmPlayer : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<PcmPlayer>() }
    }

    @DependsOn(PcmPlayer::class)
    class pcmPlayer0 : StaticOrderMapper.Field(0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<PcmPlayer>() }
    }

    @DependsOn(PcmPlayer::class)
    class pcmPlayer1 : StaticOrderMapper.Field(1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<PcmPlayer>() }
    }

    class WorldMapCacheName_details : WorldMapCacheNameInstance(0)
    class WorldMapCacheName_compositeMap : WorldMapCacheNameInstance(1)
    class WorldMapCacheName_compositeTexture : WorldMapCacheNameInstance(2)
    class WorldMapCacheName_area : WorldMapCacheNameInstance(3)
    class WorldMapCacheName_labels : WorldMapCacheNameInstance(4)

    @DependsOn(Rasterizer3D::class, TextureLoader::class)
    class Rasterizer3D_textureLoader : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<Rasterizer3D>() }
                .and { it.type == type<TextureLoader>() }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_isLowDetailTexture : OrderMapper.InClassInitializer.Field(Rasterizer3D::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(MapElementType::class)
    class MapElementType_cached : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MapElementType>().withDimensions(1) }
    }

    @DependsOn(MapElementType::class, EvictingDualNodeHashTable::class)
    class MapElementType_cachedSprites : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<MapElementType>() }
                .and { it.type == type<EvictingDualNodeHashTable>() }
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

    @DependsOn(Player::class, PacketBit::class)
    class updatePlayer : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<PacketBit>()) }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == type<Player>() } }
    }

    @MethodParameters("packet", "plane", "x", "y", "x0", "y0", "n")
    @DependsOn(Packet::class)
    class loadTerrain : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 7..8 }
                .and { it.arguments.startsWith(type<Packet>(), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
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

    @DependsOn(PacketBit::class)
    class PacketBit_masks : UniqueMapper.InClassInitializer.Field(PacketBit::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

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
    class overheadTextLimit : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .prevWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .prevWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextXOffsets : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .prevWithin(11) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Actor.overheadText::class, AbstractFont.stringWidth::class)
    class overheadTextAscents : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadText>().id }
                .next { it.isMethod && it.methodMark == method<AbstractFont.stringWidth>().mark }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
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

    @DependsOn(Actor.overheadTextColor::class)
    class overheadTextColors : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadTextColor>().id }
                .prevIn(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Actor.overheadTextEffect::class)
    class overheadTextEffects : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Actor.overheadTextEffect>().id }
                .prevIn(3) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
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

//    // 0 - 27
//    @DependsOn(Component.swapItems::class)
//    class inventorySlotHovered : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Component.swapItems>().id }
//                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//    }
//
//    @DependsOn(Component.swapItems::class)
//    class inventorySlotPressed : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Component.swapItems>().id }
//                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//    }

    @DependsOn(GameShell.focusGained::class)
    class hasFocus : UniqueMapper.InMethod.Field(GameShell.focusGained::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Usernamed.username::class)
    class username : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Usernamed.username>().mark }
    }

    @DependsOn(Varcs::class)
    class varcs : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Varcs>() }
    }

    @DependsOn(Scene::class, NodeDeque::class)
    class Scene_tilesDeque : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.klass == klass<Scene>() }
                .and { it.type == type<NodeDeque>() }
    }

    @DependsOn(Rasterizer2D_setClip::class)
    class minimapState : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Rasterizer2D_setClip>().id }
                .nextWithin(5) { it.opcode == ICONST_2 }
                .nextWithin(10) { it.opcode == ICONST_5 }
                .prevWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class visibilityMap : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE.withDimensions(4) }
    }

    @MethodParameters("a", "b", "c", "viewportWidth", "viewportHeight")
    @DependsOn(Scene::class)
    class Scene_buildVisiblityMap : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.klass == klass<Scene>() }
                .and { it.arguments.startsWith(IntArray::class.type, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    class Scene_viewportXMin : SceneViewportField(0)
    class Scene_viewportYMin : SceneViewportField(1)
    class Scene_viewportXMax : SceneViewportField(2)
    class Scene_viewportYMax : SceneViewportField(3)
    class Scene_viewportXCenter : SceneViewportField(4)
    class Scene_viewportYCenter : SceneViewportField(5)

    @MethodParameters("isInInstance", "bit")
    @DependsOn(xteaKeys::class)
    class loadRegions : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == PUTSTATIC && it.fieldId == field<xteaKeys>().id } }
    }

    @DependsOn(loadRegions::class)
    class isInInstance : OrderMapper.InMethod.Field(loadRegions::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class regions : LoadRegionPutStatic(IntArray::class.type, 0)
    class regionMapArchiveIds : LoadRegionPutStatic(IntArray::class.type, 1)
    class regionLandArchiveIds : LoadRegionPutStatic(IntArray::class.type, 2)
    class regionMapArchives : LoadRegionPutStatic(BYTE_TYPE.withDimensions(2), 1)
    class regionLandArchives : LoadRegionPutStatic(BYTE_TYPE.withDimensions(2), 2)

    @DependsOn(updatePlayer::class)
    class localPlayerIndex : OrderMapper.InMethod.Field(updatePlayer::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(alignComponent::class)
    class canvasWidth : OrderMapper.InMethod.Field(alignComponent::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(alignComponent::class)
    class canvasHeight : OrderMapper.InMethod.Field(alignComponent::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Component::class)
    class viewportComponent : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 6203 }
                .nextWithin(4) { it.opcode == GETSTATIC && it.fieldType == type<Component>() }
    }

    @DependsOn(doAction::class)
    class destinationX : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1001 }
                .nextWithin(15) { it.opcode == ICONST_0 }
                .nextWithin(3) { it.opcode == ILOAD && it.varVar == 0 }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doAction::class)
    class destinationY : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1001 }
                .nextWithin(15) { it.opcode == ICONST_0 }
                .nextWithin(10) { it.opcode == ILOAD && it.varVar == 1 }
                .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Sprite::class)
    class mapIcons : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1000 }
                .next { it.opcode == ANEWARRAY }
                .next { it.opcode == PUTSTATIC && it.fieldType == type<Sprite>().withDimensions(1) }
    }

    @DependsOn(mapIcons::class)
    class mapIconYs : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<mapIcons>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(mapIcons::class)
    class mapIconXs : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<mapIcons>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(mapIcons::class)
    class mapIconCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<mapIcons>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    class instanceChunkTemplates : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_4 }
                .next { it.opcode == BIPUSH && it.intOperand == 13 }
                .next { it.opcode == BIPUSH && it.intOperand == 13 }
                .nextIn(2) { it.opcode == PUTSTATIC }
    }

    @DependsOn(IntegerNode::class, NodeHashTable::class)
    class componentClickMasks : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<IntegerNode>() }
                .prev { it.opcode == GETSTATIC && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(Scene.init::class)
    class Tiles_minPlane : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Scene.init>().id }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("w", "b")
    @DependsOn(InterfaceParent::class)
    class closeInterface : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<InterfaceParent>(), BOOLEAN_TYPE) }
    }

    // inlined
//    @MethodParameters("key", "group", "type")
//    @DependsOn(InterfaceParent::class)
//    class openInterface : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<InterfaceParent>() }
//                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE) }
//    }

    @MethodParameters("id", "quantity", "n0", "n1", "n2", "b0")
    @DependsOn(Sprite::class)
    class getItemSprite : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>() }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, BOOLEAN_TYPE) }
    }

    @DependsOn(Sprite::class)
    class sceneMinimapSprite : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 512 }
                .next { it.opcode == SIPUSH && it.intOperand == 512 }
                .nextIn(2) { it.opcode == PUTSTATIC && it.fieldType == type<Sprite>() }
    }

    @MethodParameters("w0", "w1", "mode", "b")
    @DependsOn(World::class)
    class compareWorlds : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 4..5 }
                .and { it.arguments.startsWith(type<World>(), type<World>(), INT_TYPE, BOOLEAN_TYPE) }
    }

    @MethodParameters("actor", "a", "b", "c", "d", "e")
    @DependsOn(Actor::class)
    class drawActor2d : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Actor>(), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    // inlined
//    @DependsOn(drawActor2d::class)
//    class drawActors2d : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.size in 4..5 }
//                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
//                .and { it.instructions.any { it.isMethod && it.methodId == method<drawActor2d>().id } }
//    }

    class renderSelf : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "renderself" }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class showMouseOverText : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "mouseovertext" }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @MethodParameters("player", "b")
    @DependsOn(Player::class)
    class addPlayerToScene : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(type<Player>(), BOOLEAN_TYPE) }
    }

    @DependsOn(Sprite::class, AbstractArchive::class)
    @MethodParameters("archive", "group", "file")
    class readSprite : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>() }
                .and { it.arguments.size in 3..4 }
                .and { it.arguments.startsWith(type<AbstractArchive>(), INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("player", "menuArg0", "menuArg1", "menuArg2")
    @DependsOn(Player::class, Client.playerMenuOpcodes::class)
    class addPlayerToMenu : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 4..5 }
                .and { it.arguments.startsWith(type<Player>(), INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<Client.playerMenuOpcodes>().id } }
    }

    @MethodParameters("npc", "menuArg0", "menuArg1", "menuArg2")
    @DependsOn(NPCType::class)
    class addNpcToMenu : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 4..5 }
                .and { it.arguments.startsWith(type<NPCType>(), INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(EvictingDualNodeHashTable::class, HeadbarType.getFrontSprite::class)
    class HeadbarType_cachedSprites : UniqueMapper.InMethod.Field(HeadbarType.getFrontSprite::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(HeadbarType::class, EvictingDualNodeHashTable::class)
    class HeadbarType_cached : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<HeadbarType>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @MethodParameters("id")
    @DependsOn(HitmarkType::class)
    class getHitmarkType : StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<HitmarkType>() }
    }

    @DependsOn(HitmarkType.getFont::class, EvictingDualNodeHashTable::class)
    class HitmarkType_cachedFonts : UniqueMapper.InMethod.Field(HitmarkType.getFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(WorldMap::class)
    class worldMap0 : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMap>() }
    }

    @DependsOn(WorldMap::class)
    class worldMap : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<WorldMap>() }
    }

    class WorldMapLabelSize_small : WorldMapLabelSizeConstant(0)
    class WorldMapLabelSize_medium : WorldMapLabelSizeConstant(1)
    class WorldMapLabelSize_large : WorldMapLabelSizeConstant(2)

    @DependsOn(KeyHandler::class)
    class KeyHandler_pressedKeys : UniqueMapper.InClassInitializer.Field(KeyHandler::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 112 }
                .next { it.opcode == NEWARRAY }
                .next { it.opcode == PUTSTATIC && it.fieldType == BooleanArray::class.type }
    }

    @DependsOn(doAction::class)
    class selectedItemSlot : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 38 }
                .nextWithin(2) { it.node is JumpInsnNode }
                .nextWithin(15) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doAction::class)
    class selectedItemComponent : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 38 }
                .nextWithin(2) { it.node is JumpInsnNode }
                .nextWithin(15) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doAction::class)
    class selectedItemId : UniqueMapper.InMethod.Field(doAction::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 38 }
                .nextWithin(2) { it.node is JumpInsnNode }
                .nextWithin(15) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .nextWithin(6) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler_currentButton::class)
    class itemDragDuration : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<MouseHandler_currentButton>().id }
                .prevWithin(4) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
                .prev { it.opcode == IADD }
                .prevWithin(2) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(clickComponent::class)
    class componentClickX : OrderMapper.InMethod.Field(clickComponent::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(clickComponent::class)
    class componentClickY : OrderMapper.InMethod.Field(clickComponent::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(clickComponent::class)
    class componentDragDuration : OrderMapper.InMethod.Field(clickComponent::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(clickComponent::class)
    class isDraggingComponent : OrderMapper.InMethod.Field(clickComponent::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(ChatChannel::class)
    class Messages_channels : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ChatChannel>() }
                .prevIn(4) { it.opcode == GETSTATIC && it.fieldType == Map::class.type }
    }

    @DependsOn(addMessage::class, IterableDualNodeQueue::class)
    class Messages_queue : UniqueMapper.InMethod.Field(addMessage::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<IterableDualNodeQueue>() }
    }

    @MethodParameters()
    @DependsOn(draw::class)
    class drawLoggedIn : UniqueMapper.InMethod.Method(draw::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 30 }
                .nextWithin(10) { it.opcode == INVOKEVIRTUAL && it.methodOwner == type<Client>() }
    }

    @DependsOn(Component::class, Rasterizer2D_setClip::class)
    class drawInterface : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 9..10 }
                .and { it.arguments.startsWith(type<Component>().withDimensions(1)) }
                .and { it.instructions.any { it.opcode == INVOKESTATIC && it.methodId == method<Rasterizer2D_setClip>().id } }
    }

//    @DependsOn(drawInterface::class)
//    class drawInterface0 : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.size in 8..9 }
//                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
//                .and { it.instructions.any { it.opcode == INVOKESTATIC && it.methodId == method<drawInterface0>().id } }
//    }

    @MethodParameters("component", "x", "y")
    @DependsOn(Component::class, isMiniMenuOpen::class, minimapState::class)
    class clickComponent : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Component>(), INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<isMiniMenuOpen>().id } }
                .and { it.instructions.none { it.opcode == GETSTATIC && it.fieldId == field<minimapState>().id } }
    }

    @DependsOn(clickComponent::class, Component::class)
    class clickedComponent : OrderMapper.InMethod.Field(clickComponent::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<Component>() }
    }

    @DependsOn(clickComponent::class, Component::class)
    class clickedComponentParent : OrderMapper.InMethod.Field(clickComponent::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<Component>() }
    }

    @MethodParameters()
    @DependsOn(doCycle::class)
    class doCycleLoggedIn : UniqueMapper.InMethod.Method(doCycle::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 30 }
                .nextWithin(10) { it.opcode == INVOKEVIRTUAL && it.methodOwner == type<Client>() }
    }

    class TriBool_unknown : TriBoolConst(0)
    class TriBool_true : TriBoolConst(1)
    class TriBool_false : TriBoolConst(2)

//    @DependsOn(getUnderlayDefinition::class)
//    class underlays : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<getUnderlayDefinition>().id }
//                .prevWithin(20) { it.opcode == GETSTATIC && it.fieldType == BYTE_TYPE.withDimensions(3) }
//    }
//
//    @DependsOn(getUnderlayDefinition::class)
//    class underlayHues : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<getUnderlayDefinition>().id }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//    }
//
//    @DependsOn(getUnderlayDefinition::class)
//    class underlaySaturations : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<getUnderlayDefinition>().id }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//    }

//    @DependsOn(getUnderlayDefinition::class)
//    class underlayLightnesses : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<getUnderlayDefinition>().id }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//    }
//
//    @DependsOn(getUnderlayDefinition::class)
//    class underlayHueMultipliers : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<getUnderlayDefinition>().id }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//    }
//
//    @DependsOn(getUnderlayDefinition::class)
//    class underlayCounts : AllUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<getUnderlayDefinition>().id }
//                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//                .nextWithin(12) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
//    }

    @DependsOn(Messages::class)
    class Messages_count : OrderMapper.InClassInitializer.Field(Messages::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_0 }
                .next { it.opcode == PUTSTATIC }
    }

    class Model_transformTempX : ModelTransformTempInt(0)
    class Model_transformTempY : ModelTransformTempInt(1)
    class Model_transformTempZ : ModelTransformTempInt(2)

    @DependsOn(Model.toSharedSpotAnimationModel::class, Model::class)
    class Model_sharedSpotAnimationModel : UniqueMapper.InMethod.Field(Model.toSharedSpotAnimationModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<Model>() }
    }

    @DependsOn(Model.toSharedSpotAnimationModel::class, Model::class)
    class Model_sharedSpotAnimationModelFaceAlphas : UniqueMapper.InMethod.Field(Model.toSharedSpotAnimationModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == ByteArray::class.type }
    }

    @DependsOn(Model.toSharedSequenceModel::class, Model::class)
    class Model_sharedSequenceModel : UniqueMapper.InMethod.Field(Model.toSharedSequenceModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<Model>() }
    }

    @DependsOn(Model.toSharedSequenceModel::class, Model::class)
    class Model_sharedSequenceModelFaceAlphas : UniqueMapper.InMethod.Field(Model.toSharedSequenceModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == ByteArray::class.type }
    }

    @MethodParameters("id")
    @DependsOn(AnimFrameset::class)
    class getAnimFrameset : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AnimFrameset>() }
                .and { it.arguments.size in 1..2 }
    }

    @DependsOn(ClientScript::class, EvictingDualNodeHashTable::class)
    class ClientScript_cached : CachedDefinitionMapper(ClientScript::class)

    @DependsOn(ParamType::class, EvictingDualNodeHashTable::class)
    class ParamType_cached : CachedDefinitionMapper(ParamType::class)

    @MethodParameters("id")
    @DependsOn(ParamType::class)
    class getParamType : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<ParamType>() }
    }

    @DependsOn(VarType::class, EvictingDualNodeHashTable::class)
    class VarType_cached : CachedDefinitionMapper(VarType::class)

    @DependsOn(addPlayerToMenu::class, players::class)
    class combatTargetPlayerIndex : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<addPlayerToMenu>().id }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldId == field<players>().id }
                .nextWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("x", "y", "width", "height", "clear")
    class setViewportShape : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 5..6 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, BOOLEAN_TYPE) }
    }

    @DependsOn(addNpcToMenu::class, selectedSpellActionName::class)
    class isSpellSelected : UniqueMapper.InMethod.Field(addNpcToMenu::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<selectedSpellActionName>().id }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(addNpcToMenu::class, selectedSpellActionName::class)
    class selectedSpellFlags : UniqueMapper.InMethod.Field(addNpcToMenu::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<selectedSpellActionName>().id }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(mapMarkerSprites::class, players::class)
    class hintArrowType : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<mapMarkerSprites>().id }
                .prevWithin(35) { it.opcode == GETSTATIC && it.fieldId == field<players>().id }
                .prevWithin(11) { it.isLabel }
                .nextWithin(2) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(mapMarkerSprites::class, players::class)
    class hintArrowPlayerIndex : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<mapMarkerSprites>().id }
                .prevWithin(35) { it.opcode == GETSTATIC && it.fieldId == field<players>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(mapMarkerSprites::class, npcs::class)
    class hintArrowNpcIndex : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<mapMarkerSprites>().id }
                .prevWithin(35) { it.opcode == GETSTATIC && it.fieldId == field<npcs>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(mapMarkerSprites::class, baseY::class)
    class hintArrowY : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<mapMarkerSprites>().id }
                .prevWithin(18) { it.opcode == GETSTATIC && it.fieldId == field<baseY>().id }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(mapMarkerSprites::class, baseX::class)
    class hintArrowX : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<mapMarkerSprites>().id }
                .prevWithin(35) { it.opcode == GETSTATIC && it.fieldId == field<baseX>().id }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(hintArrowType::class)
    class hintArrowSubX : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<hintArrowType>().id }
                .nextWithin(3) { it.opcode == BIPUSH && it.intOperand == 64 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(hintArrowType::class)
    class hintArrowSubY : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<hintArrowType>().id }
                .nextWithin(3) { it.opcode == BIPUSH && it.intOperand == 64 }
                .nextWithin(2) { it.opcode == BIPUSH && it.intOperand == 64 }
                .next { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(hintArrowY::class, baseY::class)
    class hintArrowHeight : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<hintArrowY>().id }
                .next { it.opcode == GETSTATIC && it.fieldId == field<baseY>().id }
                .nextWithin(15) { it.opcode == INVOKESTATIC }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(GameShell.startThread::class)
    class revision : OrderMapper.InMethod.Field(GameShell.startThread::class, -1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(TaskHandler::class)
    class taskHandler : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<TaskHandler>() }
    }

    @DependsOn(GameShell.startThread::class)
    class applet : UniqueMapper.InMethod.Field(GameShell.startThread::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == Applet::class.type }
    }

    @DependsOn(GameShell.kill::class)
    class isKilled : OrderMapper.InMethod.Field(GameShell.kill::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

//    @MethodParameters()
//    @DependsOn(componentDragDuration::class)
//    class dragComponent : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.isEmpty() }
//                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<componentDragDuration>().id } }
//    }

    @MethodParameters("component")
    @DependsOn(Component::class, Component.clickMask::class)
    class getComponentClickMask : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments == listOf(type<Component>()) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Component.clickMask>().id } }
    }

//    @DependsOn(Component::class, clickedComponent::class, clickedComponentParent::class, dragComponent::class)
//    class componentDragTarget : UniqueMapper.InMethod.Field(dragComponent::class) {
//        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<Component>() }
//                .and { it.fieldId != field<clickedComponent>().id && it.fieldId != field<clickedComponentParent>().id }
//    }

    @MethodParameters("components", "parentId", "a", "b", "c", "d", "x", "y")
    @DependsOn(Component::class)
    class updateInterface : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Component>().withDimensions(1), INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE,
                        INT_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(ArchiveLoader::class)
    class archiveLoaders : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ArchiveLoader>() }
                .prevWithin(3) { it.opcode == GETSTATIC && it.fieldType == ArrayList::class.type }
    }

    @DependsOn(ArchiveLoader::class)
    class archiveLoaderArchive : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ArchiveLoader>() }
                .prevWithin(2) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Timer::class)
    class timer : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Timer>() }
    }

    @MethodParameters("component", "parentWidth", "parentHeight")
    @DependsOn(Component::class, Component.xAlignment::class)
    class alignComponentPosition : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Component>(), INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.isField && it.fieldId == field<Component.xAlignment>().id } }
    }

    @MethodParameters("component", "parentWidth", "parentHeight", "b")
    @DependsOn(Component::class, Component.widthAlignment::class)
    class alignComponentSize : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Component>(), INT_TYPE, INT_TYPE, BOOLEAN_TYPE) }
                .and { it.instructions.any { it.isField && it.fieldId == field<Component.widthAlignment>().id } }
    }

    @DependsOn(newPcmPlayer::class)
    class soundSystemExecutor : UniqueMapper.InMethod.Field(newPcmPlayer::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == ScheduledExecutorService::class.type }
    }

//    //    class newSecureRandom : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == SecureRandom::class.type }
//    }

    @DependsOn(SecureRandomFuture::class)
    class secureRandomFuture : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<SecureRandomFuture>() }
    }

    class secureRandom : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == SecureRandom::class.type }
    }

//    @MethodParameters("index", "archive", "record")
//    //    @DependsOn(Sprite::class, AbstractIndexCache::class)
//    class readSprites : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<Sprite>().withDimensions(1) }
//                .and { it.arguments == listOf(type<AbstractIndexCache>(), INT_TYPE, INT_TYPE) }
//    }

    @DependsOn(GraphicsDefaults::class)
    class spriteIds : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<GraphicsDefaults>() }
    }

//    @MethodParameters("index", "archive", "record")
//    //    @DependsOn(IndexedSprite::class, AbstractIndexCache::class)
//    class readIndexedSprites : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<IndexedSprite>().withDimensions(1) }
//                .and { it.arguments == listOf(type<AbstractIndexCache>(), INT_TYPE, INT_TYPE) }
//    }

//    @MethodParameters("index", "archive", "record")
//    //    @DependsOn(AbstractIndexCache::class)
//    class loadSprites : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
//                .and { it.arguments == listOf(type<AbstractIndexCache>(), INT_TYPE, INT_TYPE) }
//    }

    @MethodParameters()
    @DependsOn(GameShell.kill0::class)
    class kill0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<GameShell.kill0>().mark }
    }

    class Interpreter_stringStack : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1000 }
                .next { it.opcode == ANEWARRAY }
                .next { it.opcode == PUTSTATIC && it.fieldType == Array<String>::class.type }
    }

    @DependsOn(Interpreter::class)
    class Interpreter_intStack : UniqueMapper.InClassInitializer.Field(Interpreter::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 1000 }
                .next { it.opcode == NEWARRAY }
                .next { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Interpreter::class)
    class Interpreter_arrays : UniqueMapper.InClassInitializer.Field(Interpreter::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 5000 }
                .next { it.opcode == MULTIANEWARRAY }
                .next { it.opcode == PUTSTATIC && it.fieldType == Array<IntArray>::class.type }
    }

    @DependsOn(Interpreter::class)
    class Interpreter_arrayLengths : UniqueMapper.InClassInitializer.Field(Interpreter::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_5 }
                .next { it.opcode == NEWARRAY }
                .next { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(ClientScriptFrame::class)
    class Interpreter_frames : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<ClientScriptFrame>().withDimensions(1) }
    }

    @DependsOn(Interpreter_frames::class)
    class Interpreter_frameDepth : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Interpreter_frames>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Interpreter_intStack::class)
    class Interpreter_intStackSize : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Interpreter_intStack>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Interpreter_stringStack::class)
    class Interpreter_stringStackSize : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Interpreter_stringStack>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(ClientScriptFrame.stringLocals::class)
    class Interpreter_stringLocals : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<ClientScriptFrame.stringLocals>().id }
                .next { it.opcode == PUTSTATIC }
    }

    @DependsOn(ClientScriptFrame.intLocals::class)
    class Interpreter_intLocals : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<ClientScriptFrame.intLocals>().id }
                .next { it.opcode == PUTSTATIC }
    }

    @MethodParameters("scriptEvent", "n")
    @DependsOn(ClientScriptEvent::class)
    class runClientScript0 : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<ClientScriptEvent>(), INT_TYPE) }
    }

    @MethodParameters("scriptEvent")
    @DependsOn(ClientScriptEvent::class)
    class runClientScript : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<ClientScriptEvent>()) }
    }

    @DependsOn(alignComponentSize::class, NodeDeque::class)
    class clientScriptEvents : UniqueMapper.InMethod.Field(alignComponentSize::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @MethodParameters("brightness")
    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_setBrightness : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer3D>() }
                .and { it.returnType == VOID_TYPE && it.arguments == listOf(DOUBLE_TYPE) }
    }

    @MethodParameters("brightness", "hsMin", "hsMax")
    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_buildPalette : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer3D>() }
                .and { it.returnType == VOID_TYPE && it.arguments == listOf(DOUBLE_TYPE, INT_TYPE, INT_TYPE) }
    }

    @DependsOn(Rasterizer3D_buildPalette::class)
    class Rasterizer3D_colorPalette : UniqueMapper.InMethod.Field(Rasterizer3D_buildPalette::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(PlayerAppearance.getModel::class, EvictingDualNodeHashTable::class)
    class PlayerAppearance_cachedModels : UniqueMapper.InMethod.Field(PlayerAppearance.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(LocType.getModel::class, EvictingDualNodeHashTable::class)
    class LocType_cachedModels : UniqueMapper.InMethod.Field(LocType.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
    }

    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_alpha : OrderMapper.InClassInitializer.Field(Rasterizer3D::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(LocSound::class, NodeDeque::class)
    class objectSounds : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<LocSound>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeDeque>() }
    }

    @MethodParameters("textureLoader")
    @DependsOn(Rasterizer3D::class, TextureLoader::class)
    class Rasterizer3D_setTextureLoader : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer3D>() }
                .and { it.arguments == listOf(type<TextureLoader>()) }
    }

    @DependsOn(TextureProvider::class)
    class textureProvider : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<TextureProvider>() }
    }

    @DependsOn(PlatformInfo::class)
    class platformInfo : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PlatformInfo>() }
    }

    @DependsOn(Scene.draw::class)
    class Scene_drawnCount : OrderMapper.InMethod.Field(Scene.draw::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene::class, Occluder::class)
    class Scene_addOccluder : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Scene>() }
                .and { it.arguments.size == 8 }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == type<Occluder>() } }
    }

    @DependsOn(Occluder::class)
    class Scene_planeOccluders : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Occluder>().withDimensions(2) }
    }

    @DependsOn(Occluder::class)
    class Scene_currentOccluders : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Occluder>().withDimensions(1) }
    }

    @DependsOn(Scene_addOccluder::class)
    class Scene_planeOccluderCounts : UniqueMapper.InMethod.Field(Scene_addOccluder::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(Scene::class)
    class Scene_currentOccludersCount : UniqueMapper.InClassInitializer.Field(Scene::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 500 }
                .prev { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("component")
    @DependsOn(Component::class, Component.cs1Comparisons::class)
    class runCs1 : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments == listOf(type<Component>()) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<Component.cs1Comparisons>().id } }
    }

    @DependsOn(loadInterface::class, AbstractArchive::class)
    class Component_archive : UniqueMapper.InMethod.Field(loadInterface::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
    }

    @DependsOn(drawInterface::class, Component.color2::class, Component::class)
    class mousedOverComponentIf1 : UniqueMapper.InMethod.Field(drawInterface::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<Component.color2>().id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == type<Component>() }
    }

    @DependsOn(Texture.animate::class)
    class Texture_animatedPixels : UniqueMapper.InMethod.Field(Texture.animate::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(secureRandom::class)
    @MethodParameters()
    class doCycleLoggedOut : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() }
                .and { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == PUTSTATIC && it.fieldId == field<secureRandom>().id } }
    }

    @DependsOn(doCycleLoggedOut::class)
    class loginState : OrderMapper.InMethod.Field(doCycleLoggedOut::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class Interpreter_calendar : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> {
            it.opcode == INVOKESTATIC &&
                    it.methodName == "getInstance" &&
                    it.methodOwner == Calendar::class.type &&
                    it.methodType.argumentTypes.isEmpty()
        }.next { it.opcode == PUTSTATIC }
    }

    @DependsOn(doCycleLoggedOut::class, TaskHandler.newSocketTask::class)
    class socketTask : UniqueMapper.InMethod.Field(doCycleLoggedOut::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<TaskHandler.newSocketTask>().id }
                .next { it.opcode == PUTSTATIC }
    }

    @DependsOn(init::class)
    class useBufferedSocket : UniqueMapper.InMethod.Field(init::class) {
        override val predicate = predicateOf<Instruction2> { it.isLabel }
                .next { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class clientType : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 6519 }
                .nextWithin(20) { it.isLabel }
                .prevWithin(8) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

//    class language : StaticUniqueMapper.Field() {
//        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "/l=" }
//                .nextIn(2) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
//    }

    @DependsOn(doCycleLoggedIn::class)
    class rebootTimer : OrderMapper.InMethod.Field(doCycleLoggedIn::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(WorldMapEvent::class)
    class worldMapEvent : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<WorldMapEvent>() }
    }

    @DependsOn(Client.addNpcToMenu::class, NPCType.isFollower::class)
    class followerOpsLowPriority : OrderMapper.InMethod.Field(Client.addNpcToMenu::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldId == field<NPCType.isFollower>().id }
                .next { it.opcode == IFEQ }
                .next { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(Client.addNpcToMenu::class)
    class followerIndex : OrderMapper.InMethod.Field(Client.addNpcToMenu::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    class shiftClickDrop : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3117 }
                .nextWithin(18) { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(KeyHandler_pressedKeys::class)
    class tapToDrop : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 81 }
                .prev { it.opcode == GETSTATIC && it.fieldId == field<KeyHandler_pressedKeys>().id }
                .prevIn(2) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class showMouseCross : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3125 }
                .nextWithin(18) { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class showLoadingMessages : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3126 }
                .nextWithin(18) { it.opcode == PUTSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(currentTimeMs::class, doCycleJs5Connect::class)
    class js5StartTimeMs : OrderMapper.InMethod.Field(doCycleJs5Connect::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<currentTimeMs>().id }
                .next { it.opcode == PUTSTATIC }
    }

    @MethodParameters()
    class doCycleJs5Connect : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == "js5crc" } }
    }

    @DependsOn(doCycleJs5Connect::class)
    class js5ConnectState : OrderMapper.InMethod.Field(doCycleJs5Connect::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_0 }
                .nextWithin(2) { it.opcode == IF_ICMPNE }
                .prevWithin(2) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doCycleJs5Connect::class, TaskHandler.newSocketTask::class)
    class js5SocketTask : UniqueMapper.InMethod.Field(doCycleJs5Connect::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<TaskHandler.newSocketTask>().id }
                .next { it.opcode == PUTSTATIC }
    }

    @DependsOn(doCycleJs5Connect::class, TaskHandler.newSocketTask::class)
    class NetCache_crcMismatches : OrderMapper.InMethod.Field(doCycleJs5Connect::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doCycleJs5Connect::class, TaskHandler.newSocketTask::class)
    class NetCache_ioExceptions : OrderMapper.InMethod.Field(doCycleJs5Connect::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doCycleJs5Connect::class, AbstractSocket::class)
    class js5Socket : OrderMapper.InMethod.Field(doCycleJs5Connect::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<AbstractSocket>() }
    }

    @MethodParameters("code")
    class js5Error : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments == listOf(INT_TYPE) }
                .and { it.returnType == VOID_TYPE }
    }

    @DependsOn(js5Error::class)
    class js5Errors : OrderMapper.InMethod.Field(js5Error::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == IADD }
                .next { it.opcode == PUTSTATIC }
    }

    @DependsOn(overheadText::class, viewportTempY::class)
    class chatEffects : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldId == field<viewportTempY>().id }
                .next { it.opcode == GETSTATIC && it.fieldId == field<overheadText>().id }
                .nextWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Strings_loadingPleaseWait::class, Rasterizer2D_fillRectangle::class)
    class isLoading : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<Strings_loadingPleaseWait>().id }
                .prev { it.isMethod && it.methodId == method<Rasterizer2D_fillRectangle>().id }
                .prevWithin(9) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    @DependsOn(doCycle::class)
    class doCycleJs5 : OrderMapper.InMethod.Method(doCycle::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL }
    }

    @DependsOn(showLoadingMessages::class)
    class drawLoadingMessage : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(String::class.type, BOOLEAN_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<showLoadingMessages>().id } }
    }

    @DependsOn(Font::class)
    class drawTitle : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<Font>(), type<Font>(), type<Font>()) }
    }

    class isCameraLocked : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 5504 }
                .nextWithin(18) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(AbstractFont.drawCenteredWave::class)
    class viewportDrawCount : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKEVIRTUAL && it.methodMark == method<AbstractFont.drawCenteredWave>().mark }
                .prev { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(viewportDrawCount::class)
    class tileLastDrawnActor : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<viewportDrawCount>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == Array<IntArray>::class.type }
    }

    @MethodParameters("rgb", "brightness")
    @DependsOn(Rasterizer3D::class)
    class Rasterizer3D_brighten : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer3D>() }
                .and { it.returnType == INT_TYPE }
                .and { it.arguments == listOf(INT_TYPE, DOUBLE_TYPE) }
    }

    @DependsOn(VorbisSample::class)
    class readVorbisSample : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<VorbisSample>() }
    }

    @DependsOn(MusicTrack::class)
    class readTrack : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<MusicTrack>() }
    }

    // 22050
    @DependsOn(DevicePcmPlayer.init::class)
    class PcmPlayer_sampleRate : UniqueMapper.InMethod.Field(DevicePcmPlayer.init::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(newPcmPlayer::class, SoundSystem::class)
    class pcmPlayerCount : UniqueMapper.InMethod.Field(newPcmPlayer::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == NEW && it.typeType == type<SoundSystem>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Decimator::class)
    class decimator : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Decimator>() }
    }

    @DependsOn(soundEffects::class)
    class soundEffectCount : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<soundEffects>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(soundEffects::class)
    class soundEffectIds : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<soundEffects>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
                .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == IntArray::class.type }
    }

    @DependsOn(soundEffects::class)
    class queueSoundEffect : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETSTATIC && it.fieldId == field<soundEffects>().id } }
    }

    @DependsOn(PcmStreamMixer::class)
    class pcmStreamMixer : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<PcmStreamMixer>() }
    }

    @MethodParameters("stream")
    @DependsOn(PcmStream::class)
    class PcmStream_disable : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<PcmStream>()) }
    }

    class clearIntArray : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(IntArray::class.type, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == ICONST_0 } == 9 }
    }

    @DependsOn(SoundCache::class)
    class soundCache : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<SoundCache>() }
    }

    @DependsOn(MusicTrack::class)
    class musicTrack : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MusicTrack>() }
    }

    @DependsOn(MidiPcmStream::class)
    class midiPcmStream : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MidiPcmStream>() }
    }

    @DependsOn(musicTrack::class, AbstractArchive::class)
    class musicTrackGroupId : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<musicTrack>().id }
                .nextIn(2) { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
                .next { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(musicTrack::class, AbstractArchive::class)
    class musicTrackFileId : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<musicTrack>().id }
                .nextIn(2) { it.opcode == GETSTATIC && it.fieldType == type<AbstractArchive>() }
                .nextIn(2) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(musicTrack::class)
    class musicTrackBoolean : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<musicTrack>().id }
                .next { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

//    @DependsOn(MusicPatch::class)
//    class readMusicPatch : IdentityMapper.StaticMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == type<MusicPatch>() }
//    }

    @DependsOn(drawInterface::class, isItemSelected::class)
    class dragItemSlotSource : UniqueMapper.InMethod.Field(drawInterface::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<isItemSelected>().id }
                .prevWithin(5) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doCycleLoggedIn::class, dragItemSlotSource::class)
    class dragItemSlotDestination : UniqueMapper.InMethod.Field(doCycleLoggedIn::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<dragItemSlotSource>().id }
                .prev { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(doCycleLoggedIn::class, dragItemSlotDestination::class, Component::class)
    class dragInventoryComponent : UniqueMapper.InMethod.Field(doCycleLoggedIn::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldId == field<dragItemSlotDestination>().id }
                .nextWithin(3) { it.opcode == GETSTATIC && it.fieldType == type<Component>() }
    }

    @DependsOn(Scene.menuOpen::class)
    class Scene_selectedPlane : OrderMapper.InMethod.Field(Scene.menuOpen::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.menuOpen::class)
    class Scene_selectedScreenX : OrderMapper.InMethod.Field(Scene.menuOpen::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.menuOpen::class)
    class Scene_selectedScreenY : OrderMapper.InMethod.Field(Scene.menuOpen::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.menuOpen::class)
    class Scene_selectedX : OrderMapper.InMethod.Field(Scene.menuOpen::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(Scene.menuOpen::class)
    class Scene_selectedY : OrderMapper.InMethod.Field(Scene.menuOpen::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("id")
    @DependsOn(MapElementType::class)
    class getMapElementType : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<MapElementType>() }
                .and { it.arguments == listOf(INT_TYPE) }
    }

    @DependsOn(WorldMap.flashCategory::class)
    class MapElementType_count : UniqueMapper.InMethod.Field(WorldMap.flashCategory::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(DefaultsGroup::class)
    class DefaultsGroup_graphics : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<DefaultsGroup>() }
    }

    class playerMod : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 3323 }
                .nextWithin(10) { it.opcode == GETSTATIC && it.fieldType == BOOLEAN_TYPE }
    }

    class camFollowHeight : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == 5531 }
                .nextWithin(50) { it.isLabel }
                .prevWithin(15) { it.opcode == GETSTATIC && it.fieldType == INT_TYPE }
    }

    @MethodParameters("n", "positiveSign")
    class intToString : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == String::class.type }
                .and { it.arguments == listOf(INT_TYPE, BOOLEAN_TYPE) }
    }

    @DependsOn(VorbisCodebook::class)
    class VorbisSample_codebooks : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<VorbisCodebook>().withDimensions(1) }
    }

    @DependsOn(VorbisFloor::class)
    class VorbisSample_floors : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<VorbisFloor>().withDimensions(1) }
    }

    @DependsOn(VorbisResidue::class)
    class VorbisSample_residues : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<VorbisResidue>().withDimensions(1) }
    }

    @DependsOn(VorbisMapping::class)
    class VorbisSample_mappings : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<VorbisMapping>().withDimensions(1) }
    }

    @DependsOn(readVorbisSample::class)
    class VorbisSample_loadSetupHeader : OrderMapper.InMethod.Method(readVorbisSample::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(VorbisSample_loadSetupHeader::class)
    class VorbisSample_decodeSetupHeader : OrderMapper.InMethod.Method(VorbisSample_loadSetupHeader::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(VorbisSample_loadSetupHeader::class)
    class VorbisSample_setupHeaderLoaded : OrderMapper.InMethod.Field(VorbisSample_loadSetupHeader::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC }
    }

    @DependsOn(VorbisSample_decodeSetupHeader::class)
    class VorbisSample_setStream : OrderMapper.InMethod.Method(VorbisSample_decodeSetupHeader::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(VorbisSample_setStream::class)
    class VorbisSample_stream : OrderMapper.InMethod.Field(VorbisSample_setStream::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(VorbisSample_setStream::class)
    class VorbisSample_bytePos : OrderMapper.InMethod.Field(VorbisSample_setStream::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(VorbisSample_setStream::class)
    class VorbisSample_bitPos : OrderMapper.InMethod.Field(VorbisSample_setStream::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(VorbisSample_decodeSetupHeader::class)
    class VorbisSample_blocksize0 : OrderMapper.InMethod.Field(VorbisSample_decodeSetupHeader::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(VorbisSample_decodeSetupHeader::class)
    class VorbisSample_blocksize1 : OrderMapper.InMethod.Field(VorbisSample_decodeSetupHeader::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC }
    }

    @DependsOn(VorbisSample.decodeAudio::class)
    class VorbisSample_readBit : OrderMapper.InMethod.Method(VorbisSample.decodeAudio::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }

    @DependsOn(VorbisSample.decodeAudio::class)
    class VorbisSample_readBits : OrderMapper.InMethod.Method(VorbisSample.decodeAudio::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC }
    }
}