package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(Entity::class, SequenceDefinition::class)
class GraphicsObject : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Entity>() }
            .and { it.instanceMethods.size == 2 }
            .and { it.instanceFields.count { it.type == type<SequenceDefinition>() } == 1 }

    @DependsOn(SequenceDefinition::class)
    class sequenceDefinition : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<SequenceDefinition>() }
    }
}