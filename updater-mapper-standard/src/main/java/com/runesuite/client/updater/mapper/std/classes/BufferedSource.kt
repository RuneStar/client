package com.runesuite.client.updater.mapper.std.classes

import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.VOID_TYPE
import java.io.IOException
import java.io.InputStream

@SinceVersion(160)
class BufferedSource : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.interfaces.contains(Runnable::class.type) }
            .and { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == InputStream::class.type } == 1 }

    class thread : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Thread::class.type }
    }

    class inputStream : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == InputStream::class.type }
    }

    class exception : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == IOException::class.type }
    }

    class buffer : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == ByteArray::class.type }
    }

    @MethodParameters()
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodName == "join" } }
    }

    class position : OrderMapper.InConstructor.Field(BufferedSource::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class limit : OrderMapper.InConstructor.Field(BufferedSource::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class capacity : OrderMapper.InConstructor.Field(BufferedSource::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}