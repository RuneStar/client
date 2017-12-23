package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Method2

@SinceVersion(141)
@DependsOn(TaskDataProvider::class)
class SoundTaskDataProvider : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<TaskDataProvider>()) }

    @DependsOn(TaskData::class)
    class taskData : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<TaskData>() }
    }
}