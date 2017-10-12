package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*

@DependsOn(ReferenceTable::class)
class CacheReferenceTable : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<ReferenceTable>() }

    @DependsOn(CacheIndex::class)
    class idx : OrderMapper.InConstructor.Field(CacheReferenceTable::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<CacheIndex>() }
    }

    @DependsOn(CacheIndex::class)
    class idx255 : OrderMapper.InConstructor.Field(CacheReferenceTable::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<CacheIndex>() }
    }
}