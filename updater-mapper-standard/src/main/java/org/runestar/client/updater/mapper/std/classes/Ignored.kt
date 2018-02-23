package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2

@SinceVersion(164)
@DependsOn(User::class)
class Ignored : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<User>() }
            .and { it.instanceFields.count() == 1 }
            .and { it.instanceFields.count { it.type == INT_TYPE } == 1 }
}