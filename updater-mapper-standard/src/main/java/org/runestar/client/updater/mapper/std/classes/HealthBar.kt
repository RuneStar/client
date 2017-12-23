package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(Node::class, IterableNodeDeque::class)
class HealthBar : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size == 2 }
            .and { it.instanceFields.count { it.type == type<IterableNodeDeque>() } == 1 }
            .and { it.instanceFields.all { it.type in it.jar } }

    @DependsOn(IterableNodeDeque::class)
    class definition : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type != type<IterableNodeDeque>() }
    }

    @DependsOn(IterableNodeDeque::class)
    class hitSplats : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }
}