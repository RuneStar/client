package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.std.ConstructorPutField
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(Skeleton::class)
class Animation : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Skeleton>() } == 1 }

    @DependsOn(Skeleton::class)
    class skeleton : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Skeleton>() }
    }

    class transformCount : ConstructorPutField(Animation::class, 0, INT_TYPE)
    class transformSkeletonLabels : ConstructorPutField(Animation::class, 0, IntArray::class.type)

    class transformXs : ConstructorPutField(Animation::class, 1, IntArray::class.type)
    class transformYs : ConstructorPutField(Animation::class, 2, IntArray::class.type)
    class transformZs : ConstructorPutField(Animation::class, 3, IntArray::class.type)

    class hasAlphaTransform : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }
}