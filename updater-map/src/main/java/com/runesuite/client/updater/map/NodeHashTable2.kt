package com.runesuite.client.updater.map

import com.hunterwb.kxtra.collections.list.startsWith
import com.runesuite.mapper.IdentityMapper
import com.runesuite.mapper.OrderMapper
import com.runesuite.mapper.annotations.DependsOn
import com.runesuite.mapper.annotations.SinceVersion
import com.runesuite.mapper.extensions.and
import com.runesuite.mapper.extensions.predicateOf
import com.runesuite.mapper.extensions.type
import com.runesuite.mapper.extensions.withDimensions
import com.runesuite.mapper.tree.Class2
import com.runesuite.mapper.tree.Field2
import com.runesuite.mapper.tree.Instruction2
import com.runesuite.mapper.tree.Method2
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

@SinceVersion(141)
@DependsOn(Node::class)
class NodeHashTable2 : IdentityMapper.Class() {

    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.instanceFields.count { it.type == type<Node>().withDimensions(1) } == 1 }
            .and { it.instanceFields.count { it.type == type<Node>() } == 2 }
            .and { it.interfaces.contains(Iterable::class.type) }

    @DependsOn(Node::class)
    class buckets : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>().withDimensions(1) }
    }

    class index : OrderMapper.InConstructor.Field(NodeHashTable2::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    class size : OrderMapper.InConstructor.Field(NodeHashTable2::class, 1) {
        override val predicate = predicateOf<Instruction2> { it.opcode == Opcodes.PUTFIELD && it.fieldType == Type.INT_TYPE }
    }

    @DependsOn(Node::class)
    class get : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.startsWith(Type.LONG_TYPE) }
                .and { it.arguments.size in 1..2 }
    }

    @DependsOn(Node::class)
    class put : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
                .and { it.arguments.startsWith(type<Node>(), Type.LONG_TYPE) }
                .and { it.arguments.size in 2..3 }
    }

    @DependsOn(Node::class)
    class first : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.none { it.opcode == Opcodes.ISUB } }
    }

    @DependsOn(Node::class, get::class)
    class next : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == Opcodes.ISUB } }
                .and { it != method<get>() }
    }

    class clear : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == Type.VOID_TYPE }
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