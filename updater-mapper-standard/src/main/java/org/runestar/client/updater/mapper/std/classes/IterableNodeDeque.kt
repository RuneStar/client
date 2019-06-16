package org.runestar.client.updater.mapper.std.classes

import org.runestar.client.common.startsWith
import org.objectweb.asm.Opcodes.IF_ACMPEQ
import org.objectweb.asm.Type.BOOLEAN_TYPE
import org.objectweb.asm.Type.VOID_TYPE
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

@DependsOn(Node::class)
class IterableNodeDeque : IdentityMapper.Class() {
    override val predicate = predicateOf<Class2> { it.superType == Any::class.type }
            .and { it.interfaces.contains(Iterable::class.type) }
            .and { it.instanceFields.size == 2 }
            .and { it.instanceFields.all { it.type == type<Node>() } }

    @DependsOn(Node::class)
    class sentinel : OrderMapper.InConstructor.Field(IterableNodeDeque::class, 0) {
        override val predicate = predicateOf<Instruction2> { it.isField && it.fieldType == type<Node>() }
    }

    @DependsOn(Node::class, sentinel::class)
    class current : IdentityMapper.InstanceField() {
        override val predicate = predicateOf<Field2> { it.type == type<Node>() && it != field<sentinel>() }
    }

    @MethodParameters("node")
    @DependsOn(Node::class, Node.next::class)
    class addFirst : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Node>()) }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.next>().id } == 5 }
    }

    @MethodParameters("node")
    @DependsOn(Node::class, Node.next::class)
    class addLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.startsWith(type<Node>()) }
                .and { it.instructions.count { it.isField && it.fieldId == field<Node.next>().id } == 4 }
    }

    @MethodParameters("node")
    @DependsOn(Node::class, Node.next::class)
    class previousOrLast : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.arguments.startsWith(type<Node>()) }
    }

    @MethodParameters
    @DependsOn(Node::class, previousOrLast::class)
    class last : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.instructions.any { it.isMethod && it.methodId == method<previousOrLast>().id } }
    }

    @MethodParameters
    @DependsOn(Node::class, previousOrLast::class, last::class, Node.remove::class)
    class previous : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == type<Node>() }
                .and { it.instructions.none { it.isMethod && it.methodId == method<Node.remove>().id } }
                .and { it != method<previousOrLast>() }
                .and { it != method<last>() }
    }

    @MethodParameters()
    class clear0 : IdentityMapper.InstanceMethod() {
        override val predicate = predicateOf<Method2> { it.returnType == VOID_TYPE }
                .and { it.arguments.size in 0..1 }
                .and { it.instructions.any { it.opcode == IF_ACMPEQ } }
    }
}