package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.*
import java.lang.reflect.Modifier

class AbstractIndexCache : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceFields.count { it.type == Any::class.type.withDimensions(2) } == 1 }

    class setIndexReference : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    @DependsOn(AbstractIndexCache::class)
    class noCopyArchives : OrderMapper.InConstructor.Field(AbstractIndexCache::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(AbstractIndexCache::class)
    class noCopyRecords : OrderMapper.InConstructor.Field(AbstractIndexCache::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(setIndexReference::class)
    class archiveCrcs : OrderMapper.InMethod.Field(setIndexReference::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class archiveVersions : OrderMapper.InMethod.Field(setIndexReference::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class recordCounts : OrderMapper.InMethod.Field(setIndexReference::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class archiveNameHashes : OrderMapper.InMethod.Field(setIndexReference::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class archives : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type.withDimensions(1) }
    }

    class records : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type.withDimensions(2) }
    }

    @DependsOn(IntHashTable::class)
    class archiveNameHashTable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IntHashTable>() }
    }

    @DependsOn(IntHashTable::class)
    class recordNameHashTables : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IntHashTable>().withDimensions(1) }
    }

    @DependsOn(setIndexReference::class)
    class recordNameHashes : OrderMapper.InMethod.Field(setIndexReference::class, -1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @MethodParameters("archive", "xteaKey")
    class loadRecords : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, IntArray::class.type) }
    }

    @MethodParameters("archive", "record")
    @DependsOn(getModifiableRecordEncrypted::class)
    class getModifiableRecord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getModifiableRecordEncrypted>().id } }
    }

    @MethodParameters("archive", "record")
    @DependsOn(getModifiableRecordEncrypted::class)
    class getRecord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.none { it.isMethod && it.methodId == method<getModifiableRecordEncrypted>().id } }
                .and { it.instructions.none { it.opcode == Opcodes.NEW && it.typeType == RuntimeException::class.type } }
    }

    @MethodParameters("archiveOrRecord")
    @DependsOn(getModifiableRecord::class)
    class getModifiableRecordFlat : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == Opcodes.NEW && it.typeType == RuntimeException::class.type } }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getModifiableRecord>().id } }
    }

    @MethodParameters("archiveOrRecord")
    @DependsOn(getRecord::class)
    class getRecordFlat : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == Opcodes.NEW && it.typeType == RuntimeException::class.type } }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getRecord>().id } }
    }

    @MethodParameters("archive", "record", "xteaKey")
    class getModifiableRecordEncrypted : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, IntArray::class.type) }
                .and { it.arguments.size in 3..4 }
    }
}