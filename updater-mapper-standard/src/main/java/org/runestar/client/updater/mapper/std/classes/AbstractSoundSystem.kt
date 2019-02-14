package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.extensions.*
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.tree.Instruction2

@DependsOn(SoundSystem::class)
class AbstractSoundSystem : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<SoundSystem>().superType == it.type }

    @MethodParameters()
    @DependsOn(SoundSystem.flush::class)
    class flush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.flush>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.close>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.write>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.remaining::class)
    class remaining : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.remaining>().mark }
    }

    @MethodParameters("bufferSize")
    @DependsOn(SoundSystem.open::class)
    class open : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.open>().mark }
    }

    class ints : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == INT_TYPE.withDimensions(1) }
    }

    @MethodParameters()
    @DependsOn(flush::class)
    class tryFlush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<flush>().id } }
    }

    @MethodParameters()
    @DependsOn(close::class)
    class shutdown : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<close>().id } }
                .and { it.instructions.any { it.opcode == ICONST_2 } }
    }

    class timeMs : OrderMapper.InConstructor.Field(AbstractSoundSystem::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    @MethodParameters()
    class run : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.isEmpty() && it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == LDC && it.ldcCst == 256000 } }
    }

    @MethodParameters()
    @DependsOn(SoundSystem.init::class)
    class init : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundSystem.init>().mark }
    }

    @DependsOn(Client.newSoundSystem::class)
    class size : OrderMapper.InMethod.Field(Client.newSoundSystem::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(Client.newSoundSystem::class)
    class capacity : OrderMapper.InMethod.Field(Client.newSoundSystem::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}