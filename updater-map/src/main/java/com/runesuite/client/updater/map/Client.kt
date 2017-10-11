package com.runesuite.client.updater.map

import com.hunterwb.kxtra.collections.list.startsWith
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
import org.objectweb.asm.Opcodes.BIPUSH
import org.objectweb.asm.Opcodes.CHECKCAST
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.GETSTATIC
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Opcodes.ICONST_2
import org.objectweb.asm.Opcodes.ILOAD
import org.objectweb.asm.Opcodes.IMUL
import org.objectweb.asm.Opcodes.INVOKESTATIC
import org.objectweb.asm.Opcodes.ISHR
import org.objectweb.asm.Opcodes.LDC
import org.objectweb.asm.Opcodes.PUTSTATIC
import org.objectweb.asm.Opcodes.RETURN
import org.objectweb.asm.Opcodes.SIPUSH
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.BYTE_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import java.applet.Applet
import java.io.File
import java.lang.management.GarbageCollectorMXBean
import java.net.URL

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
    class cursorColor : OrderMapper.InMethod.Field(menuAction::class, 2) {
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

    @DependsOn(AbstractGraphicsProvider::class)
    class graphicsProvider : StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractGraphicsProvider>() }
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
    class viewportScale : OrderMapper.InMethod.Field(worldToScreen::class, -1) {
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
    class viewportY : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == RETURN }
                .prev { it.opcode == PUTSTATIC && it.fieldId == field<viewportHeight>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldId == field<viewportWidth>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(viewportHeight::class, viewportWidth::class, viewportY::class)
    class viewportX : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == RETURN }
                .prev { it.opcode == PUTSTATIC && it.fieldId == field<viewportHeight>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldId == field<viewportWidth>().id }
                .prevWithin(5) { it.opcode == PUTSTATIC && it.fieldId == field<viewportY>().id }
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
    class mouseHandler : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseHandler>() }
    }

    @SinceVersion(141)
    @DependsOn(MouseWheel::class)
    class mouseWheel : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<MouseWheel>() }
    }

    @DependsOn(KeyHandler::class)
    class keyHandler : IdentityMapper.StaticField() {
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

    @DependsOn(ClassInfo::class, NodeDeque2::class)
    class classInfos : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ClassInfo>() }
                .prevWithin(4) { it.opcode == GETSTATIC && it.fieldType == type<NodeDeque2>() }
    }

    @DependsOn(NpcDefinition::class, NodeCache::class)
    class npcDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<NpcDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(Sprite::class, NodeCache::class)
    class spriteCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<Sprite>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(KitDefinition::class, NodeCache::class)
    class kitDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<KitDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(SpotAnimationDefinition::class, NodeCache::class)
    class spotAnimationDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<SpotAnimationDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(ItemDefinition::class, NodeCache::class)
    class itemDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ItemDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(VarbitDefinition::class, NodeCache::class)
    class varbitDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<VarbitDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(SequenceDefinition::class, NodeCache::class)
    class sequenceDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<SequenceDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(ObjectDefinition::class, NodeCache::class)
    class objectDefinitionCache : StaticUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == type<ObjectDefinition>() }
                .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
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

        // todo : static field called through subclass, fix to be actual class
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
    @DependsOn(AbstractGraphicsProvider.draw::class)
    class methodDraw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == method<AbstractGraphicsProvider.draw>().id } }
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
    @MethodParameters("x", "y", "value")
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

    @MethodParameters("x", "y", "length", "value")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawHorizontalLine : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == IMUL } == 1 }
    }

    @MethodParameters("x", "y", "length", "value")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawVerticalLine : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == IMUL } == 2 }
    }

    @MethodParameters("x", "y", "width", "height", "value")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_drawRectangle : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.opcode == INVOKESTATIC } == 4 }
    }

    @MethodParameters("array")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_getClip : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE.withDimensions(1)) }
                .and { it.instructions.any { it.opcode == GETSTATIC } }
    }

    @MethodParameters("array")
    @DependsOn(Rasterizer2D::class)
    class Rasterizer2D_setClip : IdentityMapper.StaticMethod() {
        override val predicate = predicateOf<Method2> { it.klass == klass<Rasterizer2D>() }
                .and { it.arguments == listOf(INT_TYPE.withDimensions(1)) }
                .and { it.instructions.any { it.opcode == PUTSTATIC } }
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
    @DependsOn(addBoundingBox2D::class, NodeDeque2::class)
    class boundingBoxes : UniqueMapper.InMethod.Field(addBoundingBox2D::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeDeque2>() }
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

    @DependsOn(Strings::class)
    class Strings_SPACE : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == " " }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_TAKE : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Take" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_EXAMINE : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Examine" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_ATTACK : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Attack" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_DROP : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Drop" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_MORE_OPTIONS : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == " more options" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_LEVEL : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "level-" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_SKILL : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "skill-" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_USE : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Use" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_LOADING_PLEASE_WAIT : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Loading - please wait." }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_CONNECTION_LOST : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Connection lost" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_ATTEMPTING_TO_REESTABLISH : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please wait - attempting to reestablish" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_ALREADY_A_FRIEND : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == " is already on your friend list" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_REMOVE_IGNORE : OrderMapper.InClassInitializer.Field(Strings::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please remove " }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_FROM_IGNORE : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == " from your ignore list first" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_REMOVE_FRIEND : OrderMapper.InClassInitializer.Field(Strings::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please remove " }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_CHOOSE_OPTION : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Choose Option" }
                .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
    }

    @DependsOn(Strings::class)
    class Strings_PLEASE_WAIT : UniqueMapper.InClassInitializer.Field(Strings::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "Please wait..." }
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
    @DependsOn(NodeHashTable2::class)
    class messages : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeHashTable2>() }
    }

    @SinceVersion(141)
    @DependsOn(Client::class)
    class client : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<Client>() }
    }

    @DependsOn(Widget.getFont::class, NodeCache::class)
    class widgetFontCache : UniqueMapper.InMethod.Field(Widget.getFont::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @DependsOn(Widget.getModel::class, NodeCache::class)
    class widgetModelCache : UniqueMapper.InMethod.Field(Widget.getModel::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETSTATIC && it.fieldType == type<NodeCache>() }
    }

    @SinceVersion(141)
    @DependsOn(Type0::class)
    class Type0_INTEGER : OrderMapper.InClassInitializer.Field(Type0::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<Type0>() }
    }

    @SinceVersion(141)
    @DependsOn(Type0::class)
    class Type0_LONG : OrderMapper.InClassInitializer.Field(Type0::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<Type0>() }
    }

    @SinceVersion(141)
    @DependsOn(Type0::class)
    class Type0_STRING : OrderMapper.InClassInitializer.Field(Type0::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<Type0>() }
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
    class cacheDat2 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.dat2" }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == type<BufferedFile>() }
    }

    @DependsOn(BufferedFile::class)
    class cacheIdx255 : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.idx255" }
                .nextWithin(25) { it.opcode == PUTSTATIC && it.fieldType == type<BufferedFile>() }
    }

    @DependsOn(BufferedFile::class)
    class cacheIdxs : AllUniqueMapper.Field() {
        override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == "main_file_cache.idx" }
                .prevWithin(25) { it.opcode == GETSTATIC && it.fieldType == type<BufferedFile>().withDimensions(1) }
    }

    class cacheIndexCount : AllUniqueMapper.Field() {
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

    @DependsOn(CacheIndex::class)
    class cacheIndex255 : IdentityMapper.StaticField() {
        override val predicate = predicateOf<Field2> { it.type == type<CacheIndex>() }
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
    class mouseHandlerX : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mouseMoved::class)
    class mouseHandlerY : OrderMapper.InMethod.Field(MouseHandler.mouseMoved::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class mouseHandlerPressedX : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }

    @DependsOn(MouseHandler.mousePressed::class)
    class mouseHandlerPressedY : OrderMapper.InMethod.Field(MouseHandler.mousePressed::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTSTATIC && it.fieldType == INT_TYPE }
    }
}