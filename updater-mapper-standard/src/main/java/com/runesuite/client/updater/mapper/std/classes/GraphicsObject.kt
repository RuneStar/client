package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

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