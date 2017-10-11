package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(TaskData::class)
class Task0 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(Runnable::class.type) }
            .and { it.instanceFields.any { it.type == type<TaskData>().withDimensions(1) } }

    class taskData : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TaskData>().withDimensions(1) }
    }
}