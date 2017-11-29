package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(IterableNodeHashTable::class, DualNode::class)
class Script : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<DualNode>() }
            .and { it.instanceFields.any { it.type == type<IterableNodeHashTable>().withDimensions(1) } }

    class stringOperands : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Array<String>::class.type }
    }

    @DependsOn(IterableNodeHashTable::class)
    class switches : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>().withDimensions(1) }
    }
}