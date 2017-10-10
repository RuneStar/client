package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2

@SinceVersion(141)
@DependsOn(TaskDataProvider::class)
class SoundTaskDataProvider : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(type<TaskDataProvider>()) }

    @DependsOn(TaskData::class)
    class taskData : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<TaskData>() }
    }
}