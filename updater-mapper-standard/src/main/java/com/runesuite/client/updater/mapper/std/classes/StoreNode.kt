package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(Node::class, IndexStore::class)
class StoreNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<IndexStore>() } }

    @DependsOn(IndexStore::class)
    class indexStore : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexStore>() }
    }

    @DependsOn(CacheReferenceTable::class)
    class cacheReferenceTable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<CacheReferenceTable>() }
    }
}