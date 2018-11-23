package org.runestar.client.updater.mapper.std

import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type
import org.objectweb.asm.Type.BYTE_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.std.classes.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
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

@DependsOn(Client.newIndexCache::class, IndexCache::class)
abstract class IndexCacheFieldMapper(order: Int) : StaticOrderMapper.Field(order) {
    override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<Client.newIndexCache>().id }
            .next { it.opcode == PUTSTATIC && it.fieldType == type<IndexCache>() }
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
@SinceVersion(164)
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
abstract class ActorHitSplatField(index: Int) : OrderMapper.InConstructor.Field(Actor::class, index, 5) {
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

@SinceVersion(162)
@DependsOn(TriBool::class)
abstract class TriBoolConst(index: Int) : OrderMapper.InClassInitializer.Field(TriBool::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == type<TriBool>() }
}

@DependsOn(Model.animate::class)
abstract class ModelTransformTempInt(index: Int) : OrderMapper.InMethod.Field(Model.animate::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTSTATIC && it.fieldType == INT_TYPE }
}

@DependsOn(Client.newScript::class, Script::class)
abstract class ScriptField(index: Int, type: Type) : OrderMapper.InMethod.Field(Client.newScript::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type && it.fieldOwner == type<Script>() }
}

@DependsOn(Widget.decodeLegacy::class)
abstract class WidgetInvArray(index: Int) : OrderMapper.InMethod.Field(Widget.decodeLegacy::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == BIPUSH && it.intOperand == 20 }
            .nextIn(2) { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

@DependsOn(Widget.decodeLegacy::class)
abstract class Widget10Array(index: Int) : OrderMapper.InMethod.Field(Widget.decodeLegacy::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == NEWARRAY && it.intOperand == 10 }
            .next { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

@DependsOn(Widget.decode::class)
abstract class WidgetListener(index: Int) : OrderMapper.InMethod.Field(Widget.decode::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == Array<Any?>::class.type }
}

@DependsOn(Widget.decode::class, Widget.readListenerTriggers::class)
abstract class WidgetListenerTriggers(index: Int) : OrderMapper.InMethod.Field(Widget.decode::class, index) {
    override val predicate = predicateOf<Instruction2> { it.isMethod && it.methodId == method<Widget.readListenerTriggers>().id }
            .next { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
}

@DependsOn(SpriteIds::class)
abstract class SpriteIdsField(index: Int) : OrderMapper.InConstructor.Field(SpriteIds::class, index) {
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

@DependsOn(Client.runScript0::class, ScriptEvent::class)
abstract class ScriptEventFieldConst(ldc: Int) : UniqueMapper.InMethod.Field(Client.runScript0::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == ldc }
            .nextWithin(6) { it.opcode == GETFIELD && it.fieldOwner == type<ScriptEvent>() }
}

@DependsOn(Occluder::class, Client.Scene_addOccluder::class)
abstract class OccluderField(index: Int) : OrderMapper.InMethod.Field(Client.Scene_addOccluder::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE && it.fieldOwner == type<Occluder>() }
}