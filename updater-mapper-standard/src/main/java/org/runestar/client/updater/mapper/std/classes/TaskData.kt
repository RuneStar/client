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
import java.util.concurrent.ScheduledExecutorService
import javax.sound.sampled.AudioSystem

@DependsOn(SoundTaskData::class)
class TaskData : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { klass<SoundTaskData>().superType == it.type }

    @MethodParameters()
    @DependsOn(SoundTaskData.flush::class)
    class flush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.flush>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundTaskData.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.close>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundTaskData.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.write>().mark }
    }

    @MethodParameters()
    @DependsOn(SoundTaskData.remaining::class)
    class remaining : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.remaining>().mark }
    }

    @MethodParameters("bufferSize")
    @DependsOn(SoundTaskData.open::class)
    class open : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<SoundTaskData.open>().mark }
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
}