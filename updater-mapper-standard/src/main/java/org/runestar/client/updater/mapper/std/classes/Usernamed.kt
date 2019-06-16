package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Method2
import java.lang.reflect.Modifier

@DependsOn(Username::class)
class Usernamed : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { Modifier.isInterface(it.access) }
            .and { it.instanceMethods.size == 1 }
            .and { it.instanceMethods.first().returnType == type<Username>() }

    class username : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }
}