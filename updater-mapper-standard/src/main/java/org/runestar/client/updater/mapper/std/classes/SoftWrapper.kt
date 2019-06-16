package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import java.lang.ref.SoftReference

@DependsOn(Wrapper::class)
class SoftWrapper : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Wrapper>() }
            .and { it.instanceFields.any { it.type == SoftReference::class.type } }

    class ref : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == SoftReference::class.type }
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