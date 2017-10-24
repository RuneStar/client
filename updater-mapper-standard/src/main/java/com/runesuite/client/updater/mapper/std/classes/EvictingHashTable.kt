package com.runesuite.client.updater.mapper.std.classes

import com.hunterwb.kxtra.lang.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GETFIELD
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.*

@DependsOn(DualNodeDeque::class, DualNode::class, NodeHashTable::class)
class EvictingHashTable : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<DualNode>() } == 1 }
            .and { it.instanceFields.count { it.type == type<NodeHashTable>() } == 1 }
            .and { it.instanceFields.count { it.type == type<DualNodeDeque>() } == 1 }

    @DependsOn(NodeHashTable::class)
    class hashTable : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<NodeHashTable>() }
    }

    @DependsOn(DualNodeDeque::class)
    class deque : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it. type == type<DualNodeDeque>() }
    }

    @MethodParameters()
    @DependsOn(NodeHashTable::class, NodeHashTable.clear::class)
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.instructions.any { it.isMethod && it.methodId == method<NodeHashTable.clear>().id } }
    }


    @MethodParameters("dualNode", "key")
    @DependsOn(DualNode::class)
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<DualNode>(), LONG_TYPE) }
    }

    @MethodParameters("key")
    class remove : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(LONG_TYPE) }
    }

    @MethodParameters("key")
    @DependsOn(DualNode::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<DualNode>() }
                .and { it.arguments.startsWith(LONG_TYPE) }
    }

    @DependsOn(clear::class)
    class capacity : OrderMapper.InMethod.Field(clear::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == GETFIELD && it.fieldType == INT_TYPE }
    }

    @DependsOn(clear::class)
    class remainingCapacity : OrderMapper.InMethod.Field(clear::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }
}