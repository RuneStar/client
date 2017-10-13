package com.runesuite.client.updater.mapper.std

import com.runesuite.client.updater.mapper.std.classes.IndexCache
import com.runesuite.client.updater.mapper.std.classes.Client
import com.runesuite.client.updater.mapper.std.classes.Strings
import com.runesuite.mapper.StaticOrderMapper
import com.runesuite.mapper.UniqueMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.next
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*

@DependsOn(Strings::class)
abstract class StringsUniqueMapper(string: String) : UniqueMapper.InClassInitializer.Field(Strings::class) {
    override val predicate = predicateOf<Instruction2> { it.opcode == LDC && it.ldcCst == string }
            .next { it.opcode == PUTSTATIC && it.fieldType == String::class.type }
}

@DependsOn(Client.getIndexCache::class, IndexCache::class)
abstract class IndexCacheFieldMapper(order: Int) : StaticOrderMapper.Field(order) {
    override val predicate = predicateOf<Instruction2> { it.opcode == INVOKESTATIC && it.methodId == method<Client.getIndexCache>().id }
            .next { it.opcode == PUTSTATIC && it.fieldType == type<IndexCache>() }
}