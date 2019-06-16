package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.objectweb.asm.Type

@DependsOn(DualNode::class)
class Rasterizer2D : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.interfaces.isEmpty() }
            .and { it.instanceFields.isEmpty() }
            .and { it.instanceMethods.isEmpty() }
            .and { it.staticFields.count { it.type == IntArray::class.type } == 1 }
            .and { it.staticFields.count { it.type == Type.INT_TYPE } == 6 }
}