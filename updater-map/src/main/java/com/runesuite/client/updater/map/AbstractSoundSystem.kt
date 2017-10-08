package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Method2

@DependsOn(SoundSystem::class)
class AbstractSoundSystem : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<SoundSystem>().superType == it.type }

    @MethodParameters()
    @DependsOn(SoundSystem.flush::class)
    class flush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.flush>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.close>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.write>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.available::class)
    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.available>().mark }
    }

    @MethodParameters("bufferSize")
    @DependsOn(SoundSystem.open::class)
    class open : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.open>().mark }
    }
}