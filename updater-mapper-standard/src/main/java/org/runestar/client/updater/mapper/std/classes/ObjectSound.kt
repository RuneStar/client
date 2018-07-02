package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(Node::class, ObjectDefinition::class)
class ObjectSound : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<ObjectDefinition>() } }

    @DependsOn(ObjectDefinition::class)
    class obj : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<ObjectDefinition>() }
    }
}