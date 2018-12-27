package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.annotations.SinceVersion

@SinceVersion(162)
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