package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.UniqueMapper
import org.runestar.client.updater.mapper.DependsOn
import org.runestar.client.updater.mapper.MethodParameters
import org.runestar.client.updater.mapper.and
import org.runestar.client.updater.mapper.predicateOf
import org.runestar.client.updater.mapper.type
import org.runestar.client.updater.mapper.withDimensions
import org.runestar.client.updater.mapper.Class2
import org.runestar.client.updater.mapper.Field2
import org.runestar.client.updater.mapper.Instruction2
import org.runestar.client.updater.mapper.Method2
import java.lang.reflect.Modifier

class AbstractArchive : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceFields.count { it.type == Any::class.type.withDimensions(2) } == 1 }

    class decodeIndex : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    @DependsOn(AbstractArchive::class)
    class releaseGroups : OrderMapper.InConstructor.Field(AbstractArchive::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(AbstractArchive::class)
    class shallowFiles : OrderMapper.InConstructor.Field(AbstractArchive::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(decodeIndex::class)
    class groupCrcs : OrderMapper.InMethod.Field(decodeIndex::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(decodeIndex::class)
    class groupVersions : OrderMapper.InMethod.Field(decodeIndex::class, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(decodeIndex::class)
    class fileCounts : OrderMapper.InMethod.Field(decodeIndex::class, 3) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(decodeIndex::class)
    class groupNameHashes : OrderMapper.InMethod.Field(decodeIndex::class, 4) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    class groups : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type.withDimensions(1) }
    }

    class files : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == Any::class.type.withDimensions(2) }
    }

    @DependsOn(IntHashTable::class)
    class groupNameHashTable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IntHashTable>() }
    }

    @DependsOn(IntHashTable::class)
    class fileNameHashTables : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IntHashTable>().withDimensions(1) }
    }

    @DependsOn(decodeIndex::class)
    class fileNameHashes : OrderMapper.InMethod.Field(decodeIndex::class, -1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE.withDimensions(2) }
    }

    @MethodParameters("group", "xteaKey")
    class buildFiles : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, IntArray::class.type) }
    }

    @MethodParameters("group", "file")
    @DependsOn(takeFileEncrypted::class)
    class takeFile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size in 2..3 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<takeFileEncrypted>().id } }
    }

    @MethodParameters("group", "file")
    @DependsOn(takeFileEncrypted::class)
    class getFile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.arguments.size == 2 }
                .and { it.instructions.none { it.isMethod && it.methodId == method<takeFileEncrypted>().id } }
                .and { it.instructions.none { it.opcode == NEW && it.typeType == RuntimeException::class.type } }
//                .and { it.instructions.count { it.opcode == ACONST_NULL } == 5 }
    }

    @MethodParameters("groupOrFile")
    @DependsOn(takeFile::class)
    class takeFileFlat : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == RuntimeException::class.type } }
                .and { it.instructions.any { it.isMethod && it.methodId == method<takeFile>().id } }
    }

    @MethodParameters("groupOrFile")
    @DependsOn(getFile::class)
    class getFileFlat : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.arguments.size in 1..2 }
                .and { it.instructions.any { it.opcode == NEW && it.typeType == RuntimeException::class.type } }
                .and { it.instructions.any { it.isMethod && it.methodId == method<getFile>().id } }
    }

    @MethodParameters("group", "file", "xteaKey")
    class takeFileEncrypted : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE, IntArray::class.type) }
                .and { it.arguments.size in 3..4 }
    }

    @MethodParameters("groupName", "fileName")
    class takeFileByNames : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == ByteArray::class.type }
                .and { it.arguments.startsWith(String::class.type, String::class.type) }
    }

    @MethodParameters("group", "file")
    class tryLoadFile : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == ARRAYLENGTH } }
                .and { it.instructions.any { it.opcode == IFNULL || it.opcode == IFNONNULL || it.opcode == ACONST_NULL } }
    }

    @MethodParameters("group")
    class tryLoadGroup : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.none { it.opcode == ARRAYLENGTH } }
    }

    class groupLoadPercent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == BIPUSH && it.intOperand == 100 } }
    }

    @MethodParameters("groupName")
    @DependsOn(tryLoadGroup::class)
    class tryLoadGroupByName : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<tryLoadGroup>().id } }
    }

    @MethodParameters("groupName", "fileName")
    @DependsOn(tryLoadFile::class)
    class tryLoadFileByNames : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == BOOLEAN_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(String::class.type, String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<tryLoadFile>().id } }
    }

    @MethodParameters("groupName")
    @DependsOn(groupLoadPercent::class)
    class groupLoadPercentByName : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.any { it.isMethod && it.methodId == method<groupLoadPercent>().id } }
    }

    @MethodParameters("groupName")
    @DependsOn(groupLoadPercent::class)
    class getGroupId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(String::class.type) }
                .and { it.instructions.none { it.isMethod && it.methodId == method<groupLoadPercent>().id } }
    }

    @MethodParameters("group", "fileName")
    class getFileId : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, String::class.type) }
    }

    @DependsOn(tryLoadGroup::class)
    class loadGroup : UniqueMapper.InMethod.Method(tryLoadGroup::class) {
        override val predicate = predicateOf<Instruction2> { it.isMethod }
    }

    @DependsOn(decodeIndex::class)
    class hash : OrderMapper.InMethod.Field(decodeIndex::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(decodeIndex::class)
    class groupCount : OrderMapper.InMethod.Field(decodeIndex::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(decodeIndex::class)
    class groupIds : OrderMapper.InMethod.Field(decodeIndex::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == IntArray::class.type }
    }

    @DependsOn(decodeIndex::class)
    class fileIds : OrderMapper.InMethod.Field(decodeIndex::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == Array<IntArray>::class.type }
    }
}