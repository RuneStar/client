package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import java.lang.reflect.Modifier

@DependsOn(DevicePcmPlayer::class)
class PcmPlayer : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<DevicePcmPlayer>().superType == it.type }

    @MethodParameters()
    @DependsOn(DevicePcmPlayer.discard::class)
    class discard : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<DevicePcmPlayer.discard>().mark }
    }

    @MethodParameters()
    @DependsOn(DevicePcmPlayer.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<DevicePcmPlayer.close>().mark }
    }

    @MethodParameters()
    @DependsOn(DevicePcmPlayer.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<DevicePcmPlayer.write>().mark }
    }

    @MethodParameters()
    @DependsOn(DevicePcmPlayer.position::class)
    class position : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<DevicePcmPlayer.position>().mark }
    }

    @MethodParameters("bufferSize")
    @DependsOn(DevicePcmPlayer.open::class)
    class open : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<DevicePcmPlayer.open>().mark }
    }

    class samples : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE.withDimensions(1) }
    }

    @MethodParameters()
    @DependsOn(discard::class)
    class tryDiscard : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<discard>().id } }
    }

    @MethodParameters()
    @DependsOn(close::class)
    class shutdown : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<close>().id } }
                .and { it.instructions.any { it.opcode == ICONST_2 } }
    }

    class timeMs : OrderMapper.InConstructor.Field(PcmPlayer::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    @MethodParameters()
    class run : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 256000 } }
    }

    @MethodParameters()
    @DependsOn(DevicePcmPlayer.init::class)
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<DevicePcmPlayer.init>().mark }
    }

    @DependsOn(Client.newPcmPlayer::class)
    class frequency : OrderMapper.InMethod.Field(Client.newPcmPlayer::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.newPcmPlayer::class)
    class capacity : OrderMapper.InMethod.Field(Client.newPcmPlayer::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(PcmStream::class)
    class stream0 : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<PcmStream>() }
    }

    @MethodParameters("stream")
    @DependsOn(PcmStream::class)
    class setStream : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(type<PcmStream>()) }
    }

    @MethodParameters("buffer", "length")
    class fill : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments == listOf(IntArray::class.type, INT_TYPE) }
    }

    @MethodParameters("length")
    class skip : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE && it.arguments == listOf(INT_TYPE) }
                .and { Modifier.isFinal(it.access) }
    }

    class retryTimeMs : OrderMapper.InConstructor.Field(PcmPlayer::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    class nextPosition : OrderMapper.InConstructor.Field(PcmPlayer::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}