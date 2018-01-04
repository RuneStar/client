package org.runestar.client.updater.mapper.std

import org.runestar.client.updater.mapper.std.classes.*
import org.runestar.client.updater.mapper.*
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.extensions.Predicate
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

//@DependsOn(EvictingHashTable::class)
abstract class CachedDefinitionMapper(classMapper: KClass<out Mapper<Class2>>) : StaticUniqueMapper.Field() {
    override val predicate = predicateOf<Instruction2> { it.opcode == CHECKCAST && it.typeType == context.classes.getValue(classMapper).type }
            .prevWithin(6) { it.opcode == GETSTATIC && it.fieldType == type<EvictingHashTable>() }
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