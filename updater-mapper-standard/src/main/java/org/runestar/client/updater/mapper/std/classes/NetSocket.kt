package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.NEWARRAY
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.id
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket

class NetSocket : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.instanceFields.count { it.type == Socket::class.type } == 1 }
            .and { it.instanceFields.count { it.type == InputStream::class.type } == 1 }

    class finalize : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.name == "finalize" }
    }

    class socket : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Socket::class.type }
    }

    class inputStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == InputStream::class.type }
    }

    class outputStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == OutputStream::class.type }
    }

    class array : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    @MethodParameters("dst", "dstIndex", "length")
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.arguments.startsWith(ByteArray::class.type, INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.isMethod && it.methodId ==
                        Triple(InputStream::class.type, "read", getMethodType(INT_TYPE, ByteArray::class.type, INT_TYPE, INT_TYPE)) } }
    }

    @MethodParameters()
    class readUnsignedByte : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId ==
                        Triple(InputStream::class.type, "read", getMethodType(INT_TYPE)) } }
    }

    @MethodParameters()
    class available : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == InputStream::available.id } }
    }

    @MethodParameters("src", "srcIndex", "length")
    class write0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.opcode == NEWARRAY } }
    }

    @MethodParameters("src", "srcIndex", "length")
    @DependsOn(write0::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.instructions.any { it.isMethod && it.methodId == method<write0>().id } }
    }

    @DependsOn(Task::class)
    class task : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Task>() }
    }

    @DependsOn(TaskHandler::class)
    class taskHandler : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<TaskHandler>() }
    }

    class isClosed : OrderMapper.InConstructor.Field(NetSocket::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    class exceptionWriting : OrderMapper.InConstructor.Field(NetSocket::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @MethodParameters()
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == "join" } }
    }

    @MethodParameters("length")
    class isAvailable : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
    }
}