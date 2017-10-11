package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2

@DependsOn(ReferenceTable::class)
class CacheReferenceTable : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<ReferenceTable>() }
}