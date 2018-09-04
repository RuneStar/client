package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.extensions.withDimensions
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
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
    class releaseArchives : OrderMapper.InConstructor.Field(AbstractIndexCache::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(AbstractIndexCache::class)
    class shallowRecords : OrderMapper.InConstructor.Field(AbstractIndexCache::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(setIndexReference::class)
    class archiveCrcs : OrderMapper.InMethod.Field(setIndexReference::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class archiveVersions : OrderMapper.InMethod.Field(setIndexReference::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class recordCounts : OrderMapper.InMethod.Field(setIndexReference::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class archiveNameHashes : OrderMapper.InMethod.Field(setIndexReference::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
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
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @MethodParameters("archive", "xteaKey")
    class buildRecords : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, IntArray::class.type) }
    }

    @MethodParameters("archive", "record")
    @DependsOn(takeRecordEncrypted::class)
    class takeRecord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<takeRecordEncrypted>().id } }
    }

    @MethodParameters("archive", "record")
    @DependsOn(takeRecordEncrypted::class)
    class getRecord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size == 2 }
                .and { it.instructions.none { it.isMethod && it.methodId == method<takeRecordEncrypted>().id } }
                .and { it.instructions.none { it.opcode == NEW && it.typeType == RuntimeException::class.type } }
//                .and { it.instructions.count { it.opcode == ACONST_NULL } == 5 }
    }

    @MethodParameters("archiveOrRecord")
    @DependsOn(takeRecord::class)
    class takeRecordFlat : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == RuntimeException::class.type } }
                .and { it.instructions.any { it.isMethod && it.methodId == method<takeRecord>().id } }
    }

    @MethodParameters("archiveOrRecord")
    @DependsOn(getRecord::class)
    class getRecordFlat : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == RuntimeException::class.type } }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getRecord>().id } }
    }

    @MethodParameters("archive", "record", "xteaKey")
    class takeRecordEncrypted : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, IntArray::class.type) }
                .and { it.arguments.size in 3..4 }
    }

    class takeRecordByNames : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(String::class.type, String::class.type) }
    }

    class tryLoadRecord : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == ARRAYLENGTH } }
                .and { it.instructions.any { it.opcode == IFNULL || it.opcode == IFNONNULL || it.opcode == ACONST_NULL } }
    }

    class tryLoadArchive : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.none { it.opcode == ARRAYLENGTH } }
    }

    @SinceVersion(141)
    class archiveLoadPercent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 100 } }
    }

    @SinceVersion(141)
    @DependsOn(tryLoadArchive::class)
    class tryLoadArchiveByName : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<tryLoadArchive>().id } }
    }

    @SinceVersion(141)
    @DependsOn(tryLoadRecord::class)
    class tryLoadRecordByNames : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(String::class.type, String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<tryLoadRecord>().id } }
    }

    @SinceVersion(141)
    @DependsOn(archiveLoadPercent::class)
    class archiveLoadPercentByName : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<archiveLoadPercent>().id } }
    }

    @SinceVersion(141)
    @DependsOn(archiveLoadPercent::class)
    class getArchiveId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.none { it.isMethod && it.methodId == method<archiveLoadPercent>().id } }
    }

    class getRecordId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type) }
    }

    @DependsOn(tryLoadArchive::class)
    class loadArchive : UniqueMapper.InMethod.Method(tryLoadArchive::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(setIndexReference::class)
    class hash : OrderMapper.InMethod.Field(setIndexReference::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(setIndexReference::class)
    class archiveCount : OrderMapper.InMethod.Field(setIndexReference::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(setIndexReference::class)
    class archiveIds : OrderMapper.InMethod.Field(setIndexReference::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(setIndexReference::class)
    class recordIds : OrderMapper.InMethod.Field(setIndexReference::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == Array<IntArray>::class.type }
    }
}