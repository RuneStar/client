package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import org.objectweb.asm.Type

@DependsOn(DualNode::class, IndexCache::class)
class NetArchiveRequest : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.count { it.type == type<IndexCache>() } == 1 }

    @DependsOn(IndexCache::class)
    class indexCache : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexCache>() }
    }

    class crc : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }

    class length0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.BYTE_TYPE }
    }
}