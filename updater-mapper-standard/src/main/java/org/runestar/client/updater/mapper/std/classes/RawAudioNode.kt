package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.Predicate
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2

@DependsOn(AbstractIntNode::class)
class RawAudioNode : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractIntNode>() }

    class buffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    class sampleRate : OrderMapper.InConstructor.Field(RawAudioNode::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size == 5 }
    }

    class start : OrderMapper.InConstructor.Field(RawAudioNode::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size == 5 }
    }

    class end : OrderMapper.InConstructor.Field(RawAudioNode::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
        override val constructorPredicate = predicateOf<Method2> { it.arguments.size == 5 }
    }

    @MethodParameters("resampler")
    class resample : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }
}