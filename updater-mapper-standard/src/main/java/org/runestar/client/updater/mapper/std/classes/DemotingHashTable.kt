package org.runestar.client.updater.mapper.std.classes

import org.kxtra.lang.list.startsWith
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*
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

@DependsOn(IterableNodeHashTable::class, IterableDualNodeQueue::class)
@SinceVersion(165)
class DemotingHashTable : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<IterableNodeHashTable>() } == 1 }
            .and { it.instanceFields.count { it.type == type<IterableDualNodeQueue>() } == 1 }

    class hashTable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableNodeHashTable>() }
    }

    class queue : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<IterableDualNodeQueue>() }
    }

    @MethodParameters()
    @DependsOn(IterableDualNodeQueue.clear::class)
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<IterableDualNodeQueue.clear>().id } }
    }

    class capacity : OrderMapper.InConstructor.Field(DemotingHashTable::class, 0, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class remaining : OrderMapper.InConstructor.Field(DemotingHashTable::class, 1, 2) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("key")
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Any::class.type }
    }

    @MethodParameters("key")
    class remove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(LONG_TYPE) }
    }

    @MethodParameters("wrapper")
    @DependsOn(Wrapper::class)
    class removeWrapper : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(type<Wrapper>()) }
    }

    @MethodParameters("value", "key")
    class put1 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 2..3 }
                .and { it.arguments.startsWith(Any::class.type, LONG_TYPE) }
                .and { it.instructions.count { it.isMethod } == 1 }
    }

    @MethodParameters("value", "key", "size")
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 3..4 }
                .and { it.arguments.startsWith(Any::class.type, LONG_TYPE, INT_TYPE) }
                .and { it.instructions.count { it.isMethod } > 1 }
    }

    @MethodParameters("softeningLevel")
    class demote : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 1..2 }
                .and { it.arguments.startsWith(INT_TYPE) }
                .and { it.instructions.any { it.opcode == LADD } }
    }
}