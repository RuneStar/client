package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@SinceVersion(160)
@DependsOn(AbstractChannel::class)
class PacketWriter : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<AbstractChannel>() } }

    @DependsOn(AbstractChannel::class)
    class channel0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractChannel>() }
    }

    @MethodParameters()
    @DependsOn(AbstractChannel::class)
    class getChannel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractChannel>() }
    }

    @MethodParameters("channel")
    @DependsOn(AbstractChannel::class)
    class setChannel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<AbstractChannel>()) }
    }

    @DependsOn(channel0::class)
    @MethodParameters()
    class removeChannel : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<channel0>().id } }
                .and { it.instructions.any { it.opcode == ACONST_NULL } }
                .and { it.instructions.none { it.isMethod } }
    }

    @DependsOn(Buffer::class)
    class buffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Buffer>() }
    }

    @DependsOn(PacketBuffer::class)
    class packetBuffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PacketBuffer>() }
    }

    @DependsOn(IterableNodeDeque::class)
    class packetBufferNodes : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    @DependsOn(IsaacCipher::class)
    class isaacCipher : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IsaacCipher>() }
    }

    @MethodParameters()
    @DependsOn(AbstractChannel.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AbstractChannel.close>().id } }
    }
}