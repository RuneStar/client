package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.objectweb.asm.Type

@DependsOn(DualNode::class, IndexCache::class)
class NetFileRequest : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.count { it.type == type<IndexCache>() } == 1 }

    @DependsOn(IndexCache::class)
    class indexCache : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IndexCache>() }
    }

    class crc : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE }
    }

    class padding : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.BYTE_TYPE }
    }
}