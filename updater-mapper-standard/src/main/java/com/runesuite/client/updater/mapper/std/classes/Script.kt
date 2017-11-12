package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2

@DependsOn(IterableNodeHashTable::class)
class Script : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.any { it.type == type<IterableNodeHashTable>().withDimensions(1) } }
}