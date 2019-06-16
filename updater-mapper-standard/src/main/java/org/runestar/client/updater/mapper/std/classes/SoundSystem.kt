package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions

@DependsOn(PcmPlayer::class)
class SoundSystem : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.interfaces.contains(Runnable::class.type) }
            .and { it.instanceFields.any { it.type == type<PcmPlayer>().withDimensions(1) } }

    class players : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PcmPlayer>().withDimensions(1) }
    }
}