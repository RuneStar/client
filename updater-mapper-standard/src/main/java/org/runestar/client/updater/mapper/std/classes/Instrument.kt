package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(SoundEnvelope::class)
class Instrument : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<SoundEnvelope>() } >= 6 }

    class oscillatorVolume : OrderMapper.InConstructor.Field(Instrument::class, 0, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class oscillatorPitch : OrderMapper.InConstructor.Field(Instrument::class, 1, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class oscillatorDelays : OrderMapper.InConstructor.Field(Instrument::class, 2, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class delayTime : OrderMapper.InConstructor.Field(Instrument::class, 0, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class delayDecay : OrderMapper.InConstructor.Field(Instrument::class, 1, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class duration : OrderMapper.InConstructor.Field(Instrument::class, 2, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class offset : OrderMapper.InConstructor.Field(Instrument::class, 3, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("buffer")
    @DependsOn(Buffer::class)
    class decode : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(type<Buffer>()) }
    }

    class synthesize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == IntArray::class.type }
    }

    class evaluateWave : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
    }
}