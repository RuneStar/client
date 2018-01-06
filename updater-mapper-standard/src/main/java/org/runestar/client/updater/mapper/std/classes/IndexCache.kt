package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
import org.runestar.client.updater.mapper.annotations.DependsOn
import org.runestar.client.updater.mapper.annotations.MethodParameters
import org.runestar.client.updater.mapper.annotations.SinceVersion
import org.runestar.client.updater.mapper.extensions.and
import org.runestar.client.updater.mapper.extensions.predicateOf
import org.runestar.client.updater.mapper.extensions.type
import org.runestar.client.updater.mapper.tree.Class2
import org.runestar.client.updater.mapper.tree.Field2
import org.runestar.client.updater.mapper.tree.Instruction2
import org.runestar.client.updater.mapper.tree.Method2
import org.objectweb.asm.Type.*

@DependsOn(AbstractIndexCache::class)
class IndexCache : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == type<AbstractIndexCache>() }

    @DependsOn(IndexStore::class)
    class indexStore : OrderMapper.InConstructor.Field(IndexCache::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<IndexStore>() }
    }

    @DependsOn(IndexStore::class)
    class referenceStore : OrderMapper.InConstructor.Field(IndexCache::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == type<IndexStore>() }
    }

    class index : OrderMapper.InConstructor.Field(IndexCache::class, 1) {
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

    class validArchives : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == BooleanArray::class.type }
    }

    @DependsOn(IndexStore::class)
    class load : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<IndexStore>()) }
    }

    @DependsOn(AbstractIndexCache.loadArchive::class)
    class loadArchive : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractIndexCache.loadArchive>().mark }
    }

    @DependsOn(referenceStore::class)
    class loadIndexReference : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(INT_TYPE, INT_TYPE) }
                .and { it.instructions.any { it.opcode == GETFIELD && it.fieldId == field<referenceStore>().id } }
    }

    @SinceVersion(141)
    @DependsOn(AbstractIndexCache.archiveLoadPercent::class)
    class archiveLoadPercent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.mark == method<AbstractIndexCache.archiveLoadPercent>().mark }
    }

    @SinceVersion(141)
    @DependsOn(archiveLoadPercent::class)
    class loadPercent : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == INT_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.isMethod && it.methodId == method<archiveLoadPercent>().id } }
    }

    @DependsOn(loadIndexReference::class)
    class indexReferenceCrc : OrderMapper.InMethod.Field(loadIndexReference::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(loadIndexReference::class)
    class indexReferenceVersion : OrderMapper.InMethod.Field(loadIndexReference::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}