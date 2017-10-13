package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.extensions.*
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type.*
import java.lang.reflect.Modifier

class ReferenceTable : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { Modifier.isAbstract(it.access) }
            .and { it.instanceFields.count { it.type == Any::class.type.withDimensions(2) } == 1 }

    class setIndexReference : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(ByteArray::class.type) }
    }

    @DependsOn(ReferenceTable::class)
    class boolean1 : OrderMapper.InConstructor.Field(ReferenceTable::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == BOOLEAN_TYPE }
    }

    @DependsOn(ReferenceTable::class)
    class boolean2 : OrderMapper.InConstructor.Field(ReferenceTable::class, 1, 2) {
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

    class archiveRecords : IdentityMapper.InstanceField() {
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
}