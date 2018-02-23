package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@SinceVersion(164)
@DependsOn(Buddy::class)
class ClanMate : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Buddy>() }
            .and { it.instanceFields.count { it.type == BOOLEAN_TYPE } == 0 }
}