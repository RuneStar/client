package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2

@DependsOn(PcmStream::class)
class MusicPatchPcmStream : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<PcmStream>() }
            .and { it.instanceFields.size == 3 }

    @DependsOn(NodeDeque::class)
    class queue : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeDeque>() }
    }

    @DependsOn(PcmStreamMixer::class)
    class mixer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PcmStreamMixer>() }
    }

    @DependsOn(MidiPcmStream::class)
    class superStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<MidiPcmStream>() }
    }
}