package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Type.*
import java.lang.reflect.Modifier

@DependsOn(Task::class)
class TaskHandler : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Task>() } == 2 }

    class thread : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Thread::class.type }
    }

    @DependsOn(Task::class)
    class newTask : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Task>() }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, INT_TYPE, Any::class.type) }
    }

    class isClosed : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BOOLEAN_TYPE }
    }

    @MethodParameters()
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == "join" } }
    }
}