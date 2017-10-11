package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.id
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.NEWARRAY
import org.objectweb.asm.Type.*
import java.io.InputStream
import java.io.OutputStream

class NetSocket : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.instanceFields.count { it.type == java.net.Socket::class.type } == 1 }

    class finalize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.name == "finalize" }
    }

    class socket : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == java.net.Socket::class.type }
    }

    class inputStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == InputStream::class.type }
    }

    class outputStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == OutputStream::class.type }
    }

    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId ==
                        Triple(InputStream::class.type, "read", getMethodType(INT_TYPE, ByteArray::class.type, INT_TYPE, INT_TYPE)) } }
    }

    class readByte : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId ==
                        Triple(InputStream::class.type, "read", getMethodType(INT_TYPE)) } }
    }

    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == InputStream::available.id } }
    }

    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == NEWARRAY } }
    }

    @DependsOn(Task::class)
    class task : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Task>() }
    }

    @DependsOn(TaskHandler::class)
    class taskHandler : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TaskHandler>() }
    }

    // isClosed

    // buffer
}