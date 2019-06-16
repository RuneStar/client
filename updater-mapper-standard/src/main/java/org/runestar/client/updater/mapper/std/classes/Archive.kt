package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.*
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
import org.objectweb.asm.Type.*

@DependsOn(AbstractArchive::class)
class Archive : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractArchive>() }

    @DependsOn(ArchiveDisk::class)
    class archiveDisk : OrderMapper.InConstructor.Field(Archive::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<ArchiveDisk>() }
    }

    @DependsOn(ArchiveDisk::class)
    class masterDisk : OrderMapper.InConstructor.Field(Archive::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<ArchiveDisk>() }
    }

    class index : OrderMapper.InConstructor.Field(Archive::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

//    @MethodParameters
    class write : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(INT_TYPE, ByteArray::class.type, BOOLEAN_TYPE, BOOLEAN_TYPE) }
    }

    @MethodParameters()
    class loadAllLocal : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == IINC } }
    }

    class validGroups : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BooleanArray::class.type }
    }

    @DependsOn(ArchiveDisk::class)
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<ArchiveDisk>()) }
    }

    @DependsOn(AbstractArchive.loadGroup::class)
    class loadGroup : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractArchive.loadGroup>().mark }
    }

    @DependsOn(masterDisk::class)
    class loadIndex : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<masterDisk>().id } }
    }

    @DependsOn(AbstractArchive.groupLoadPercent::class)
    class groupLoadPercent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractArchive.groupLoadPercent>().mark }
    }

    @DependsOn(groupLoadPercent::class)
    class loadPercent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<groupLoadPercent>().id } }
    }

    @DependsOn(loadIndex::class)
    class indexCrc : OrderMapper.InMethod.Field(loadIndex::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(loadIndex::class)
    class indexVersion : OrderMapper.InMethod.Field(loadIndex::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}