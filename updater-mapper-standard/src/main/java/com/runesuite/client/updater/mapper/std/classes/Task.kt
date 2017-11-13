package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import java.lang.reflect.Modifier

class Task : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.any { Modifier.isVolatile(it.access) && it.type == Any::class.type } }

    class payload : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { Modifier.isVolatile(it.access) && it.type == Any::class.type }
    }

    class objectArgument : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { !Modifier.isVolatile(it.access) && it.type == Any::class.type }
    }

    class status : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { Modifier.isVolatile(it.access) && it.type == INT_TYPE }
    }

    class task : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Task>() }
    }

    @DependsOn(TaskHandler.newTask::class)
    class type : OrderMapper.InMethod.Field(TaskHandler.newTask::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }

    @DependsOn(TaskHandler.newTask::class)
    class intArgument : OrderMapper.InMethod.Field(TaskHandler.newTask::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD }
    }
}