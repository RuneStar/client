package com.runesuite.client.updater.map

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2

@DependsOn(AbstractSoundSystem::class)
class SoundTask : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(Runnable::class.type) }
            .and { it.instanceFields.any { it.type == type<AbstractSoundSystem>().withDimensions(1) } }

    class soundSystems : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractSoundSystem>().withDimensions(1) }
    }
}