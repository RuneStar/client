package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.std.ConstructorPutField
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2

@DependsOn(Node::class)
class AnimBase : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 2 }
            .and { it.instanceFields.count { it.type == INT_TYPE.withDimensions(1) } == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE.withDimensions(2) } == 1 }

    class id : ConstructorPutField(AnimBase::class, 0, INT_TYPE)

    class transformCount : ConstructorPutField(AnimBase::class, 1, INT_TYPE)

    class transformTypes : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }

    class transformLabels : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE.withDimensions(2) }
    }
}