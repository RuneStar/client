package com.runesuite.client.updater.map.classes

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.MethodParameters
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes.GOTO
import org.objectweb.asm.Opcodes.PUTFIELD
import org.objectweb.asm.Type.INT_TYPE
import org.objectweb.asm.Type.LONG_TYPE
import org.objectweb.asm.Type.VOID_TYPE

@DependsOn(Node::class)
class NodeHashTable : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Node>().withDimensions(1) } == 1 }
            .and { it.instanceFields.count { it.type == type<Node>() } == 2 }
            .and { it.interfaces.isEmpty() }

    @DependsOn(Node::class)
    class buckets : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>().withDimensions(1) }
    }

    class index : OrderMapper.InConstructor.Field(NodeHashTable::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    class size : OrderMapper.InConstructor.Field(NodeHashTable::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == PUTFIELD && it.fieldType == INT_TYPE }
    }

    @MethodParameters("key")
    @DependsOn(Node::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.startsWith(LONG_TYPE) }
                .and { it.arguments.size in 1..2 }
    }

    @MethodParameters("node", "key")
    @DependsOn(Node::class)
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Node>(), LONG_TYPE) }
                .and { it.arguments.size in 2..3 }
    }

    @MethodParameters()
    @DependsOn(Node::class)
    class first : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.none { it.opcode == GOTO } }
    }

    @MethodParameters()
    @DependsOn(Node::class, get::class)
    class next : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == GOTO } }
                .and { it != method<get>() }
    }

    @MethodParameters()
    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
    }

    @DependsOn(Node::class, next::class)
    class current : OrderMapper.InMethod.Field(next::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == type<Node>() }
    }

    @DependsOn(current::class, Node::class)
    class currentGet : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() && it != field<current>() }
    }
}