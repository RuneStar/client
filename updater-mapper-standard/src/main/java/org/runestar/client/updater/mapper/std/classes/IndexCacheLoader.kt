package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@SinceVersion(162)
@DependsOn(IndexCache::class)
class IndexCacheLoader : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<IndexCache>() } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }

    @DependsOn(IndexCache::class)
    class indexCache : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexCache>() }
    }
}