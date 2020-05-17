package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.ACONST_NULL
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
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

@DependsOn(AbstractSocket::class)
class PacketWriter : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { it.type == type<AbstractSocket>() } }

    @DependsOn(AbstractSocket::class)
    class socket0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AbstractSocket>() }
    }

    @MethodParameters()
    @DependsOn(AbstractSocket::class)
    class getSocket : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<AbstractSocket>() }
    }

    @MethodParameters("socket")
    @DependsOn(AbstractSocket::class)
    class setSocket : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<AbstractSocket>()) }
    }

    @DependsOn(socket0::class)
    @MethodParameters()
    class removeSocket : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == PUTFIELD && it.fieldId == field<socket0>().id } }
                .and { it.instructions.any { it.opcode == ACONST_NULL } }
                .and { it.instructions.none { it.isMethod } }
    }

    @DependsOn(Packet::class)
    class packet : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Packet>() }
    }

    @DependsOn(PacketBit::class)
    class bit : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PacketBit>() }
    }

    @DependsOn(IterableNodeDeque::class)
    class bitNodes : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeDeque>() }
    }

    @DependsOn(Isaac::class)
    class isaac : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Isaac>() }
    }

    @MethodParameters()
    @DependsOn(AbstractSocket.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AbstractSocket.close>().id } }
    }

    @DependsOn(ServerProt::class)
    class serverPacket0 : UniqueMapper.InConstructor.Field(PacketWriter::class) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<ServerProt>() }
    }

    class serverPacket0Length : OrderMapper.InConstructor.Field(PacketWriter::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}