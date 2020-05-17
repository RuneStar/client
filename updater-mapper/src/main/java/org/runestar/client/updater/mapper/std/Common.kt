package org.runestar.client.updater.mapper.std

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.BYTE_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.std.classes.*
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import kotlin.reflect.KClass

@DependsOn(Strings::class)
abstract class StringsUniqueMapper(string: String) : UniqueMapper.InClassInitializer.Field(Strings::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == string }
            .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
}

@DependsOn(Formatting::class)
abstract class FormattingUniqueMapper(string: String) : UniqueMapper.InClassInitializer.Field(Formatting::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == string }
            .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
}

@DependsOn(Client.newArchive::class, Archive::class)
abstract class ArchiveFieldMapper(order: Int) : StaticOrderMapper.Field(order) {
    override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<Client.newArchive>().id }
            .next { it.opcode == PUTSTATIC && it.fieldType == type<Archive>() }
}

//@DependsOn(EvictingDualNodeHashTable::class)
abstract class CachedDefinitionMapper(classMapper: KClass<out Mapper<Class2>>) : StaticUniqueMapper.Field() {
    override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == context.classes.getValue(classMapper).type }
            .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingDualNodeHashTable>() }
}

@DependsOn(IndexedSprite::class)
abstract class IndexedSpritesFieldMapper(s: String) : AllUniqueMapper.Field() {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == s }
            .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == type<IndexedSprite>().withDimensions(1) }
}

@DependsOn(Instrument::class)
abstract class InstrumentStaticIntArrayMapper(index: Int) : OrderMapper.InClassInitializer.Field(Instrument::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == IntArray::class.type }
}

@DependsOn(AbstractFont::class)
abstract class AbstractFontStaticIntMapper(index: Int) : OrderMapper.InClassInitializer.Field(AbstractFont::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
}

@DependsOn(PlayerType::class)
abstract class PlayerTypeInstance(index: Int) : OrderMapper.InClassInitializer.Field(PlayerType::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<PlayerType>() }
}

@DependsOn(WorldMapCacheName::class)
abstract class WorldMapCacheNameInstance(index: Int) : OrderMapper.InClassInitializer.Field(WorldMapCacheName::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<WorldMapCacheName>() }
}

@DependsOn(ByteArrayPool::class)
abstract class ByteArrayPoolArray(index: Int) : OrderMapper.InClassInitializer.Field(ByteArrayPool::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == BYTE_TYPE.withDimensions(2) }
}

@DependsOn(ByteArrayPool::class)
abstract class ByteArrayPoolCount(index: Int) : OrderMapper.InClassInitializer.Field(ByteArrayPool::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
}

//@DependsOn(DesktopPlatformInfoProvider.length::class)
//abstract class PlatformInfoString(index: Int) : OrderMapper.InMethod.Field(DesktopPlatformInfoProvider.length::class, index) {
//    override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == String::class.type }
//}

@DependsOn(Client.friendSystem::class)
abstract class UserComparatorClass(opcode: Int) : AllUniqueMapper.Class() {
    override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == opcode }
            .nextWithin(30) { it.isField && it.fieldId == field<Client.friendSystem>().id }
            .nextWithin(3) { it.opcode == NEW }
}

@DependsOn(Client.Scene_buildVisiblityMap::class)
abstract class SceneViewportField(index: Int) : OrderMapper.InMethod.Field(Client.Scene_buildVisiblityMap::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
}

@DependsOn(Client.loadRegions::class)
abstract class LoadRegionPutStatic(type: Type, index: Int) : OrderMapper.InMethod.Field(Client.loadRegions::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type }
}

@DependsOn(SpriteMask::class)
abstract class SpriteMaskConstructorField(type: Type, index: Int) : OrderMapper.InConstructor.Field(SpriteMask::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type }
}

