package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.tree.Class2

@DependsOn(Node::class)
class Skeleton : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }
            .and { it.instanceFields.count { it.type == INT_TYPE.withDimensions(1) } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE.withDimensions(2) } == 1 }
}