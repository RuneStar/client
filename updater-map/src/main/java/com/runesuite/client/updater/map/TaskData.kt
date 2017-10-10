package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Type

@DependsOn(SoundTaskData::class)
class TaskData : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<SoundTaskData>().superType == it.type }

    @MethodParameters()
    @DependsOn(SoundTaskData.flush::class)
    class flush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.flush>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundTaskData.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.close>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundTaskData.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.write>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundTaskData.available::class)
    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.available>().mark }
    }

    @MethodParameters("bufferSize")
    @DependsOn(SoundTaskData.open::class)
    class open : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.open>().mark }
    }

    class ints : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Type.INT_TYPE.withDimensions(1) }
    }
}