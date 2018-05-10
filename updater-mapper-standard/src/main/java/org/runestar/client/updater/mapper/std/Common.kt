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

@DependsOn(Sprite::class)
abstract class SpritesFieldMapper(s: String) : AllUniqueMapper.Field() {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == s }
            .nextWithin(5) { it.opcode == PUTSTATIC && it.fieldType == type<Sprite>().withDimensions(1) }
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

@DependsOn(PlatformInfo.length::class)
abstract class PlatformInfoString(index: Int) : OrderMapper.InMethod.Field(PlatformInfo.length::class, index) {
    override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == String::class.type }
}

@DependsOn(Client.friendSystem::class)
@SinceVersion(164)
abstract class UserComparatorClass(opcode: Int) : AllUniqueMapper.Class() {
    override val predicate = predicateOf<Instruction2> { it.opcode == SIPUSH && it.intOperand == opcode }
            .nextWithin(30) { it.isField && it.fieldId == field<Client.friendSystem>().id }
            .nextWithin(3) { it.opcode == NEW }
}

@DependsOn(Client.Scene_buildVisiblityMaps::class)
abstract class SceneViewportField(index: Int) : OrderMapper.InMethod.Field(Client.Scene_buildVisiblityMaps::class, index) {
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