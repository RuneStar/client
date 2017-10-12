package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.id
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.*
import java.io.RandomAccessFile

class AccessFile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == RandomAccessFile::class.type } == 1 }

    class file : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == RandomAccessFile::class.type }
    }

    class capacity : OrderMapper.InConstructor.Field(AccessFile::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    class index : OrderMapper.InConstructor.Field(AccessFile::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == LONG_TYPE }
    }

    @SinceVersion(154)
    @MethodParameters("sync")
    class closeSync : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == RandomAccessFile::close.id } }
    }

    @SinceVersion(154)
    @MethodParameters()
    @DependsOn(closeSync::class)
    class close : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<closeSync>().id } }
    }

    @MethodParameters("index")
    class seek : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(LONG_TYPE) }
                .and { it.instructions.any { it.isMethod && it.methodId == RandomAccessFile::seek.id } }
    }

    @MethodParameters()
    class length : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == LONG_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == RandomAccessFile::length.id } }
    }

    @MethodParameters("dst", "dstIndex", "length")
    class read : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type, INT_TYPE, INT_TYPE) }
    }

    @MethodParameters("src", "srcIndex", "length")
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type, INT_TYPE, INT_TYPE) }
    }
}