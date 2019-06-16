package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.runestar.client.updater.mapper.IdentityMapper
import org.runestar.client.updater.mapper.OrderMapper
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
import org.objectweb.asm.Opcodes.*
import org.objectweb.asm.Type.*

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
                .and { it.instructions.none { it.opcode == ICONST_1 } }
    }

    @MethodParameters()
    @DependsOn(Node::class, get::class)
    class next : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == ICONST_1 } }
                .and { it != method<get>() }
    }

//    @MethodParameters()
//    class clear : IdentityMapper.InstanceMethod() {
//        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
//                .and { it.arguments.size in 0..1 }
//    }

    @DependsOn(Node::class, next::class)
    class current : OrderMapper.InMethod.Field(next::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == type<Node>() }
    }

    @DependsOn(current::class, Node::class)
    class currentGet : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() && it != field<current>() }
    }
}