@DependsOn(Actor::class)
abstract class ActorHitmarkField(index: Int) : OrderMapper.InConstructor.Field(Actor::class, index, 5) {
    override val predicate = predicateOf<Instruction2> { it.opcode == ICONST_4 }
            .nextIn(2) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

abstract class ConstructorPutField(klass: KClass<out Mapper<Class2>>, index: Int, type: Type) : OrderMapper.InConstructor.Field(klass, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type }
}

abstract class MethodPutField(method: KClass<out Mapper<Method2>>, index: Int, type: Type) : OrderMapper.InMethod.Field(method, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type }
}

@DependsOn(WorldMapLabelSize::class)
abstract class WorldMapLabelSizeConstant(val index: Int) : OrderMapper.InClassInitializer.Field(WorldMapLabelSize::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<WorldMapLabelSize>() }
}

@DependsOn(TriBool::class)
abstract class TriBoolConst(index: Int) : OrderMapper.InClassInitializer.Field(TriBool::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<TriBool>() }
}

@DependsOn(Model.animate::class)
abstract class ModelTransformTempInt(index: Int) : OrderMapper.InMethod.Field(Model.animate::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
}

@DependsOn(Client.loadClientScript::class, ClientScript::class)
abstract class ScriptField(index: Int, type: Type) : OrderMapper.InMethod.Field(Client.loadClientScript::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type && it.fieldOwner == type<ClientScript>() }
}

@DependsOn(Component.decodeLegacy::class)
abstract class WidgetInvArray(index: Int) : OrderMapper.InMethod.Field(Component.decodeLegacy::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 20 }
            .nextIn(2) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

@DependsOn(Component.decodeLegacy::class)
abstract class Widget10Array(index: Int) : OrderMapper.InMethod.Field(Component.decodeLegacy::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == NEWARRAY && it.intOperand == 10 }
            .next { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

@DependsOn(Component.decode::class)
abstract class WidgetListener(index: Int) : OrderMapper.InMethod.Field(Component.decode::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == Array<Any?>::class.type }
}

@DependsOn(Component.decode::class)
abstract class WidgetListener2(index: Int) : StaticOrderMapper.Field(index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand in 1400..1430 }
            .nextWithin(3) { it.opcode == ALOAD }
            .next { it.opcode == ALOAD }
            .next { it.opcode == PUTFIELD && it.fieldType == Array<Any>::class.type && it.fieldOwner == type<Component>() }
}

@DependsOn(Component.decode::class, Component.readListenerTriggers::class)
abstract class WidgetListenerTriggers(index: Int) : OrderMapper.InMethod.Field(Component.decode::class, index) {
    override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Component.readListenerTriggers>().id }
            .next { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

@DependsOn(GraphicsDefaults::class)
abstract class SpriteIdsField(index: Int) : OrderMapper.InConstructor.Field(GraphicsDefaults::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
}

//@DependsOn(Sprite::class)
abstract class SpriteArrayField(archiveField: KClass<out Mapper<Field2>>) : StaticUniqueMapper.Field() {
    override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && context.fields[archiveField]!!.id == it.fieldId }
            .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == type<Sprite>().withDimensions(1) }
}
//@DependsOn(IndexedSprite::class)
abstract class IndexedSpriteArrayField(archiveField: KClass<out Mapper<Field2>>) : StaticUniqueMapper.Field() {
    override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && context.fields[archiveField]!!.id == it.fieldId }
            .prevWithin(10) { it.opcode == GETSTATIC && it.fieldType == type<IndexedSprite>().withDimensions(1) }
}

@DependsOn(Client.runClientScript0::class, ClientScriptEvent::class)
abstract class ScriptEventFieldConst(ldc: Int) : UniqueMapper.InMethod.Field(Client.runClientScript0::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == ldc }
            .nextWithin(6) { it.opcode == GETFIELD && it.fieldOwner == type<ClientScriptEvent>() }
}

@DependsOn(Occluder::class, Client.Scene_addOccluder::class)
abstract class OccluderField(index: Int) : OrderMapper.InMethod.Field(Client.Scene_addOccluder::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Occluder>() }
}