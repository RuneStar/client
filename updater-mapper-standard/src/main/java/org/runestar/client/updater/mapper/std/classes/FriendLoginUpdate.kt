package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.objectweb.asm.Type.*

@DependsOn(Link::class)
class FriendLoginUpdate : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Link>() }
            .and { it.instanceFields.size == 3 }

    @DependsOn(Username::class)
    class username : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Username>() }
    }

    class time : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE }
    }

    class world : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == SHORT_TYPE }
    }
}