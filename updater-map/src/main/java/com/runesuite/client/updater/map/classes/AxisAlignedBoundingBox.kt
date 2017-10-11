package com.runesuite.client.updater.map.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2

@SinceVersion(141)
@DependsOn(BoundingBox::class, Model::class)
class AxisAlignedBoundingBox : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<BoundingBox>() }
            .and { it.constructors.first().arguments[0] == type<Model>() }

    @MethodParameters()
    @DependsOn(BoundingBox.draw::class)
    class draw : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<BoundingBox.draw>().mark }
    }
}