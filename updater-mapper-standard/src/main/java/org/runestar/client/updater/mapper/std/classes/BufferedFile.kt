package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.LONG_TYPE
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(AccessFile::class)
class BufferedFile : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<AccessFile>() } == 1 }

    @DependsOn(AccessFile::class)
    class accessFile : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<AccessFile>() }
    }

    @MethodParameters()
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

    @MethodParameters("index")
    class seek : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(LONG_TYPE) }
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