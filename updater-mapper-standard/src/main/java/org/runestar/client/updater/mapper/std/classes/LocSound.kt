package org.runestar.client.updater.mapper.std.classes

import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2

@DependsOn(Node::class, LocType::class)
class LocSound : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == type<Node>() }
            .and { it.instanceFields.any { it.type == type<LocType>() } }

    @DependsOn(LocType::class)
    class obj : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<LocType>() }
    }

    @MethodParameters()
    class set : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { true }
    }

    @DependsOn(set::class)
    class soundEffectId : OrderMapper.InMethod.Field(set::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    class soundEffectIds : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IntArray::class.type }
    }

    @DependsOn(set::class, RawPcmStream::class)
    class stream1 : UniqueMapper.InMethod.Field(set::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == type<RawPcmStream>() }
    }

    @DependsOn(stream1::class, RawPcmStream::class)
    class stream2 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<RawPcmStream>() && it != field<stream1>() }
    }
}