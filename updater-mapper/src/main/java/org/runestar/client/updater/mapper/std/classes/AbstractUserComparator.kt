package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import java.lang.reflect.Modifier

@DependsOn(User::class)
class AbstractUserComparator : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { Modifier.isAbstract(it.access) }
            .and { it.interfaces.contains(Comparator::class.type) }
            .and { it.instanceMethods.any { it.arguments.startsWith(type<User>(), type<User>()) } }
}