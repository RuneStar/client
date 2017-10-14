package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(Node::class, IterableNodeDeque::class)
class IterableNodeDequeDescendingIterator : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.contains(Iterator::class.type) }
            .and { it.instanceFields.count { it.type == type<Node>() } == 2 }
            .and { it.instanceFields.count { it.type == type<IterableNodeDeque>() } == 1 }
            .and { it.instanceFields.size == 3 }

    @DependsOn(IterableNodeDeque::class)
    class deque : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    // current

    // last
}