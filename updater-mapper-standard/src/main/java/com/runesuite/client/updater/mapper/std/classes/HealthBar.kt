package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(Node::class, NodeDeque2::class)
class HealthBar : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.size == 2 }
            .and { it.instanceFields.count { it.type == type<NodeDeque2>() } == 1 }
            .and { it.instanceFields.all { it.type in it.jar } }

    @DependsOn(NodeDeque2::class)
    class definition : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type != type<NodeDeque2>() }
    }

    @DependsOn(NodeDeque2::class)
    class hitSplats : InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeDeque2>() }
    }
}