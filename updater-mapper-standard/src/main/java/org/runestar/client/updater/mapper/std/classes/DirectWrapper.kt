package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(Wrapper::class)
@SinceVersion(165)
class DirectWrapper : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Wrapper>() }
            .and { it.instanceFields.any { it.type == Any::class.type } }

    class obj : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type }
    }

    @MethodParameters()
    @DependsOn(Wrapper.get::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Wrapper.get>().mark }
    }

    @MethodParameters()
    @DependsOn(Wrapper.get::class, Wrapper.isSoft::class)
    class isSoft : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<Wrapper.isSoft>().mark }
    }
}