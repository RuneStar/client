package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.Predicate
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

@DependsOn(AccessFile::class)
class BufferedFile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<AccessFile>() } == 1 }

    @DependsOn(AccessFile::class)
    class accessFile : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AccessFile>() }
    }

    @MethodParameters()
    @SinceVersion(154)
    @DependsOn(AccessFile.close::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AccessFile.close>().id } }
    }

    class capacity : OrderMapper.InConstructor.Field(BufferedFile::class, 3, 5) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    class readBuffer : OrderMapper.InConstructor.Field(BufferedFile::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == ByteArray::class.type }
    }

    class writeBuffer : OrderMapper.InConstructor.Field(BufferedFile::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == ByteArray::class.type }
    }

    @MethodParameters()
    class length : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
    }

    @MethodParameters("dst", "dstIndex", "length")
    @DependsOn(AccessFile.read::class)
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AccessFile.read>().id } }
    }

    @MethodParameters("dst")
    class readFill : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type) }
                .and { it.arguments.size in 1..2 }
    }

    @MethodParameters("src", "srcIndex", "length")
    @DependsOn(AccessFile.write::class)
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AccessFile.write>().id } }
    }

    @MethodParameters()
    @DependsOn(AccessFile.write::class)
    class flush : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AccessFile.write>().id } }
    }

    @MethodParameters()
    @DependsOn(AccessFile.read::class)
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<AccessFile.read>().id } }
    }
}