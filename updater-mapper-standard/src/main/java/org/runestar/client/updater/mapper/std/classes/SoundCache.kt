package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.GETFIELD
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(NodeHashTable::class)
class SoundCache : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.size == 4 }
            .and { it.instanceFields.count { it.type == type<NodeHashTable>() } == 2 }

    @DependsOn(Client.readSoundEffect::class)
    class getSoundEffect0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size == 3 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<Client.readSoundEffect>().id } }
    }

    @DependsOn(Client.readMusicSample::class)
    class getMusicSample0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size == 3 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<Client.readMusicSample>().id } }
    }

    @DependsOn(getSoundEffect0::class)
    class getSoundEffect : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size == 2 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getSoundEffect0>().id } }
    }

    @DependsOn(getMusicSample0::class)
    class getMusicSample : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.size == 2 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getMusicSample0>().id } }
    }

    @DependsOn(AbstractIndexCache::class, getSoundEffect0::class)
    class soundEffectIndex : UniqueMapper.InMethod.Field(getSoundEffect0::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(AbstractIndexCache::class, getMusicSample0::class)
    class musicSampleIndex : UniqueMapper.InMethod.Field(getMusicSample0::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == type<AbstractIndexCache>() }
    }

    @DependsOn(getSoundEffect0::class, NodeHashTable::class)
    class rawSounds : UniqueMapper.InMethod.Field(getSoundEffect0::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == type<NodeHashTable>() }
    }

    @DependsOn(getMusicSample0::class, NodeHashTable::class, rawSounds::class)
    class musicSamples : UniqueMapper.InMethod.Field(getMusicSample0::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == type<NodeHashTable>() && it.fieldId != field<rawSounds>().id }
    }
}