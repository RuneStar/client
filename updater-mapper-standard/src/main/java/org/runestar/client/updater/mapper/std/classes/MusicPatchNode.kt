package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2

@DependsOn(Node::class, MusicPatch::class)
class MusicPatchNode : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<MusicPatch>() } }

    @DependsOn(MusicPatch::class)
    class patch : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<MusicPatch>() }
    }

    @DependsOn(RawPcmStream::class)
    class stream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<RawPcmStream>() }
    }

    @DependsOn(RawSound::class)
    class rawSound : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<RawSound>() }
    }
}