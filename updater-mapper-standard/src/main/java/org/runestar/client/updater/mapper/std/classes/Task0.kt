package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(TaskData::class)
class Task0 : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(Runnable::class.type) }
            .and { it.instanceFields.any { it.type == type<TaskData>().withDimensions(1) } }

    class taskData : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TaskData>().withDimensions(1) }
    }
}